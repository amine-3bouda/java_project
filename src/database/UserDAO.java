package database;

import models.User;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class UserDAO {
    
    public boolean registerUser(String username, String password, String email) {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement: " + e.getMessage());
            return false;
        }
    }
    
    public User loginUser(String username, String password) {
        String sql = "SELECT user_id, username, email, created_at, updated_at FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String usernameResult = rs.getString("username");
                String email = rs.getString("email");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime updatedAt = rs.getTimestamp("updated_at").toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime();
                
                return new User(userId, usernameResult, email, createdAt, updatedAt);
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion: " + e.getMessage());
            return null;
        }
    }
    
    public boolean userExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'utilisateur: " + e.getMessage());
        }
        return false;
    }
    
    public User getUserById(int userId) {
        String sql = "SELECT user_id, username, email, created_at, updated_at FROM users WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                LocalDateTime createdAt = rs.getTimestamp("created_at").toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime updatedAt = rs.getTimestamp("updated_at").toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime();
                
                return new User(rs.getInt("user_id"), rs.getString("username"), 
                               rs.getString("email"), createdAt, updatedAt);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'utilisateur: " + e.getMessage());
        }
        return null;
    }
}