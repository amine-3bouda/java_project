package views;

import controllers.AuthController;
import controllers.ExerciseController;
import models.Exercise;
import models.Stats;
import database.ExerciseDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.List;

public class DashboardView {
    private AuthController authController;
    private ExerciseController exerciseController;
    private Stage primaryStage;
    private TableView<Exercise> historyTable;
    private Label exercisesValueLabel;
    private Label caloriesValueLabel;
    private Label durationValueLabel;
    private Label avgValueLabel;
    private Label dailyDateLabel;
    private Label dailyExercisesLabel;
    private Label dailyCaloriesLabel;
    private Label dailyDurationLabel;
    private Button nextButton;
    private LocalDate selectedDate = LocalDate.now();
    public DashboardView(AuthController authController, ExerciseController exerciseController, Stage primaryStage) {
        this.authController = authController;
        this.exerciseController = exerciseController;
        this.primaryStage = primaryStage;
    }
    
    public Scene createDashboardScene(Runnable onLogout) {
        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: #1a1a1a;");
        
        HBox navBar = createNavigationBar(onLogout);
        
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        Tab dashboardTab = new Tab("Tableau de bord");
        dashboardTab.setContent(createDashboardTab());

        Tab historyTab = new Tab("Historique");
        historyTab.setContent(createHistoryTab());

        Tab addExerciseTab = new Tab("Ajouter exercice");
        addExerciseTab.setContent(createAddExerciseTab());

        tabPane.getTabs().addAll(dashboardTab, addExerciseTab, historyTab);
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == dashboardTab) {
                if (dailyDateLabel != null) updateDailyStats();
            } else if (newTab == historyTab) {
                if (historyTable != null) refreshTable(historyTable);
            }
        });
        
        root.getChildren().addAll(navBar, tabPane);
        
        Scene scene = new Scene(root, 900, 700);
        scene.getStylesheets().add(getClass().getResource("dashboard.css").toExternalForm());
        
        return scene;
    }
    
    private HBox createNavigationBar(Runnable onLogout) {
        HBox navBar = new HBox(15);
        navBar.setPadding(new Insets(15, 20, 15, 20));
        navBar.setStyle("-fx-background-color: #000000; -fx-effect: dropshadow(gaussian, rgba(255,140,0,0.3), 10, 0, 0, 3);");
        
        Label titleLabel = new Label("FITNESS TRACKER - " + authController.getCurrentUser().getUsername());
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #ff8c00;");
        
        Button refreshButton = new Button("🔄 Actualiser");
        refreshButton.setStyle("-fx-padding: 10 20; -fx-font-size: 13; -fx-font-weight: 600; -fx-background-color: #ff8c00; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 4;");
        refreshButton.setOnMouseEntered(e -> refreshButton.setStyle("-fx-padding: 10 20; -fx-font-size: 13; -fx-font-weight: 600; -fx-background-color: #e67e00; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 4;"));
        refreshButton.setOnMouseExited(e -> refreshButton.setStyle("-fx-padding: 10 20; -fx-font-size: 13; -fx-font-weight: 600; -fx-background-color: #ff8c00; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 4;"));
        refreshButton.setOnAction(e -> {
            if (dailyDateLabel != null) updateDailyStats();
            if (historyTable != null) refreshTable(historyTable);
        });

        Button logoutButton = new Button("🚪 Déconnexion");
        logoutButton.setStyle("-fx-padding: 10 20; -fx-font-size: 13; -fx-font-weight: 600; -fx-background-color: #555; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 4;");
        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle("-fx-padding: 10 20; -fx-font-size: 13; -fx-font-weight: 600; -fx-background-color: #666; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 4;"));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle("-fx-padding: 10 20; -fx-font-size: 13; -fx-font-weight: 600; -fx-background-color: #555; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 4;"));
        logoutButton.setOnAction(e -> {
            authController.logout();
            onLogout.run();
        });
        
        navBar.getChildren().addAll(titleLabel, refreshButton, logoutButton);
        HBox.setHgrow(titleLabel, javafx.scene.layout.Priority.ALWAYS);
        
        return navBar;
    }
    
    private VBox createDashboardTab() {
        VBox dashboardBox = new VBox(20);
        dashboardBox.setPadding(new Insets(25));
        dashboardBox.setStyle("-fx-background-color: #1a1a1a;");

        Label dailyTitle = new Label("Statistiques par jour");
        dailyTitle.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #ff8c00;");

        VBox dailyStatsCard = createDailyStatsCard();

        dashboardBox.getChildren().addAll(dailyTitle, dailyStatsCard);
        
        return dashboardBox;
    }
    
    private VBox createDailyStatsCard() {
        VBox card = new VBox(20);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: #2d2d2d; -fx-background-radius: 4; -fx-effect: dropshadow(gaussian, rgba(255,140,0,0.2), 15, 0, 0, 5); -fx-border-color: #ff8c00; -fx-border-width: 2; -fx-border-radius: 4;");
        
        HBox navigationBar = new HBox(15);
        navigationBar.setAlignment(javafx.geometry.Pos.CENTER);
        
        Button prevButton = new Button("← Jour précédent");
        prevButton.setStyle("-fx-padding: 10 20; -fx-font-size: 13; -fx-font-weight: 600; -fx-background-color: #ff8c00; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 4;");
        prevButton.setOnMouseEntered(e -> prevButton.setStyle("-fx-padding: 10 20; -fx-font-size: 13; -fx-font-weight: 600; -fx-background-color: #e67e00; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 4;"));
        prevButton.setOnMouseExited(e -> prevButton.setStyle("-fx-padding: 10 20; -fx-font-size: 13; -fx-font-weight: 600; -fx-background-color: #ff8c00; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 4;"));
        prevButton.setOnAction(e -> {
            selectedDate = selectedDate.minusDays(1);
            updateDailyStats();
        });
        
        dailyDateLabel = new Label(selectedDate.toString());
        dailyDateLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #ff8c00; -fx-min-width: 200; -fx-alignment: center;");
        
        nextButton = new Button("Jour suivant →");
        nextButton.setStyle("-fx-padding: 10 20; -fx-font-size: 13; -fx-font-weight: 600; -fx-background-color: #ff8c00; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 4;");
        nextButton.setOnMouseEntered(e -> {
            if (!nextButton.isDisabled()) {
                nextButton.setStyle("-fx-padding: 10 20; -fx-font-size: 13; -fx-font-weight: 600; -fx-background-color: #e67e00; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 4;");
            }
        });
        nextButton.setOnMouseExited(e -> {
            if (!nextButton.isDisabled()) {
                nextButton.setStyle("-fx-padding: 10 20; -fx-font-size: 13; -fx-font-weight: 600; -fx-background-color: #ff8c00; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 4;");
            }
        });
        nextButton.setOnAction(e -> {
            if (selectedDate.isBefore(LocalDate.now())) {
                selectedDate = selectedDate.plusDays(1);
                updateDailyStats();
            }
        });
        
        Button todayButton = new Button("Aujourd'hui");
        todayButton.setStyle("-fx-padding: 10 20; -fx-font-size: 13; -fx-font-weight: 600; -fx-background-color: #555; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 4;");
        todayButton.setOnMouseEntered(e -> todayButton.setStyle("-fx-padding: 10 20; -fx-font-size: 13; -fx-font-weight: 600; -fx-background-color: #666; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 4;"));
        todayButton.setOnMouseExited(e -> todayButton.setStyle("-fx-padding: 10 20; -fx-font-size: 13; -fx-font-weight: 600; -fx-background-color: #555; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 4;"));
        todayButton.setOnAction(e -> {
            selectedDate = LocalDate.now();
            updateDailyStats();
        });
        
        navigationBar.getChildren().addAll(prevButton, dailyDateLabel, nextButton, todayButton);
        
        HBox statsRow = new HBox(15);
        statsRow.setAlignment(javafx.geometry.Pos.CENTER);
        
        dailyExercisesLabel = new Label("0");
        dailyExercisesLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: white;");
        VBox exercisesBox = createDailyStatBox("Exercices", dailyExercisesLabel);
        
        dailyCaloriesLabel = new Label("0");
        dailyCaloriesLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: white;");
        VBox caloriesBox = createDailyStatBox("Calories", dailyCaloriesLabel);
        
        dailyDurationLabel = new Label("0");
        dailyDurationLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: white;");
        VBox durationBox = createDailyStatBox("Durée (min)", dailyDurationLabel);
        
        statsRow.getChildren().addAll(exercisesBox, caloriesBox, durationBox);
        
        card.getChildren().addAll(navigationBar, statsRow);
        
        updateDailyStats();
        
        return card;
    }
    
    private VBox createDailyStatBox(String label, Label valueLabel) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: #1a1a1a; -fx-background-radius: 4; -fx-min-width: 150;");
        
        Label labelControl = new Label(label);
        labelControl.setStyle("-fx-font-size: 13; -fx-text-fill: #cccccc; -fx-font-weight: 600;");
        
        valueLabel.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: #ff8c00;");
        
        box.getChildren().addAll(labelControl, valueLabel);
        box.setAlignment(javafx.geometry.Pos.CENTER);
        
        return box;
    }
    
    private void updateDailyStats() {
        dailyDateLabel.setText(selectedDate.toString());
        
        if (nextButton != null) {
            boolean isToday = !selectedDate.isBefore(LocalDate.now());
            nextButton.setDisable(isToday);
            if (isToday) {
                nextButton.setStyle("-fx-padding: 10 20; -fx-font-size: 13; -fx-font-weight: 600; -fx-background-color: #555; -fx-text-fill: #888; -fx-background-radius: 4; -fx-opacity: 0.5;");
            } else {
                nextButton.setStyle("-fx-padding: 10 20; -fx-font-size: 13; -fx-font-weight: 600; -fx-background-color: #ff8c00; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 4;");
            }
        }
        
        ExerciseDAO exerciseDAO = new ExerciseDAO();
        List<Exercise> exercises = exerciseDAO.getUserExercisesByDate(
            authController.getCurrentUser().getUserId(), 
            selectedDate,
            selectedDate
        );
        
        int exerciseCount = exercises.size();
        int totalCalories = exercises.stream().mapToInt(Exercise::getCaloriesBurned).sum();
        int totalDuration = exercises.stream().mapToInt(Exercise::getDurationMinutes).sum();
        
        dailyExercisesLabel.setText(String.valueOf(exerciseCount));
        dailyCaloriesLabel.setText(String.valueOf(totalCalories));
        dailyDurationLabel.setText(String.valueOf(totalDuration));
    }
    
    private VBox createStatBox(String label, Label valueControl, String color) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(25));
        box.setStyle("-fx-background-color: #2d2d2d; -fx-text-fill: white; -fx-background-radius: 4; -fx-effect: dropshadow(gaussian, rgba(255,140,0,0.2), 10, 0, 0, 4); -fx-min-width: 180; -fx-border-color: #ff8c00; -fx-border-width: 2; -fx-border-radius: 4;");

        Label labelControl = new Label(label);
        labelControl.setStyle("-fx-font-size: 13; -fx-text-fill: #cccccc; -fx-font-weight: 600;");

        valueControl.setStyle("-fx-font-size: 32; -fx-font-weight: bold; -fx-text-fill: #ff8c00;");

        box.getChildren().addAll(labelControl, valueControl);
        HBox.setHgrow(box, javafx.scene.layout.Priority.ALWAYS);

        return box;
    }
    
    private VBox createAddExerciseTab() {
        VBox addBox = new VBox(18);
        addBox.setPadding(new Insets(25));
        addBox.setStyle("-fx-background-color: #1a1a1a;");
        
        VBox card = new VBox(18);
        card.setPadding(new Insets(30));
        card.setMaxWidth(600);
        card.setStyle("-fx-background-color: #2d2d2d; -fx-background-radius: 4; -fx-effect: dropshadow(gaussian, rgba(255,140,0,0.2), 15, 0, 0, 5); -fx-border-color: #ff8c00; -fx-border-width: 2; -fx-border-radius: 4;");
        
        Label titleLabel = new Label("Ajouter un nouvel exercice");
        titleLabel.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #ff8c00;");
        
        Label exerciseNameLabel = new Label("Type d'exercice:");
        exerciseNameLabel.setStyle("-fx-font-size: 13; -fx-font-weight: 600; -fx-text-fill: #ffffff;");
        
        ComboBox<String> exerciseComboBox = new ComboBox<>();
        exerciseComboBox.getItems().addAll(
            "Course à pied",
            "Vélo",
            "Natation",
            "Musculation",
            "Yoga",
            "Marche rapide",
            "Danse",
            "Football",
            "Basketball",
            "Tennis",
            "Boxe",
            "Escalade",
            "Rameur",
            "HIIT"
        );
        exerciseComboBox.setValue("Course à pied");
        exerciseComboBox.setStyle("-fx-font-size: 13;");
        exerciseComboBox.setMaxWidth(Double.MAX_VALUE);
        
        Label durationLabel = new Label("Durée (minutes):");
        durationLabel.setStyle("-fx-font-size: 13; -fx-font-weight: 600; -fx-text-fill: #ffffff;");
        Spinner<Integer> durationSpinner = new Spinner<>(1, 500, 30);
        durationSpinner.setEditable(true);
        durationSpinner.setStyle("-fx-font-size: 13;");
        
        Label caloriesLabel = new Label("Calories brûlées (estimé):");
        caloriesLabel.setStyle("-fx-font-size: 13; -fx-font-weight: 600; -fx-text-fill: #ff8c00;");
        Label caloriesValueLabel = new Label("300");
        caloriesValueLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #ff8c00; -fx-padding: 10;");
        
        Runnable updateCalories = () -> {
            String exercise = exerciseComboBox.getValue();
            int duration = durationSpinner.getValue();
            int caloriesPerMinute = getCaloriesPerMinute(exercise);
            int totalCalories = caloriesPerMinute * duration;
            caloriesValueLabel.setText(String.valueOf(totalCalories));
        };
        
        exerciseComboBox.setOnAction(e -> updateCalories.run());
        durationSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateCalories.run());
        
        updateCalories.run();
        
        Label dateLabel = new Label("Date:");
        dateLabel.setStyle("-fx-font-size: 13; -fx-font-weight: 600; -fx-text-fill: #ffffff;");
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setStyle("-fx-font-size: 13;");
        
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-font-size: 13; -fx-font-weight: 600; -fx-padding: 10; -fx-background-radius: 8;");
        
        Button addButton = new Button("Ajouter l'exercice");
        addButton.setStyle("-fx-padding: 14 40; -fx-font-size: 14; -fx-font-weight: bold; -fx-background-color: #ff8c00; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(255,140,0,0.3), 8, 0, 0, 2);");
        addButton.setMaxWidth(Double.MAX_VALUE);
        addButton.setOnMouseEntered(e -> addButton.setStyle("-fx-padding: 14 40; -fx-font-size: 14; -fx-font-weight: bold; -fx-background-color: #e67e00; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(255,140,0,0.5), 12, 0, 0, 3);"));
        addButton.setOnMouseExited(e -> addButton.setStyle("-fx-padding: 14 40; -fx-font-size: 14; -fx-font-weight: bold; -fx-background-color: #ff8c00; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(255,140,0,0.3), 8, 0, 0, 2);"));
        
        addButton.setOnAction(e -> {
            String exerciseName = exerciseComboBox.getValue();
            int duration = durationSpinner.getValue();
            int calories = Integer.parseInt(caloriesValueLabel.getText());
            LocalDate date = datePicker.getValue();
            
            if (exerciseController.addExercise(
                authController.getCurrentUser().getUserId(),
                exerciseName,
                duration,
                calories,
                date
            )) {
                messageLabel.setText("Exercice ajouté avec succès!");
                messageLabel.setStyle("-fx-text-fill: #ff8c00; -fx-font-size: 13; -fx-font-weight: 600; -fx-padding: 10; -fx-background-color: rgba(255,140,0,0.1); -fx-background-radius: 8;");
                exerciseComboBox.setValue("Course à pied");
                durationSpinner.getValueFactory().setValue(30);
                datePicker.setValue(LocalDate.now());
                updateCalories.run();
                if (historyTable != null) refreshTable(historyTable);
                if (dailyDateLabel != null) updateDailyStats();
            } else {
                messageLabel.setText("Erreur lors de l'ajout de l'exercice");
                messageLabel.setStyle("-fx-text-fill: #ff4444; -fx-font-size: 13; -fx-font-weight: 600; -fx-padding: 10; -fx-background-color: rgba(255,68,68,0.1); -fx-background-radius: 8;");
            }
        });
        
        card.getChildren().addAll(
            titleLabel,
            exerciseNameLabel, exerciseComboBox,
            durationLabel, durationSpinner,
            caloriesLabel, caloriesValueLabel,
            dateLabel, datePicker,
            addButton,
            messageLabel
        );
        
        addBox.getChildren().add(card);
        
        return addBox;
    }
    
    private VBox createHistoryTab() {
        VBox historyBox = new VBox(20);
        historyBox.setPadding(new Insets(25));
        historyBox.setStyle("-fx-background-color: #1a1a1a;");
        
        VBox card = new VBox(15);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: #2d2d2d; -fx-background-radius: 4; -fx-effect: dropshadow(gaussian, rgba(255,140,0,0.2), 15, 0, 0, 5); -fx-border-color: #ff8c00; -fx-border-width: 2; -fx-border-radius: 4;");
        
        Label titleLabel = new Label("Historique des exercices");
        titleLabel.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #ff8c00;");
        
        historyTable = new TableView<>();
        historyTable.setStyle("-fx-background-color: #1a1a1a; -fx-control-inner-background: #1a1a1a; -fx-table-cell-border-color: #555;");
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        historyTable.setPlaceholder(new Label("Aucun exercice enregistré"));
        
        TableColumn<Exercise, String> nameCol = new TableColumn<>("Exercice");
        nameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getExerciseName()));
        nameCol.setMinWidth(150);
        
        TableColumn<Exercise, Integer> durationCol = new TableColumn<>("Durée (min)");
        durationCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDurationMinutes()));
        durationCol.setMinWidth(100);
        durationCol.setStyle("-fx-alignment: CENTER;");
        
        TableColumn<Exercise, Integer> caloriesCol = new TableColumn<>("Calories");
        caloriesCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getCaloriesBurned()));
        caloriesCol.setMinWidth(100);
        caloriesCol.setStyle("-fx-alignment: CENTER;");
        
        TableColumn<Exercise, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getExerciseDate()));
        dateCol.setMinWidth(100);
        dateCol.setStyle("-fx-alignment: CENTER;");
        
        TableColumn<Exercise, Void> actionCol = new TableColumn<>("");
        actionCol.setMinWidth(100);
        actionCol.setMaxWidth(100);
        actionCol.setCellFactory(col -> new TableCell<Exercise, Void>() {
            private final Button deleteButton = new Button("Supprimer");
            
            {
                deleteButton.setStyle("-fx-padding: 6 12; -fx-background-color: #ff4444; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 6; -fx-font-size: 12;");
                deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-padding: 6 12; -fx-background-color: #cc0000; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 6; -fx-font-size: 12;"));
                deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-padding: 6 12; -fx-background-color: #ff4444; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 6; -fx-font-size: 12;"));
                deleteButton.setOnAction(event -> {
                    Exercise exercise = getTableView().getItems().get(getIndex());
                    if (exerciseController.deleteExercise(exercise.getExerciseId(), 
                        authController.getCurrentUser().getUserId())) {
                        if (historyTable != null) refreshTable(historyTable);
                        if (dailyDateLabel != null) updateDailyStats();
                    }
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });
        
        historyTable.getColumns().addAll(nameCol, durationCol, caloriesCol, dateCol, actionCol);
        historyTable.setFixedCellSize(45);

        refreshTable(historyTable);
        
        historyTable.setPrefHeight(Math.max(500, historyTable.getFixedCellSize() * (historyTable.getItems().size() + 1.5)));
        historyTable.setMaxHeight(2000);
        
        historyTable.getItems().addListener((javafx.collections.ListChangeListener.Change<? extends Exercise> c) -> {
            historyTable.setPrefHeight(Math.max(500, historyTable.getFixedCellSize() * (historyTable.getItems().size() + 1.5)));
        });
        
        card.getChildren().addAll(titleLabel, historyTable);
        
        historyBox.getChildren().add(card);
        
        return historyBox;
    }
    
    private void refreshTable(TableView<Exercise> tableView) {
        List<Exercise> exercises = exerciseController.getUserExercises(authController.getCurrentUser().getUserId());
        ObservableList<Exercise> data = FXCollections.observableArrayList(exercises);
        tableView.setItems(data);
    }

private void refreshStats() {
    if (exercisesValueLabel == null) return;
    Stats stats = exerciseController.getUserStats(authController.getCurrentUser().getUserId());
    if (stats != null) {
        exercisesValueLabel.setText(String.valueOf(stats.getTotalExercises()));
        caloriesValueLabel.setText(String.valueOf(stats.getTotalCalories()));
        durationValueLabel.setText(String.valueOf(stats.getTotalDuration()));
        avgValueLabel.setText(String.format("%.2f", stats.getAvgCaloriesPerSession()));
    }
}

private int getCaloriesPerMinute(String exercise) {
    switch (exercise) {
        case "Course à pied": return 10;
        case "Vélo": return 8;
        case "Natation": return 12;
        case "Musculation": return 6;
        case "Yoga": return 3;
        case "Marche rapide": return 5;
        case "Danse": return 7;
        case "Football": return 9;
        case "Basketball": return 9;
        case "Tennis": return 8;
        case "Boxe": return 11;
        case "Escalade": return 10;
        case "Rameur": return 11;
        case "HIIT": return 13;
        default: return 7;
    }
}

}