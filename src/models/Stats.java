package models;

import java.time.LocalDateTime;

public class Stats {
    private int statId;
    private int userId;
    private int totalExercises;
    private int totalCalories;
    private int totalDuration;
    private double avgCaloriesPerSession;
    private LocalDateTime lastUpdated;
    
    public Stats(int userId) {
        this.userId = userId;
        this.totalExercises = 0;
        this.totalCalories = 0;
        this.totalDuration = 0;
        this.avgCaloriesPerSession = 0;
    }
    
    public Stats(int statId, int userId, int totalExercises, int totalCalories, 
                 int totalDuration, double avgCaloriesPerSession, LocalDateTime lastUpdated) {
        this.statId = statId;
        this.userId = userId;
        this.totalExercises = totalExercises;
        this.totalCalories = totalCalories;
        this.totalDuration = totalDuration;
        this.avgCaloriesPerSession = avgCaloriesPerSession;
        this.lastUpdated = lastUpdated;
    }
    
    public int getStatId() { return statId; }
    public void setStatId(int statId) { this.statId = statId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getTotalExercises() { return totalExercises; }
    public void setTotalExercises(int totalExercises) { this.totalExercises = totalExercises; }
    
    public int getTotalCalories() { return totalCalories; }
    public void setTotalCalories(int totalCalories) { this.totalCalories = totalCalories; }
    
    public int getTotalDuration() { return totalDuration; }
    public void setTotalDuration(int totalDuration) { this.totalDuration = totalDuration; }
    
    public double getAvgCaloriesPerSession() { return avgCaloriesPerSession; }
    public void setAvgCaloriesPerSession(double avgCaloriesPerSession) { 
        this.avgCaloriesPerSession = avgCaloriesPerSession; 
    }
    
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    
    @Override
    public String toString() {
        return "Stats{" +
                "userId=" + userId +
                ", totalExercises=" + totalExercises +
                ", totalCalories=" + totalCalories +
                ", totalDuration=" + totalDuration +
                ", avgCaloriesPerSession=" + avgCaloriesPerSession +
                '}';
    }
}