package controllers;

import models.User;
import database.UserDAO;
import database.StatsDAO;

public class AuthController {
    private UserDAO userDAO;
    private StatsDAO statsDAO;
    private User currentUser;
    
    public AuthController() {
        this.userDAO = new UserDAO();
        this.statsDAO = new StatsDAO();
    }
    
    public boolean registerUser(String username, String password, String confirmPassword, String email) {
        // Validation (use stdout for user-facing messages so PowerShell doesn't treat them as errors)
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Le nom d'utilisateur ne peut pas être vide. Veuillez réessayer.");
            return false;
        }

        if (username.length() < 3) {
            System.out.println("Le nom d'utilisateur doit contenir au moins 3 caractères. Veuillez réessayer.");
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            System.out.println("Le mot de passe ne peut pas être vide. Veuillez réessayer.");
            return false;
        }

        if (password.length() < 3) {
            System.out.println("Le mot de passe doit contenir au moins 3 caractères. Veuillez réessayer.");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            System.out.println("Les mots de passe ne correspondent pas. Veuillez réessayer.");
            return false;
        }

        if (email == null || !email.contains("@")) {
            System.out.println("Veuillez entrer une adresse e-mail valide. Veuillez réessayer.");
            return false;
        }

        if (userDAO.userExists(username)) {
            System.out.println("Le nom d'utilisateur existe déjà. Veuillez choisir un autre nom.");
            return false;
        }
        
        // Enregistrement
        if (userDAO.registerUser(username, password, email)) {
            // Créer les stats pour le nouvel utilisateur
            User newUser = userDAO.loginUser(username, password);
            if (newUser != null) {
                statsDAO.createStats(newUser.getUserId());
                return true;
            }
        }
        return false;
    }
    
    public boolean loginUser(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            System.out.println("Veuillez entrer un nom d'utilisateur et un mot de passe");
            return false;
        }
        
        User user = userDAO.loginUser(username, password);
        if (user != null) {
            this.currentUser = user;
            return true;
        }
        return false;
    }
    
    public void logout() {
        this.currentUser = null;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}