/*
 * NewShipmentController.java is the controller for the New Shipment View.
 * This view allows the user to create a new shipment and add it to a warehouse.
 */

package hack.in.black.controllers;

import hack.in.black.models.Warehouse;
import hack.in.black.utilities.LogisticsException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class NewShipmentController extends ViewController {

    @FXML
    private Label labelRemainingCapacity;

    @FXML
    private Spinner<Integer> spinnerSize;

    @FXML
    private ComboBox<Warehouse> comboBoxWarehouse;

    // This code will run when the view is loaded
    @Override
    public void lateInitialize() {
        // Load warehouses into combo box
        comboBoxWarehouse.getItems().addAll(appModel.getLogisticsManager().getWarehouses());
        comboBoxWarehouse.getSelectionModel().select(0);

        // Update labelCapacity
        updateCapacityLabel();

        // Initialize spinnerCapacity with default values
        spinnerSize.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50000, 100));
    }

    private void updateCapacityLabel() {
        Warehouse selectedWarehouse = comboBoxWarehouse.getSelectionModel().getSelectedItem();
        if (selectedWarehouse == null) {
            return;
        }
        int remainingCapacity = selectedWarehouse.calculateRemainingCapacity();
        labelRemainingCapacity.setText(selectedWarehouse.getName() + " remaining capacity: " + remainingCapacity);
    }

    // This code will run when the confirm button is clicked
    public void handleButtonConfirmClickEvent(ActionEvent event) {
        // Add shipment
        addShipment();
    }

    public void handleComboBoxWarehouseSelection(ActionEvent event) {
        updateCapacityLabel();
    }

    public void handleButtonBackClickEvent(ActionEvent event) {
        boolean confirmation = appController.showConfirmationDialog("Are you sure you want to go back?");
        if (confirmation) {
            appController.switchView("Shipments");
        }
    }

    private void addShipment() {
        // Get shipment details
        int size = spinnerSize.getValue();
        Warehouse warehouse = comboBoxWarehouse.getSelectionModel().getSelectedItem();

        // Try to create and add the shipment to the selected warehouse
        try {
            appModel.getLogisticsManager().createShipment(warehouse, size);
        } catch (LogisticsException e) {
            appController.showAlert(e.getMessage());
            return;
        }

        // Switch view to shipments
        appController.switchView("Shipments");
    }
}