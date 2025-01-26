/*
 * Superclass for all view controllers. This class is used to store the appController and appModel
 * so that they can be accessed by each subclass.
 */

package hack.in.black.controllers;

import hack.in.black.models.AppModel;

public class ViewController {
    // These two fields are protected so that they can be accessed by subclasses
    protected AppController appController;
    protected AppModel appModel;

    // Is called after the standard FXML initiliazation
    // by the appController which means that the appModel
    // and appController fields are already set
    protected void lateInitialize() {
        // This is meant to be overridden by subclasses
    }

    // Getters and setters
    public AppModel getAppModel() {
        return appModel;
    }

    public void setAppModel(AppModel appModel) {
        this.appModel = appModel;
    }

    public AppController getAppController() {
        return appController;
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }
}
