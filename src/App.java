import controllers.AuthController;
import controllers.ExerciseController;
import views.LoginView;
import views.DashboardView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private AuthController authController;
    private ExerciseController exerciseController;
    private LoginView loginView;

    @Override
    public void start(Stage primaryStage) {
        try {
            authController = new AuthController();
            exerciseController = new ExerciseController();

            loginView = new LoginView(authController, primaryStage);
            Scene loginScene = loginView.createLoginScene();

            loginView.setOnLoginSuccess(() -> {
                DashboardView dashboardView =
                        new DashboardView(authController, exerciseController, primaryStage);

                Scene dashboardScene = dashboardView.createDashboardScene(() -> {
                    authController.logout();
                    primaryStage.setScene(loginScene);
                    primaryStage.setTitle("Fitness Tracker - Connexion");
                });

                primaryStage.setScene(dashboardScene);
                primaryStage.setTitle("Fitness Tracker - Tableau de bord");
            });

            primaryStage.setScene(loginScene);
            primaryStage.setTitle("Fitness Tracker - Connexion");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
