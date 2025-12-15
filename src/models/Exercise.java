package models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Exercise {
    private int exerciseId;
    private int userId;
    private String exerciseName;
    private int durationMinutes;
    private int caloriesBurned;
    private LocalDate exerciseDate;
    private LocalDateTime createdAt;
    
    public Exercise(int userId, String exerciseName, int durationMinutes, int caloriesBurned, LocalDate exerciseDate) {
        this.userId = userId;
        this.exerciseName = exerciseName;
        this.durationMinutes = durationMinutes;
        this.caloriesBurned = caloriesBurned;
        this.exerciseDate = exerciseDate;
    }
    
    public Exercise(int exerciseId, int userId, String exerciseName, int durationMinutes, 
                   int caloriesBurned, LocalDate exerciseDate, LocalDateTime createdAt) {
        this.exerciseId = exerciseId;
        this.userId = userId;
        this.exerciseName = exerciseName;
        this.durationMinutes = durationMinutes;
        this.caloriesBurned = caloriesBurned;
        this.exerciseDate = exerciseDate;
        this.createdAt = createdAt;
    }
    
    public int getExerciseId() { return exerciseId; }
    public void setExerciseId(int exerciseId) { this.exerciseId = exerciseId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getExerciseName() { return exerciseName; }
    public void setExerciseName(String exerciseName) { this.exerciseName = exerciseName; }
    
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    
    public int getCaloriesBurned() { return caloriesBurned; }
    public void setCaloriesBurned(int caloriesBurned) { this.caloriesBurned = caloriesBurned; }
    
    public LocalDate getExerciseDate() { return exerciseDate; }
    public void setExerciseDate(LocalDate exerciseDate) { this.exerciseDate = exerciseDate; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "Exercise{" +
                "exerciseId=" + exerciseId +
                ", exerciseName='" + exerciseName + '\'' +
                ", durationMinutes=" + durationMinutes +
                ", caloriesBurned=" + caloriesBurned +
                ", exerciseDate=" + exerciseDate +
                '}';
    }
}