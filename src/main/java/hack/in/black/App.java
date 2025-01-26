/*
 * Entry point of the application. This class is responsible for creating the model and controller
 * objects and initializing the main window.
 */

package hack.in.black;

import hack.in.black.controllers.AppController;
import hack.in.black.models.AppModel;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Set the title of the window
        primaryStage.setTitle("Viking Express Logistics Manager");

        // Set icon of the window
        Image icon = new Image(getClass().getResourceAsStream("/images/icon64.png"));
        primaryStage.getIcons().add(icon);

        // Create the model and controller
        AppModel appModel = new AppModel();
        AppController appController = new AppController(primaryStage, appModel);

        // Show the main view
        appController.initializeMainWindow();
    }
}