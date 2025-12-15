package controllers;

import models.Exercise;
import models.Stats;
import database.ExerciseDAO;
import database.StatsDAO;
import java.time.LocalDate;
import java.util.List;

public class ExerciseController {
    private ExerciseDAO exerciseDAO;
    private StatsDAO statsDAO;
    
    public ExerciseController() {
        this.exerciseDAO = new ExerciseDAO();
        this.statsDAO = new StatsDAO();
    }
    
    public boolean addExercise(int userId, String exerciseName, int durationMinutes, 
                               int caloriesBurned, LocalDate exerciseDate) {
        // Validation
        if (exerciseName == null || exerciseName.trim().isEmpty()) {
            System.err.println("Le nom de l'exercice ne peut pas être vide");
            return false;
        }
        
        if (durationMinutes <= 0) {
            System.err.println("La durée doit être supérieure à 0");
            return false;
        }
        
        if (caloriesBurned < 0) {
            System.err.println("Les calories ne peuvent pas être négatives");
            return false;
        }
        
        if (exerciseDate == null || exerciseDate.isAfter(LocalDate.now())) {
            System.err.println("La date de l'exercice ne peut pas être dans le futur");
            return false;
        }
        
        Exercise exercise = new Exercise(userId, exerciseName, durationMinutes, caloriesBurned, exerciseDate);
        
        if (exerciseDAO.addExercise(exercise)) {
            statsDAO.updateStats(userId);
            return true;
        }
        return false;
    }
    
    public List<Exercise> getUserExercises(int userId) {
        return exerciseDAO.getUserExercises(userId);
    }
    
    public List<Exercise> getUserExercisesByDate(int userId, LocalDate startDate, LocalDate endDate) {
        return exerciseDAO.getUserExercisesByDate(userId, startDate, endDate);
    }
    
    public Stats getUserStats(int userId) {
        return statsDAO.getStats(userId);
    }
    
    public boolean deleteExercise(int exerciseId, int userId) {
        if (exerciseDAO.deleteExercise(exerciseId)) {
            statsDAO.updateStats(userId);
            return true;
        }
        return false;
    }
}