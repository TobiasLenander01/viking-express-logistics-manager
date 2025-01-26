/*
 * NewWarehouseController.java is the controller for the New Warehouse View.
 * It provides the functionality to create a new warehouse.
 */

package hack.in.black.controllers;

import java.util.List;
import java.util.stream.Collectors;

import hack.in.black.enums.Region;
import hack.in.black.models.LogisticsManager;
import hack.in.black.models.Warehouse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

public class NewWarehouseController extends ViewController {

    @FXML
    private TextField textFieldName;

    @FXML
    private TextField textFieldAddress;

    @FXML
    private Spinner<Integer> spinnerCapacity;

    @FXML
    private ComboBox<Region> comboBoxRegion;

    @Override
    public void lateInitialize() {
        // Initialize comboBoxRegion with all regions
        comboBoxRegion.getItems().addAll(Region.values());
        comboBoxRegion.getSelectionModel().select(0);

        // Initialize spinnerCapacity with default values
        spinnerCapacity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50000, 1000));
    }

    public void handleButtonAddNewWarehouseClickEvent(ActionEvent event) {
        if (checkInput() == false)
            return;

        addWarehouse();
    }

    public void handleButtonBackClickEvent(ActionEvent event) {
        boolean confirmation = appController.showConfirmationDialog("Are you sure you want to go back?");
        if (confirmation) {
            appController.switchView("Warehouses");
        }
    }

    // Method to add a new warehouse
    private void addWarehouse() {
        String name = textFieldName.getText();
        String address = textFieldAddress.getText();
        int capacity = spinnerCapacity.getValue();
        Region region = comboBoxRegion.getValue();

        Warehouse warehouse1 = new Warehouse(name, region, address, capacity);
        LogisticsManager logisticsManager = appModel.getLogisticsManager();
        logisticsManager.addWarehouse(warehouse1);

        System.out.println(warehouse1);

        appController.switchView("Warehouses");
    }

    // Method for verifying input
    private boolean checkInput() {
        List<String> warehouseNames = appModel.getLogisticsManager().getWarehouses().stream()
                .map(Warehouse::getName)
                .collect(Collectors.toList());

        if (warehouseNames.contains(textFieldName.getText())) {
            appController.showAlert("Warehouse with this name already exists");
            return false;
        }

        if (textFieldName.getText().isEmpty() || textFieldAddress.getText().isEmpty()) {
            appController.showAlert("Please fill in all fields");
            return false;
        }

        return true;
    }
}
