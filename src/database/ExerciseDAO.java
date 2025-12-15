package database;

import models.Exercise;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ExerciseDAO {
    
    public boolean addExercise(Exercise exercise) {
        String sql = "INSERT INTO exercises (user_id, exercise_name, duration_minutes, calories_burned, exercise_date) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, exercise.getUserId());
            pstmt.setString(2, exercise.getExerciseName());
            pstmt.setInt(3, exercise.getDurationMinutes());
            pstmt.setInt(4, exercise.getCaloriesBurned());
            pstmt.setDate(5, java.sql.Date.valueOf(exercise.getExerciseDate()));
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout d'exercice: " + e.getMessage());
            return false;
        }
    }
    
    public List<Exercise> getUserExercises(int userId) {
        List<Exercise> exercises = new ArrayList<>();
        String sql = "SELECT * FROM exercises WHERE user_id = ? ORDER BY exercise_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                LocalDate exerciseDate = rs.getDate("exercise_date").toLocalDate();
                LocalDateTime createdAt = rs.getTimestamp("created_at").toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime();
                
                Exercise exercise = new Exercise(
                    rs.getInt("exercise_id"),
                    rs.getInt("user_id"),
                    rs.getString("exercise_name"),
                    rs.getInt("duration_minutes"),
                    rs.getInt("calories_burned"),
                    exerciseDate,
                    createdAt
                );
                exercises.add(exercise);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des exercices: " + e.getMessage());
        }
        return exercises;
    }
    
    public List<Exercise> getUserExercisesByDate(int userId, LocalDate startDate, LocalDate endDate) {
        List<Exercise> exercises = new ArrayList<>();
        String sql = "SELECT * FROM exercises WHERE user_id = ? AND exercise_date BETWEEN ? AND ? ORDER BY exercise_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setDate(2, java.sql.Date.valueOf(startDate));
            pstmt.setDate(3, java.sql.Date.valueOf(endDate));
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                LocalDate exerciseDate = rs.getDate("exercise_date").toLocalDate();
                LocalDateTime createdAt = rs.getTimestamp("created_at").toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime();
                
                Exercise exercise = new Exercise(
                    rs.getInt("exercise_id"),
                    rs.getInt("user_id"),
                    rs.getString("exercise_name"),
                    rs.getInt("duration_minutes"),
                    rs.getInt("calories_burned"),
                    exerciseDate,
                    createdAt
                );
                exercises.add(exercise);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des exercices: " + e.getMessage());
        }
        return exercises;
    }
    
    public boolean deleteExercise(int exerciseId) {
        String sql = "DELETE FROM exercises WHERE exercise_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, exerciseId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression d'exercice: " + e.getMessage());
            return false;
        }
    }
    
    public List<DailyStats> getDailyStats(int userId, int days) {
        List<DailyStats> dailyStats = new ArrayList<>();
        String sql = "SELECT exercise_date, " +
                    "COUNT(*) as exercise_count, " +
                    "SUM(calories_burned) as total_calories, " +
                    "SUM(duration_minutes) as total_duration " +
                    "FROM exercises WHERE user_id = ? " +
                    "AND exercise_date >= DATE_SUB(CURDATE(), INTERVAL ? DAY) " +
                    "GROUP BY exercise_date " +
                    "ORDER BY exercise_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, days);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                LocalDate date = rs.getDate("exercise_date").toLocalDate();
                int exerciseCount = rs.getInt("exercise_count");
                int totalCalories = rs.getInt("total_calories");
                int totalDuration = rs.getInt("total_duration");
                
                dailyStats.add(new DailyStats(date, exerciseCount, totalCalories, totalDuration));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des statistiques quotidiennes: " + e.getMessage());
        }
        return dailyStats;
    }
    
    public static class DailyStats {
        private LocalDate date;
        private int exerciseCount;
        private int totalCalories;
        private int totalDuration;
        
        public DailyStats(LocalDate date, int exerciseCount, int totalCalories, int totalDuration) {
            this.date = date;
            this.exerciseCount = exerciseCount;
            this.totalCalories = totalCalories;
            this.totalDuration = totalDuration;
        }
        
        public LocalDate getDate() { return date; }
        public int getExerciseCount() { return exerciseCount; }
        public int getTotalCalories() { return totalCalories; }
        public int getTotalDuration() { return totalDuration; }
    }
}