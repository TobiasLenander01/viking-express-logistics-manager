/*
 * The main view controller class is responsible for all logic
 * related to the main view i.e. the navigation and menu bar.
 * It extends the ViewController class which provides it with 
 * access to the appController and appModel.
 * 
 * The scrollPaneContent control is used to display the content
 * of the different views.
 */

package hack.in.black.controllers;

import hack.in.black.utilities.TestDataGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

public class MainViewController extends ViewController {

    // -- FXML FIELDS -- //
    @FXML
    private ScrollPane scrollPaneContent;

    @FXML
    private Button buttonWarehouses;

    @FXML
    private Button buttonShipments;

    @FXML
    private Button buttonInspections;

    // -- GETTERS -- //
    public ScrollPane getScrollPaneContent() {
        return scrollPaneContent;
    }

    // -- INITIALIZATION -- //
    @FXML
    public void initialize() {
        System.out.println("Main View Controller initialized");
    }

    // -- EVENT HANDLERS -- //
    public void handleMenuItemWarehouseClickEvent(ActionEvent event) {
        appController.switchView("NewWarehouse");
    }

    public void handleMenuItemShipmentClickEvent(ActionEvent event) {
        if (appModel.getLogisticsManager().getWarehouses().isEmpty()) {
            appController.showAlert("No Warehouses to create a shipment on. Please create a warehouse first.");
            return;
        }

        appController.switchView("NewShipment");
    }

    public void handleMenuItemInspectionClickEvent(ActionEvent event) {
        if (appModel.getLogisticsManager().getAllShipments().isEmpty()) {
            appController.showAlert("No Shipments to create an inspection on. Please create a shipment first.");
            return;
        }

        appController.switchView("NewInspection");
    }

    public void handleMenuItemShipmentLogClickEvent(ActionEvent event) {
        if (appModel.getLogisticsManager().getAllShipments().isEmpty()) {
            appController.showAlert("No Shipments to log. Please create a shipment first.");
            return;
        }

        appController.switchView("NewShipmentLog");
    }

    public void handleMenuItemLoadTestDataClickEvent(ActionEvent event) {
        appModel.setLogisticsManager(TestDataGenerator.getTestLogisticsManager());
        appController.switchView("Warehouses");
    }

    public void handleMenuItemAboutClickEvent(ActionEvent event) {
        appController.showAboutDialog();
    }

    public void handleButtonWarehousesClickEvent(ActionEvent event) {
        appController.switchView("Warehouses");
    }

    public void handleButtonShipmentsClickEvent(ActionEvent event) {
        appController.switchView("Shipments");
    }

    public void handleButtonInspectionsClickEvent(ActionEvent event) {
        appController.switchView("Inspections");
    }

    // -- METHODS -- //
    public void updateGUI(String currentView) {
        // Update styling for currently selected button
        switch (currentView) {
            case "Warehouses" -> {
                buttonInspections.getStyleClass().remove("selected");
                buttonShipments.getStyleClass().remove("selected");
                if (!buttonWarehouses.getStyleClass().contains("selected"))
                    buttonWarehouses.getStyleClass().add("selected");
            }
            case "Shipments" -> {
                buttonWarehouses.getStyleClass().remove("selected");
                buttonInspections.getStyleClass().remove("selected");
                if (!buttonShipments.getStyleClass().contains("selected"))
                    buttonShipments.getStyleClass().add("selected");
            }
            case "Inspections" -> {
                buttonWarehouses.getStyleClass().remove("selected");
                buttonShipments.getStyleClass().remove("selected");
                if (!buttonInspections.getStyleClass().contains("selected"))
                    buttonInspections.getStyleClass().add("selected");
            }
            default -> {
                buttonWarehouses.getStyleClass().remove("selected");
                buttonShipments.getStyleClass().remove("selected");
                buttonInspections.getStyleClass().remove("selected");
            }
        }
    }
}