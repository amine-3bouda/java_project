CREATE DATABASE IF NOT EXISTS fitness_tracker;
USE fitness_tracker;

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS exercises (
    exercise_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    exercise_name VARCHAR(100) NOT NULL,
    duration_minutes INT NOT NULL,
    calories_burned INT NOT NULL,
    exercise_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS stats (
    stat_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    total_exercises INT DEFAULT 0,
    total_calories INT DEFAULT 0,
    total_duration INT DEFAULT 0,
    avg_calories_per_session DECIMAL(10, 2) DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_stats (user_id)
);

CREATE INDEX idx_user_exercises ON exercises(user_id, exercise_date);
CREATE INDEX idx_user_id ON users(user_id);