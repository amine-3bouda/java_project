
param(
    [switch]$runOnly
)

function Write-Info([string]$m){ Write-Host "[INFO] $m" -ForegroundColor Cyan }
function Write-Warn([string]$m){ Write-Host "[WARN] $m" -ForegroundColor Yellow }
function Write-Err([string]$m){ Write-Host "[ERROR] $m" -ForegroundColor Red }

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
Push-Location $projectRoot

if (!(Test-Path lib)) { New-Item -ItemType Directory -Path lib | Out-Null }

$connector = Get-ChildItem lib -Filter 'mysql-connector-java*.jar' -File -ErrorAction SilentlyContinue | Select-Object -First 1
if (-not $connector) {
    Write-Warn "MySQL Connector/J not found in lib/. The script can download it for you."
    $choice = Read-Host "Download mysql-connector-java-8.0.30.jar into lib/? (Y/n)"
    if ($choice -ne 'n') {
        $url = 'https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.30/mysql-connector-java-8.0.30.jar'
        $out = 'lib\mysql-connector-java-8.0.30.jar'
        Write-Info "Downloading $url"
        try { Invoke-WebRequest -Uri $url -OutFile $out -UseBasicParsing; Write-Info "Downloaded $out" } catch { Write-Err "Download failed: $_" }
    } else { Write-Warn "Proceeding without connector. DB operations will fail." }
} else { Write-Info "Found connector: $($connector.Name)" }

if ($runOnly) {
    Write-Info "Run-only requested. Skipping compile."
    Write-Info "Running..."
    & java -cp "bin;lib/*" App
    Pop-Location
    return
}

$javac = Get-Command javac -ErrorAction SilentlyContinue
if (-not $javac) {
    Write-Err "javac (JDK) not found on PATH. Install a JDK (e.g., Temurin/Adoptium) and add javac to PATH to compile."
    Write-Host 'You can still run the existing compiled classes: java -cp "bin;lib/*" App'
    Pop-Location
    exit 1
}

# Compile sources
Write-Info "Compiling sources into bin/"
if (!(Test-Path bin)) { New-Item -ItemType Directory -Path bin | Out-Null }

$srcFiles = Get-ChildItem -Path src -Recurse -Filter '*.java' | ForEach-Object { $_.FullName }
$srcArg = [string]::Join(' ', ($srcFiles | ForEach-Object { '"' + $_ + '"' }))
$cmd = 'javac -d bin -cp "lib/*" ' + $srcArg
Write-Info $cmd
Invoke-Expression $cmd
if ($LASTEXITCODE -ne 0) {
    Write-Err "Compilation failed. Fix errors and retry."
    Pop-Location
    exit 1
}

Write-Info "Compilation successful. Running application..."
& java -cp "bin;lib/*" App

Pop-Location
