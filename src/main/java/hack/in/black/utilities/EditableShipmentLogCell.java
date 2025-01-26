/*
 * This class is used to create a custom ListCell for the ListView of shipment logs.
 * It allows the user to edit the shipment log directly in the ListView.
 * The user can change the date, time, shipment, status, and warehouse of the shipment log.
 * 
 * This class is used in the ShipmentsController class.
 */

package hack.in.black.utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import hack.in.black.enums.ShipmentStatus;
import hack.in.black.models.LogisticsManager;
import hack.in.black.models.Shipment;
import hack.in.black.models.ShipmentLog;
import hack.in.black.models.Warehouse;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

public class EditableShipmentLogCell extends ListCell<ShipmentLog> {
    // Instance variables for the LogisticsManager and the original shipment of the shipment log
    // and the HBox that contains the controls for editing the shipment log
    private final LogisticsManager logisticsManager;
    private Shipment originalShipment;
    private final HBox hBox = new HBox();
    private final DatePicker datePicker = new DatePicker();
    private final Spinner<Integer> spinnerHour = new Spinner<>(0, 23, 0);
    private final Spinner<Integer> spinnerMinute = new Spinner<>(0, 59, 0);
    private final ComboBox<ShipmentStatus> comboBoxStatus = new ComboBox<>();
    private final ComboBox<Shipment> comboBoxShipment = new ComboBox<>();
    private final ComboBox<Warehouse> comboBoxWarehouse = new ComboBox<>();

    // Constructor for getting the LogisticsManager and the original shipment of the shipment log
    public EditableShipmentLogCell(LogisticsManager logisticsManager) {
        this.logisticsManager = logisticsManager;
        if (getItem() != null)
            this.originalShipment = getItem().getShipment();
    }

    // Initialize the ListCell with the controls for editing the shipment log
    private HBox getInitializedListCell() {
        // Get shipment to edit
        ShipmentLog shipmentLog = getItem();

        // Datepicker
        datePicker.setValue(shipmentLog.getDateTime().toLocalDate());
        datePicker.setPrefWidth(100);

        // Time spinners
        configureTimeSpinnersValueFactories();
        spinnerHour.setEditable(true);
        spinnerHour.setPrefWidth(60);
        spinnerMinute.setEditable(true);
        spinnerMinute.setPrefWidth(60);

        // Label
        Label label1 = new Label();
        label1.setText(" - Shipment with ID ");

        // Combobox for shipment
        comboBoxShipment.getItems().addAll(logisticsManager.getAllShipments());
        comboBoxShipment.getSelectionModel().select(shipmentLog.getShipment());
        comboBoxShipment.setPrefWidth(120);

        // Label
        Label label2 = new Label();
        label2.setText(" was ");

        // Combobox for status
        comboBoxStatus.getItems().addAll(ShipmentStatus.values());
        comboBoxStatus.getSelectionModel().select(shipmentLog.getStatus());
        comboBoxStatus.setPrefWidth(110);

        // Label
        Label label3 = new Label();
        label3.setText(" at ");

        // Combobox for warehouse
        comboBoxWarehouse.getItems().addAll(logisticsManager.getWarehouses());
        comboBoxWarehouse.getSelectionModel().select(shipmentLog.getWarehouse());
        comboBoxWarehouse.setPrefWidth(150);

        // Button to save changes
        Button buttonConfirm = new Button("Confirm");
        buttonConfirm.setOnAction(e -> commitEdit(getEditedShipmentLog()));

        // Add all of the above to the HBox in the correct order
        hBox.getChildren().clear();
        hBox.getChildren().addAll(datePicker, spinnerHour, spinnerMinute, label1, comboBoxShipment, label2,
                comboBoxStatus, label3, comboBoxWarehouse, buttonConfirm);
        hBox.setSpacing(10);

        return hBox;
    }

    @Override
    public void startEdit() {
        // Initialize ListCell
        super.startEdit();
        if (getItem() != null) {
            setText(null); // Clear the text
            setGraphic(getInitializedListCell());
        }
    }

    @Override
    public void cancelEdit() {
        // Cancel editing
        super.cancelEdit();
        if (getItem() != null)
            setText(getItem().toString());
        else {
            setText(null);
        }
        setGraphic(null);
    }

    @Override
    public void commitEdit(ShipmentLog newValue) {
        // Commit changes
        if (originalShipment != null)
            logisticsManager.moveShipmentLog(newValue, originalShipment, newValue.getShipment());
        cancelEdit();
    }

    @Override
    protected void updateItem(ShipmentLog shipmentLog, boolean empty) {
        super.updateItem(shipmentLog, empty);
        if (empty || shipmentLog == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                setGraphic(getInitializedListCell());
            } else {
                setText(shipmentLog.toString());
                setGraphic(null);
            }
        }
    }

    // Get the edited shipment log
    private ShipmentLog getEditedShipmentLog() {
        ShipmentLog shipmentLog = getItem();
        if (shipmentLog != null) {
            // Get values from the controls
            LocalDate date = datePicker.getValue();
            LocalTime time = LocalTime.of(spinnerHour.getValue(), spinnerMinute.getValue());
            LocalDateTime dateTime = LocalDateTime.of(date, time);
            Shipment shipment = comboBoxShipment.getValue();
            ShipmentStatus status = comboBoxStatus.getValue();
            Warehouse warehouse = comboBoxWarehouse.getValue();

            // Set the values to the shipment log
            shipmentLog.setDateTime(dateTime);
            shipmentLog.setShipment(shipment);
            shipmentLog.setStatus(status);
            shipmentLog.setWarehouse(warehouse);
        }
        return shipmentLog;
    }

    // Configures the factories for the time spinners so that
    // they display the correct values and show the correct format for time
    // (e.g. 01 instead of 1)
    private void configureTimeSpinnersValueFactories() {
        if (getItem() == null)
            return;

        SpinnerValueFactory<Integer> hourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23,
                getItem().getDateTime().getHour());
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
                getItem().getDateTime().getMinute());
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
    }
}
