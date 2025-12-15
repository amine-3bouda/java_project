package views;

import controllers.AuthController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {
    private AuthController authController;
    private Stage primaryStage;
    private Runnable onLoginSuccess;
    
    public LoginView(AuthController authController, Stage primaryStage) {
        this.authController = authController;
        this.primaryStage = primaryStage;
    }
    
    public void setOnLoginSuccess(Runnable runnable) {
        this.onLoginSuccess = runnable;
    }
    
    public Scene createLoginScene() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #1a1a1a; -fx-alignment: center;");
        
        VBox card = new VBox(20);
        card.setPadding(new Insets(40));
        card.setMaxWidth(400);
        card.setStyle("-fx-background-color: #2d2d2d; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(255,140,0,0.4), 20, 0, 0, 10);");
        
        Label titleLabel = new Label("FITNESS TRACKER");
        titleLabel.setStyle("-fx-font-size: 32; -fx-font-weight: bold; -fx-text-fill: #ff8c00; -fx-alignment: center;");
        
        Label subtitleLabel = new Label("Suivi des Exercices et Fitness");
        subtitleLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #cccccc; -fx-alignment: center;");
        
        Label usernameLabel = new Label("Nom d'utilisateur:");
        usernameLabel.setStyle("-fx-font-size: 13; -fx-font-weight: 600; -fx-text-fill: #ffffff;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Entrez votre nom d'utilisateur");
        usernameField.setStyle("-fx-padding: 12; -fx-font-size: 13; -fx-background-color: #1a1a1a; -fx-text-fill: white; -fx-background-radius: 8; -fx-border-color: #555; -fx-border-radius: 8;");
        
        Label passwordLabel = new Label("Mot de passe:");
        passwordLabel.setStyle("-fx-font-size: 13; -fx-font-weight: 600; -fx-text-fill: #ffffff;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Entrez votre mot de passe");
        passwordField.setStyle("-fx-padding: 12; -fx-font-size: 13; -fx-background-color: #1a1a1a; -fx-text-fill: white; -fx-background-radius: 8; -fx-border-color: #555; -fx-border-radius: 8;");
        
        Button loginButton = new Button("Se connecter");
        loginButton.setStyle("-fx-padding: 14 40; -fx-font-size: 14; -fx-font-weight: bold; -fx-background-color: #ff8c00; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(255,140,0,0.3), 8, 0, 0, 2);");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-padding: 14 40; -fx-font-size: 14; -fx-font-weight: bold; -fx-background-color: #e67e00; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(255,140,0,0.5), 12, 0, 0, 3);"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-padding: 14 40; -fx-font-size: 14; -fx-font-weight: bold; -fx-background-color: #ff8c00; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(255,140,0,0.3), 8, 0, 0, 2);"));
        
        Button registerButton = new Button("S'enregistrer");
        registerButton.setStyle("-fx-padding: 14 40; -fx-font-size: 14; -fx-font-weight: 600; -fx-background-color: transparent; -fx-text-fill: #ff8c00; -fx-cursor: hand; -fx-background-radius: 8; -fx-border-color: #ff8c00; -fx-border-width: 2; -fx-border-radius: 8;");
        registerButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setOnMouseEntered(e -> registerButton.setStyle("-fx-padding: 14 40; -fx-font-size: 14; -fx-font-weight: 600; -fx-background-color: rgba(255,140,0,0.1); -fx-text-fill: #ff8c00; -fx-cursor: hand; -fx-background-radius: 8; -fx-border-color: #ff8c00; -fx-border-width: 2; -fx-border-radius: 8;"));
        registerButton.setOnMouseExited(e -> registerButton.setStyle("-fx-padding: 14 40; -fx-font-size: 14; -fx-font-weight: 600; -fx-background-color: transparent; -fx-text-fill: #ff8c00; -fx-cursor: hand; -fx-background-radius: 8; -fx-border-color: #ff8c00; -fx-border-width: 2; -fx-border-radius: 8;"));
        
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #ff4444; -fx-font-size: 12; -fx-font-weight: 600; -fx-padding: 8; -fx-background-color: rgba(255,68,68,0.1); -fx-background-radius: 5;");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            
            if (authController.loginUser(username, password)) {
                if (onLoginSuccess != null) {
                    onLoginSuccess.run();
                }
            } else {
                errorLabel.setText("Nom d'utilisateur ou mot de passe incorrect");
                errorLabel.setVisible(true);
                errorLabel.setManaged(true);
            }
        });
        
        registerButton.setOnAction(e -> {
            showRegisterDialog();
        });
        
        card.getChildren().addAll(
            titleLabel,
            subtitleLabel,
            usernameLabel,
            usernameField,
            passwordLabel,
            passwordField,
            loginButton,
            registerButton,
            errorLabel
        );
        
        root.getChildren().add(card);
        
        return new Scene(root, 600, 700);
    }
    
    private void showRegisterDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Créer un compte");
        
        VBox content = new VBox(18);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #2d2d2d; -fx-background-radius: 10;");
        content.setMinWidth(450);
        
        Label titleLabel = new Label("Créer un nouveau compte");
        titleLabel.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #ff8c00;");
        
        Label subtitleLabel = new Label("Rejoignez-nous pour suivre vos progrès fitness");
        subtitleLabel.setStyle("-fx-font-size: 13; -fx-text-fill: #cccccc; -fx-padding: 0 0 10 0;");
        
        Label usernameLabel = new Label("Nom d'utilisateur");
        usernameLabel.setStyle("-fx-font-size: 13; -fx-font-weight: 600; -fx-text-fill: #ffffff;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Choisissez un nom d'utilisateur unique");
        usernameField.setStyle("-fx-padding: 12; -fx-font-size: 13; -fx-background-color: #1a1a1a; -fx-text-fill: white; -fx-background-radius: 8; -fx-border-color: #555; -fx-border-radius: 8; -fx-border-width: 1.5;");
        usernameField.setOnMouseEntered(e -> usernameField.setStyle("-fx-padding: 12; -fx-font-size: 13; -fx-background-color: #1a1a1a; -fx-text-fill: white; -fx-background-radius: 8; -fx-border-color: #ff8c00; -fx-border-radius: 8; -fx-border-width: 1.5;"));
        usernameField.setOnMouseExited(e -> usernameField.setStyle("-fx-padding: 12; -fx-font-size: 13; -fx-background-color: #1a1a1a; -fx-text-fill: white; -fx-background-radius: 8; -fx-border-color: #555; -fx-border-radius: 8; -fx-border-width: 1.5;"));
        
        Label emailLabel = new Label("Adresse e-mail");
        emailLabel.setStyle("-fx-font-size: 13; -fx-font-weight: 600; -fx-text-fill: #ffffff;");
        TextField emailField = new TextField();
        emailField.setPromptText("exemple@email.com");
        emailField.setStyle("-fx-padding: 12; -fx-font-size: 13; -fx-background-color: #1a1a1a; -fx-text-fill: white; -fx-background-radius: 8; -fx-border-color: #555; -fx-border-radius: 8; -fx-border-width: 1.5;");
        emailField.setOnMouseEntered(e -> emailField.setStyle("-fx-padding: 12; -fx-font-size: 13; -fx-background-color: #1a1a1a; -fx-text-fill: white; -fx-background-radius: 8; -fx-border-color: #ff8c00; -fx-border-radius: 8; -fx-border-width: 1.5;"));
        emailField.setOnMouseExited(e -> emailField.setStyle("-fx-padding: 12; -fx-font-size: 13; -fx-background-color: #1a1a1a; -fx-text-fill: white; -fx-background-radius: 8; -fx-border-color: #555; -fx-border-radius: 8; -fx-border-width: 1.5;"));
        
        Label passwordLabel = new Label("Mot de passe");
        passwordLabel.setStyle("-fx-font-size: 13; -fx-font-weight: 600; -fx-text-fill: #ffffff;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Choisissez un mot de passe sécurisé");
        passwordField.setStyle("-fx-padding: 12; -fx-font-size: 13; -fx-background-color: #1a1a1a; -fx-text-fill: white; -fx-background-radius: 8; -fx-border-color: #555; -fx-border-radius: 8; -fx-border-width: 1.5;");
        passwordField.setOnMouseEntered(e -> passwordField.setStyle("-fx-padding: 12; -fx-font-size: 13; -fx-background-color: #1a1a1a; -fx-text-fill: white; -fx-background-radius: 8; -fx-border-color: #ff8c00; -fx-border-radius: 8; -fx-border-width: 1.5;"));
        passwordField.setOnMouseExited(e -> passwordField.setStyle("-fx-padding: 12; -fx-font-size: 13; -fx-background-color: #1a1a1a; -fx-text-fill: white; -fx-background-radius: 8; -fx-border-color: #555; -fx-border-radius: 8; -fx-border-width: 1.5;"));
        
        Label confirmPasswordLabel = new Label("Confirmer le mot de passe");
        confirmPasswordLabel.setStyle("-fx-font-size: 13; -fx-font-weight: 600; -fx-text-fill: #ffffff;");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Saisissez à nouveau votre mot de passe");
        confirmPasswordField.setStyle("-fx-padding: 12; -fx-font-size: 13; -fx-background-color: #1a1a1a; -fx-text-fill: white; -fx-background-radius: 8; -fx-border-color: #555; -fx-border-radius: 8; -fx-border-width: 1.5;");
        confirmPasswordField.setOnMouseEntered(e -> confirmPasswordField.setStyle("-fx-padding: 12; -fx-font-size: 13; -fx-background-color: #1a1a1a; -fx-text-fill: white; -fx-background-radius: 8; -fx-border-color: #ff8c00; -fx-border-radius: 8; -fx-border-width: 1.5;"));
        confirmPasswordField.setOnMouseExited(e -> confirmPasswordField.setStyle("-fx-padding: 12; -fx-font-size: 13; -fx-background-color: #1a1a1a; -fx-text-fill: white; -fx-background-radius: 8; -fx-border-color: #555; -fx-border-radius: 8; -fx-border-width: 1.5;"));
        
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #ff4444; -fx-font-size: 12; -fx-font-weight: 600; -fx-padding: 10; -fx-background-color: rgba(255,68,68,0.1); -fx-background-radius: 8;");
        errorLabel.setVisible(false);
        
        content.getChildren().addAll(
            titleLabel,
            subtitleLabel,
            usernameLabel, usernameField,
            emailLabel, emailField,
            passwordLabel, passwordField,
            confirmPasswordLabel, confirmPasswordField,
            errorLabel
        );
        
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setStyle("-fx-background-color: #2d2d2d; -fx-background-radius: 10;");
        
        ButtonType registerButtonType = new ButtonType("S'enregistrer", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(registerButtonType, cancelButtonType);
        
        Button registerButton = (Button) dialog.getDialogPane().lookupButton(registerButtonType);
        registerButton.setStyle("-fx-padding: 12 30; -fx-font-size: 13; -fx-font-weight: bold; -fx-background-color: #ff8c00; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 8;");
        registerButton.setOnMouseEntered(e -> registerButton.setStyle("-fx-padding: 12 30; -fx-font-size: 13; -fx-font-weight: bold; -fx-background-color: #e67e00; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 8;"));
        registerButton.setOnMouseExited(e -> registerButton.setStyle("-fx-padding: 12 30; -fx-font-size: 13; -fx-font-weight: bold; -fx-background-color: #ff8c00; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 8;"));
        
        Button cancelButton = (Button) dialog.getDialogPane().lookupButton(cancelButtonType);
        cancelButton.setStyle("-fx-padding: 12 30; -fx-font-size: 13; -fx-font-weight: 600; -fx-background-color: #555; -fx-text-fill: #ffffff; -fx-cursor: hand; -fx-background-radius: 8;");
        cancelButton.setOnMouseEntered(e -> cancelButton.setStyle("-fx-padding: 12 30; -fx-font-size: 13; -fx-font-weight: 600; -fx-background-color: #666; -fx-text-fill: #ffffff; -fx-cursor: hand; -fx-background-radius: 8;"));
        cancelButton.setOnMouseExited(e -> cancelButton.setStyle("-fx-padding: 12 30; -fx-font-size: 13; -fx-font-weight: 600; -fx-background-color: #555; -fx-text-fill: #ffffff; -fx-cursor: hand; -fx-background-radius: 8;"));
        
        dialog.setOnCloseRequest(event -> {
            if (dialog.getResult() == registerButtonType) {
                String username = usernameField.getText();
                String email = emailField.getText();
                String password = passwordField.getText();
                String confirmPassword = confirmPasswordField.getText();
                
                if (authController.registerUser(username, password, confirmPassword, email)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("✅ Succès");
                    alert.setHeaderText("Enregistrement réussi!");
                    alert.setContentText("Bienvenue " + username + "! Vous pouvez maintenant vous connecter avec vos identifiants.");
                    
                    DialogPane alertPane = alert.getDialogPane();
                    alertPane.setStyle("-fx-background-color: white;");
                    Button okButton = (Button) alertPane.lookupButton(ButtonType.OK);
                    okButton.setStyle("-fx-padding: 10 25; -fx-font-size: 13; -fx-font-weight: bold; -fx-background-color: linear-gradient(to right, #48bb78, #38a169); -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 8;");
                    
                    alert.showAndWait();
                } else {
                    errorLabel.setText("❌ Erreur lors de l'enregistrement. Vérifiez vos données.");
                    errorLabel.setVisible(true);
                    event.consume();
                }
            }
        });
        
        dialog.showAndWait();
    }
}