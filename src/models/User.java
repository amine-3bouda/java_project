package models;

import java.time.LocalDateTime;

public class User {
    private int userId;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
    
    public User(int userId, String username, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}