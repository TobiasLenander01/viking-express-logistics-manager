/*
 * ShipmentsController.java is a controller class that manages the Shipments Overview.
 * It handles the display of shipments and shipment logs using a table, list, and filters.
 */

package hack.in.black.controllers;

import hack.in.black.enums.ShipmentStatus;
import hack.in.black.models.Shipment;
import hack.in.black.models.ShipmentLog;
import hack.in.black.models.Warehouse;
import hack.in.black.utilities.EditableShipmentLogCell;
import hack.in.black.utilities.LogisticsException;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

public class ShipmentsController extends ViewController {
    // --- FXML Fields --- //
    @FXML
    private TableView<Shipment> tableViewShipments;

    @FXML
    private ComboBox<Warehouse> comboBoxWarehouse;

    @FXML
    private TableColumn<Shipment, String> tableColumnId, tableColumnWarehouse, tableColumnNeedsAttention;

    @FXML
    private TableColumn<Shipment, ShipmentStatus> tableColumnStatus;

    @FXML
    private TableColumn<Shipment, Integer> tableColumnSize, tableColumnNumberOfWarehouses;

    @FXML
    private Label labelShipmentLogs;

    @FXML
    private ListView<ShipmentLog> listViewLogs;

    // --- Fields --- //
    private ObservableList<Shipment> allShipments;
    private ObservableList<ShipmentLog> allLogs;
    private FilteredList<Shipment> filteredShipments;
    private FilteredList<ShipmentLog> filteredLogs;

    // --- Initialization --- //
    @Override
    public void lateInitialize() {
        configureTableViewShipments();
        configureComboBoxWarehouse();
        configureListViewLogs();
    }

    private void configureTableViewShipments() {
        // Get all shipments
        allShipments = appModel.getLogisticsManager().getAllShipments();

        // Create a filtered list with a predicate for the selected warehouse
        filteredShipments = new FilteredList<>(allShipments, predicate -> true);

        // Initialize the table view columns
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableColumnSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        tableColumnWarehouse.setCellValueFactory(new PropertyValueFactory<>("warehouse"));
        tableColumnNumberOfWarehouses.setCellValueFactory(new PropertyValueFactory<>("numberOfWarehouses"));
        tableColumnNeedsAttention.setCellValueFactory(new PropertyValueFactory<>("needsAttention"));

        // Enable editing for the size column
        tableColumnSize.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        tableColumnSize.setOnEditCommit(event -> {

            // Get the shipment
            Shipment shipment = event.getRowValue();

            // Get the new size value
            int newSize = event.getNewValue();

            // Validate the new size
            if (newSize < 0 || newSize > shipment.getWarehouse().getCapacity()) {
                appController.showAlert("Please enter a valid size");
                return;
            }

            // Update the shipment size in the model
            shipment.setSize(newSize);
        });

        // Set the table view items
        tableViewShipments.setItems(filteredShipments);
    }

    private void configureComboBoxWarehouse() {
        // Get all warehouses and set them as items in the combo box
        ObservableList<Warehouse> warehouses = appModel.getLogisticsManager().getWarehouses();
        comboBoxWarehouse.setItems(warehouses);
    }

    private void configureListViewLogs() {
        // Set custom cell factory
        listViewLogs.setCellFactory(param -> new EditableShipmentLogCell(appModel.getLogisticsManager()));

        // Get all logs and set them as items in the list view
        allLogs = appModel.getLogisticsManager().getAllShipmentLogs();

        // Create a filtered list with a predicate for the selected shipment
        filteredLogs = new FilteredList<>(allLogs, predicate -> true);

        // Create a sorted list with the filtered logs
        SortedList<ShipmentLog> sortedLogs = new SortedList<>(filteredLogs);
        sortedLogs.setComparator((log1, log2) -> log1.getDateTime().compareTo(log2.getDateTime()));

        // Set the list view items
        listViewLogs.setItems(sortedLogs);

    }

    // --- Event Handlers --- //
    // This method is called when the "New Shipment" button is clicked
    public void handleButtonNewClickEvent(ActionEvent event) {
        appController.switchView("NewShipment");
    }

    // This method is called when the "Delete Shipment" button is clicked
    public void handleButtonDeleteClickEvent(ActionEvent event) {
        // Get selected shipment
        Shipment selectedShipment = tableViewShipments.getSelectionModel().getSelectedItem();

        // Check if a shipment is selected
        if (selectedShipment == null) {
            appController.showAlert("Please select a shipment to delete.");
            return;
        }

        // Ask user for confirmation
        if (!appController.showConfirmationDialog(
                "Are you sure you want to delete shipment: " + selectedShipment.getId() + "?")) {
            return;
        }

        // Delete selected shipment from logisticsmanager warehouse
        appModel.getLogisticsManager().removeShipment(selectedShipment);

        // Update the filter and table view
        tableViewShipments.getSelectionModel().clearSelection();
        handleFilterChangedEvent(event);
    }

