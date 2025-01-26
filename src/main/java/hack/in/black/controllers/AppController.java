/*
 * This class is the controller for the main window of the application.
 * It initializes the main window and switches between different views.
 */

package hack.in.black.controllers;

import java.io.IOException;

import hack.in.black.models.AppModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AppController {
    private final Stage primaryStage;
    private final AppModel appModel;
    private MainViewController mainViewController;

    public AppController(Stage primaryStage, AppModel appModel) {
        this.primaryStage = primaryStage;
        this.appModel = appModel;
    }

    // This method initializes the main window of the application
    // and sets the default view to the warehouses view
    public void initializeMainWindow() {
        try {
            // Load fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
            Parent root = loader.load();

            // Get mainViewController and set the appController
            mainViewController = loader.getController();
            mainViewController.setAppController(this);
            mainViewController.setAppModel(appModel);

            // Create new scene
            Scene scene = new Scene(root);

            // Set size restrictions
            primaryStage.setMinWidth(700);
            primaryStage.setMinHeight(500);

            // Create a new scene based on the fxml file
            primaryStage.setScene(scene);
            primaryStage.show();

            // Show warehouses view by default
            switchView("Warehouses");

        } catch (IOException e) {
            System.out.println("Error loading FXML file: " + e.getMessage());
        }
    }

    // This method is used to switch between different views
    // the view parameter is the name of the fxml file
    public void switchView(String view) {
        try {
            // Load fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + view + ".fxml"));
            Parent root = loader.load();

            // Set the new view
            mainViewController.getScrollPaneContent().setContent(root);

            // Set appcontroller and appmodel
            ViewController currentViewController = loader.getController();
            currentViewController.setAppController(this);
            currentViewController.setAppModel(appModel);
            currentViewController.lateInitialize();

            // Update GUI
            mainViewController.updateGUI(view);

        } catch (IOException e) {
            System.out.println("Error loading FXML file: " + e.getMessage());
            showAlert("Error loading FXML file" + e.getMessage());
        }
    }

    public void showAboutDialog() {
        try {
            // Load fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/About.fxml"));
            Parent root = loader.load();

            // Create new scene
            Scene scene = new Scene(root);

            // Create new stage
            Stage stage = new Stage();
            stage.setTitle("About");
            stage.setScene(scene);
            stage.setMinWidth(scene.getWidth());
            stage.setMinHeight(scene.getHeight());
            stage.setResizable(false);

            // Set icon of the window
            Image icon = new Image(getClass().getResourceAsStream("/images/icon64.png"));
            stage.getIcons().add(icon);

            // Show the stage
            stage.show();

        } catch (IOException e) {
            System.out.println("Error loading FXML file: " + e.getMessage());
        }

    }

    public void showAlert(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean showConfirmationDialog(String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();

        return alert.getResult().getText().equals("OK");
    }

}
