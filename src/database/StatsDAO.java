package database;

import models.Stats;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class StatsDAO {
    
    public boolean createStats(int userId) {
        String sql = "INSERT INTO stats (user_id) VALUES (?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création des statistiques: " + e.getMessage());
            return false;
        }
    }
    
    public void updateStats(int userId) {
        String sql = "UPDATE stats SET total_exercises = " +
                    "(SELECT COUNT(*) FROM exercises WHERE user_id = ?), " +
                    "total_calories = (SELECT COALESCE(SUM(calories_burned), 0) FROM exercises WHERE user_id = ?), " +
                    "total_duration = (SELECT COALESCE(SUM(duration_minutes), 0) FROM exercises WHERE user_id = ?), " +
                    "avg_calories_per_session = " +
                    "(SELECT COALESCE(AVG(calories_burned), 0) FROM exercises WHERE user_id = ?) " +
                    "WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, userId);
            pstmt.setInt(4, userId);
            pstmt.setInt(5, userId);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour des statistiques: " + e.getMessage());
        }
    }
    
    public Stats getStats(int userId) {
        String sql = "SELECT * FROM stats WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                LocalDateTime lastUpdated = rs.getTimestamp("last_updated").toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime();
                
                return new Stats(
                    rs.getInt("stat_id"),
                    rs.getInt("user_id"),
                    rs.getInt("total_exercises"),
                    rs.getInt("total_calories"),
                    rs.getInt("total_duration"),
                    rs.getDouble("avg_calories_per_session"),
                    lastUpdated
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des statistiques: " + e.getMessage());
        }
        return null;
    }
}