    // This method is called when the "Send Shipment" button is clicked
    public void handleButtonSendClickEvent(ActionEvent event) {
        // Get all warehouses
        ObservableList<Warehouse> warehouses = appModel.getLogisticsManager().getWarehouses();

        // Check if there are any warehouses
        if (warehouses.isEmpty()) {
            appController.showAlert("There are no warehouses to send shipments to.");
            return;
        }

        // Get selected shipment
        Shipment selectedShipment = tableViewShipments.getSelectionModel().getSelectedItem();

        // Check if a shipment is selected
        if (selectedShipment == null) {
            appController.showAlert("Please select a shipment to send.");
            return;
        }

        // Show dialog to select warehouse
        Warehouse selectedWarehouse = showSendShipmentDialog(selectedShipment, warehouses);

        // Check if a warehouse was selected
        if (selectedWarehouse == null) {
            return;
        }

        // Try to send selected shipment to selected warehouse
        try {
            appModel.getLogisticsManager().sendShipment(selectedShipment, selectedWarehouse);
        } catch (LogisticsException e) {
            appController.showAlert(e.getMessage());
        }

        // Update the table view
        handleFilterChangedEvent(event);
    }

    public void handleFilterChangedEvent(Event event) {
        // Get up to date lists
        allShipments.setAll(appModel.getLogisticsManager().getAllShipments());
        allLogs.setAll(appModel.getLogisticsManager().getAllShipmentLogs());

        // Refresh table views
        tableViewShipments.refresh();
        listViewLogs.refresh();

        // Get selected warehouse and shipment
        Warehouse selectedWarehouse = comboBoxWarehouse.getSelectionModel().getSelectedItem();
        Shipment selectedShipment = tableViewShipments.getSelectionModel().getSelectedItem();

        // Filter the SHIPMENTS based on the selected warehouse
        if (selectedWarehouse != null) {
            // If there is a selected warehouse, filter the shipments based on it
            filteredShipments.setPredicate(shipment -> {
                return shipment.getWarehouse().equals(selectedWarehouse);
            });
        } else {
            // If there is no selected warehouse, show all shipments
            filteredShipments.setPredicate(null);
        }

        // Filter the LOGS based on the selected shipment and warehouse
        if (selectedShipment != null && selectedWarehouse != null) {
            // If there is a selected shipment and warehouse, filter the logs based on them
            filteredLogs.setPredicate(log -> {
                return log.getShipment().equals(selectedShipment) && log.getWarehouse().equals(selectedWarehouse);
            });

            // Update the label to show the selected shipment's logs
            labelShipmentLogs.setText(
                    "Logs for shipment: " + selectedShipment.getId() + " at warehouse: " + selectedWarehouse.getName());

        } else if (selectedShipment != null && selectedWarehouse == null) {
            // If there is a selected shipment but no selected warehouse, filter the logs
            // based on the shipment
            filteredLogs.setPredicate(log -> {
                return log.getShipment().equals(selectedShipment);
            });

            // Update the label to show the selected shipment's logs
            labelShipmentLogs.setText("Logs for shipment: " + selectedShipment.getId());

        } else if (selectedWarehouse != null && selectedShipment == null) {
            // If there is a selected warehouse but no selected shipment, filter the logs
            // based on the warehouse
            filteredLogs.setPredicate(log -> {
                return log.getWarehouse().equals(selectedWarehouse);
            });

            // Update the label to show the selected warehouse's logs
            labelShipmentLogs.setText("Logs for warehouse: " + selectedWarehouse.getName());

        } else {
            // If there is no selected shipment, show all logs
            filteredLogs.setPredicate(null);

            // Update the label to show all logs
            labelShipmentLogs.setText("All Shipment Logs");
        }
    }

    public void handleButtonDeleteShipmentLogClickEvent(ActionEvent event) {
        ShipmentLog selectedLog = listViewLogs.getSelectionModel().getSelectedItem();

        // Check if a shipment log is selected
        if (selectedLog == null) {
            appController.showAlert("Please select a shipment log to delete.");
            return;
        }

        // Check if the shipment is currently being edited
        boolean isEditing = listViewLogs.getEditingIndex() != -1;
        if (isEditing) {
            appController.showAlert("Can't delete shipment log while editing. Please finish editing first.");
            return;
        }

        // Ask user for confirmation
        if (!appController.showConfirmationDialog(
                "Are you sure you want to delete shipment log: " + selectedLog.getId() + "?")) {
            return;
        }

        // Delete selected shipment log from shipment
        selectedLog.getShipment().removeShipmentLog(selectedLog);

        // Update the filter and table view
        listViewLogs.getSelectionModel().clearSelection();
        handleFilterChangedEvent(event);
    }

    public void handleButtonAddShipmentLogClickEvent(ActionEvent event) {
        appController.switchView("NewShipmentLog");
    }

    public void handleButtonClearFilterClickEvent(ActionEvent event) {
        comboBoxWarehouse.getSelectionModel().clearSelection();
        tableViewShipments.getSelectionModel().clearSelection();
        handleFilterChangedEvent(event);
    }

    // --- Helper Methods --- //
    // This method shows a dialog to select a warehouse to send a shipment to
    private Warehouse showSendShipmentDialog(Shipment shipment, ObservableList<Warehouse> warehouses) {
        if (warehouses.isEmpty() || shipment == null) {
            return null;
        }

        Warehouse[] choices = new Warehouse[warehouses.size()];
        warehouses.toArray(choices);
        ChoiceDialog<Warehouse> dialog = new ChoiceDialog<>(choices[0], choices);
        dialog.setTitle("Where to send shipment: " + shipment.getId() + "?");
        dialog.setHeaderText(null);
        dialog.setContentText("Please select a warehouse");

        dialog.showAndWait();
        return dialog.getSelectedItem();
    }

}
