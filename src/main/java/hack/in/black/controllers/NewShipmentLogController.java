/*
 * NewShipmentLogController.java is the controller for the New Shipment Log View.
 * It provides the functionality to create a new shipment log.
 */

package hack.in.black.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import hack.in.black.enums.ShipmentStatus;
import hack.in.black.models.Shipment;
import hack.in.black.models.Warehouse;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;

public class NewShipmentLogController extends ViewController {

    @FXML
    private ComboBox<Warehouse> warehouseComboBox;

    @FXML
    private ComboBox<Shipment> shipmentComboBox;

    @FXML
    private ComboBox<ShipmentStatus> statusComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Spinner<Integer> spinnerHour, spinnerMinute;

    @Override
    public void lateInitialize() {
        // Populate the warehouse combobox with all warehouses
        ObservableList<Warehouse> warehouses = appModel.getLogisticsManager().getWarehouses();
        warehouseComboBox.getItems().addAll(warehouses);
        warehouseComboBox.getSelectionModel().select(0);

        // Populate the status combobox with all shipment statuses
        statusComboBox.getItems().addAll(ShipmentStatus.values());
        statusComboBox.getSelectionModel().select(0);

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

        updateShipmentComoBox();
    }

    public void handleWarehouseComboBoxSelection(ActionEvent event) {
        updateShipmentComoBox();
    }

    public void handleBackButtonClickEvent(ActionEvent event) {
        boolean confirmation = appController.showConfirmationDialog("Are you sure you want to go back?");
        if (confirmation) {
            appController.switchView("Shipments");
        }
    }

    public void handleConfirmButtonClickEvent(ActionEvent event) {
        createShipmentLog();
    }

    public void createShipmentLog() {
        // Get selected warehouse, shipment and status
        Warehouse warehouse = warehouseComboBox.getSelectionModel().getSelectedItem();
        Shipment shipment = shipmentComboBox.getSelectionModel().getSelectedItem();
        ShipmentStatus status = statusComboBox.getSelectionModel().getSelectedItem();

        // Get the inspection date and time
        LocalDate date = datePicker.getValue();
        LocalTime time = LocalTime.of(spinnerHour.getValue(), spinnerMinute.getValue());
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        if (warehouse == null) {
            appController.showAlert("Please select a warehouse");
            return;
        }

        if (shipment == null) {
            appController.showAlert("Please select a shipment");
            return;
        }

        if (status == null) {
            appController.showAlert("Please select a status");
            return;
        }

        // Create shipment log
        appModel.getLogisticsManager().createShipmentLog(shipment, status, dateTime);

        // Change view to shipments
        appController.switchView("Shipments");
    }

    public void updateShipmentComoBox() {
        Warehouse warehouse = warehouseComboBox.getSelectionModel().getSelectedItem();
        shipmentComboBox.getItems().clear();
        shipmentComboBox.getItems().addAll(warehouse.getShipments());
        shipmentComboBox.getSelectionModel().select(0);
    }

}
