/*
 * NewInspectionController.java is the controller for the New Inspection View.
 * It allows the user to create a new inspection log for a shipment.
 */

package hack.in.black.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import hack.in.black.enums.InspectionResult;
import hack.in.black.models.InspectionLog;
import hack.in.black.models.Shipment;
import hack.in.black.models.Warehouse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class NewInspectionController extends ViewController {
    @FXML
    private ComboBox<Warehouse> comboBoxWarehouse;

    @FXML
    private ComboBox<Shipment> comboBoxShipment;

    @FXML
    private ComboBox<InspectionResult> comboBoxResult;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Spinner<Integer> spinnerHour, spinnerMinute;

    @FXML
    private TextField textFieldInspectorName;

    // -- INITIALIZATION -- //
    @Override
    public void lateInitialize() {
        // Populate the warehouse combobox with all warehouses
        comboBoxWarehouse.getItems().addAll(appModel.getLogisticsManager().getWarehouses());
        comboBoxWarehouse.getSelectionModel().select(0);

        // Populate the result combobox with all inspection results
        comboBoxResult.getItems().addAll(InspectionResult.values());
        comboBoxResult.getSelectionModel().select(0);

        // Set the date picker and spinner value factories with the current time
        datePicker.setValue(LocalDateTime.now().toLocalDate());
        SpinnerValueFactory<Integer> hourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23,
                LocalDateTime.now().getHour());
        hourValueFactory.setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                return String.format("%02d", value);
            }

            @Override
            public Integer fromString(String string) {
                return Integer.valueOf(string);
            }
        });
        spinnerHour.setValueFactory(hourValueFactory);

        SpinnerValueFactory<Integer> minuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59,
                LocalDateTime.now().getMinute());
        minuteValueFactory.setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                return String.format("%02d", value);
            }

            @Override
            public Integer fromString(String string) {
                return Integer.valueOf(string);
            }
        });
        spinnerMinute.setValueFactory(minuteValueFactory);

        // Update the shipment combobox
        updateShipmentComoBox();
    }

    public void handleComboBoxWarehouseSelection(ActionEvent event) {
        updateShipmentComoBox();
    }

    private void updateShipmentComoBox() {
        Warehouse warehouse = comboBoxWarehouse.getSelectionModel().getSelectedItem();
        comboBoxShipment.getItems().clear();
        comboBoxShipment.getItems().addAll(warehouse.getShipments());
        comboBoxShipment.getSelectionModel().select(0);
    }

    public void handleButtonConfirmClickEvent(ActionEvent event) {
        if (inputOk()) {
            createInspection();
            appController.switchView("Inspections");
        }
    }

    public void handleButtonBackClickEvent(ActionEvent event) {
        boolean confirmation = appController.showConfirmationDialog("Are you sure you want to go back?");
        if (confirmation)
            appController.switchView("Inspections");
    }

    private boolean inputOk() {
        // Check if a shipment is selected
        if (comboBoxShipment.getSelectionModel().getSelectedItem() == null) {
            appController.showAlert("Please select a shipment.");
            return false;
        }

        // Check if a result is selected
        if (comboBoxResult.getSelectionModel().getSelectedItem() == null) {
            appController.showAlert("Please select an inspection result.");
            return false;
        }

        // Check if the inspector name is empty
        if (textFieldInspectorName.getText().isEmpty()) {
            appController.showAlert("Please enter the inspector's name.");
            return false;
        }

        return true;
    }

    private void createInspection() {
        // Get the selected shipment
        Shipment selectedShipment = comboBoxShipment.getSelectionModel().getSelectedItem();

        // Get the selected inspection result
        InspectionResult selectedResult = comboBoxResult.getSelectionModel().getSelectedItem();

        // Get the inspector name
        String inspectorName = textFieldInspectorName.getText();

        // Get the inspection date and time
        LocalDate date = datePicker.getValue();
        LocalTime time = LocalTime.of(spinnerHour.getValue(), spinnerMinute.getValue());
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        // Create the inspection
        InspectionLog log = appModel.getLogisticsManager().inspectShipment(selectedShipment, selectedResult,
                inspectorName, dateTime);
        System.out.println("Inspection created: " + log.toStringForTest());
    }

}
