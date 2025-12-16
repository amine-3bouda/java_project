# Fitness Tracker

A modern desktop application for tracking your fitness activities, built with JavaFX and MySQL.

## Features

### Dashboard
- **Day-by-Day Statistics**: Navigate through your workout history with previous/next day buttons
- View exercises, calories burned, and total duration for any specific day
- Cannot view future dates - only today and past workouts

### Exercise Management
- **15 Pre-defined Exercise Types**: Choose from activities like running, swimming, cycling, HIIT, yoga, and more
- **Automatic Calorie Calculation**: Calories are automatically calculated based on exercise type and duration
- Each exercise has a specific calorie burn rate per minute
- Set custom dates for logged exercises

### Exercise History
- View all your past workouts in a sortable table
- See exercise name, duration, calories burned, and date
- Delete individual exercises with one click
- Dynamic table sizing based on your data

### User Authentication
- Secure login and registration system
- Each user has their own private workout data

## Technologies

- **Frontend**: JavaFX 23.0.1 (modern black and orange UI theme)
- **Backend**: Java 21
- **Database**: MySQL (JDBC)
- **Build Tool**: Apache Maven 3.9.9
- **Architecture**: MVC pattern with DAO layer

## Screenshots

The application features:
- Black background with orange accents
- Professional 4px border radius design
- Responsive tables and forms
- Real-time calorie calculations

## Quick Start

### Prerequisites
- Java 21 or higher
- MySQL server (XAMPP or standalone)
- Maven (included in project: `apache-maven-3.9.9/`)

### Database Setup
1. Start your MySQL server
2. Run the SQL script:
   ```sql
   mysql -u root < schema.sql
   ```
3. Update credentials in `src/main/java/database/DatabaseConnection.java` if needed

### Running the Application

**Option 1: Using Maven**
```bash
apache-maven-3.9.9\bin\mvn javafx:run
```

**Option 2: Using the JAR file**
```bash
java -jar target\fitness-tracker-1.0.0.jar
```
Or double-click `run.bat`

### Building from Source
```bash
apache-maven-3.9.9\bin\mvn clean package
```

## Project Structure

```
project/
├── src/main/
│   ├── java/
│   │   ├── App.java                    # Application entry point
│   │   ├── controllers/                # Business logic layer
│   │   │   ├── AuthController.java    # User authentication
│   │   │   └── ExerciseController.java # Exercise management
│   │   ├── database/                   # Data access layer
│   │   │   ├── DatabaseConnection.java # MySQL connection
│   │   │   ├── UserDAO.java           # User operations
│   │   │   ├── ExerciseDAO.java       # Exercise operations
│   │   │   └── StatsDAO.java          # Statistics operations
│   │   ├── models/                     # Data models
│   │   │   ├── User.java
│   │   │   ├── Exercise.java
│   │   │   └── Stats.java
│   │   └── views/                      # UI layer (JavaFX)
│   │       ├── LoginView.java         # Login & registration
│   │       └── DashboardView.java     # Main dashboard
│   └── resources/
│       └── views/
│           └── dashboard.css          # UI styling
├── pom.xml                            # Maven configuration
├── schema.sql                         # Database schema
└── README.md                          # This file
```

## Exercise Types & Calorie Rates

| Exercise | Calories per Minute |
|----------|---------------------|
| HIIT | 13 |
| Natation (Swimming) | 12 |
| Boxe (Boxing) | 11 |
| Rameur (Rowing) | 11 |
| Course à pied (Running) | 10 |
| Escalade (Climbing) | 10 |
| Football | 9 |
| Basketball | 9 |
| Vélo (Cycling) | 8 |
| Tennis | 8 |
| Danse (Dancing) | 7 |
| Musculation (Weight Training) | 6 |
| Marche rapide (Brisk Walking) | 5 |
| Pilates | 4 |
| Yoga | 3 |

## Database Schema

### Tables
- **users**: User accounts (id, username, password, email)
- **exercises**: Workout records (id, user_id, exercise_name, duration_minutes, calories_burned, exercise_date)
- **stats**: Aggregated statistics per user (user_id, total_exercises, total_calories, total_duration, avg_calories_per_session)

## Contributing

This is an academic project. Feel free to fork and modify for your own use.

## License

Educational project - free to use and modify.
