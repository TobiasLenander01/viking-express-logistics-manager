/*
 * InspectionsController.java is the controller for the Inspections Overview.
 * It handles the display of Inspection Logs in a table with the ability to filter
 */

package hack.in.black.controllers;

import java.time.format.DateTimeFormatter;

import hack.in.black.enums.InspectionResult;
import hack.in.black.models.InspectionLog;
import hack.in.black.models.Shipment;
import hack.in.black.models.Warehouse;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class InspectionsController extends ViewController {
    // --- FXML Fields --- //
    @FXML
    private TableView<InspectionLog> tableViewInspections;

    @FXML
    private TableColumn<InspectionLog, String> tableColumnId,
            tableColumnInspector, tableColumnWarehouse, tableColumnInspectionDate;

    @FXML
    private TableColumn<InspectionLog, InspectionResult> tableColumnResult;

    @FXML
    private TableColumn<InspectionLog, Shipment> tableColumnShipment;

    @FXML
    private ComboBox<Warehouse> comboBoxWarehouse;

    @FXML
    private ComboBox<Shipment> comboBoxShipment;

    // --- Fields --- //
    private ObservableList<InspectionLog> allInspections;
    private FilteredList<InspectionLog> filteredInspections;
    private FilteredList<Shipment> filteredShipments;

    // --- Initialization --- //
    @Override
    public void lateInitialize() {
        configureTableViewInspections();
        configureTableViewInspectionsListeners();
        configureComboboxWarehouse();
        configureComboboxShipment();
    }

    // This method configures the table view for inspections
    // and is run through the lateInitialize method
    private void configureTableViewInspections() {

        // Get all inspections from logistics manager
        allInspections = appModel.getLogisticsManager().getAllInspectionLogs();

        // Create a filtered list with a predicate for the selected warehouse and
        // shipment
        filteredInspections = new FilteredList<>(allInspections, preciate -> true);

        // Assign cell value factories
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnResult.setCellValueFactory(new PropertyValueFactory<>("result"));
        tableColumnInspector.setCellValueFactory(new PropertyValueFactory<>("inspector"));
        tableColumnWarehouse.setCellValueFactory(new PropertyValueFactory<>("warehouse"));
        tableColumnShipment.setCellValueFactory(new PropertyValueFactory<>("shipment"));

        // Assign custom cell value factories
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        tableColumnInspectionDate
                .setCellValueFactory((CellDataFeatures<InspectionLog, String> param) -> new SimpleStringProperty(
                        param.getValue().getInspectionDate().format(formatter)));

        // Assign editable cell factories
        tableColumnResult.setCellFactory(ComboBoxTableCell.forTableColumn(InspectionResult.values()));
        tableColumnInspector.setCellFactory(TextFieldTableCell.forTableColumn());

        // Set the items of the table view to the filtered inspections
        tableViewInspections.setItems(filteredInspections);
    }

    private void configureTableViewInspectionsListeners() {
        tableColumnResult.setOnEditCommit(event -> {
            InspectionLog selectedInspection = event.getRowValue();
            selectedInspection.setResult(event.getNewValue());
        });

        tableColumnInspector.setOnEditCommit(event -> {
            InspectionLog selectedInspection = event.getRowValue();
            selectedInspection.setInspector(event.getNewValue());
        });
    }

    // This method configures the combobox for warehouses
    // and is run through the lateInitialize method
    private void configureComboboxWarehouse() {
        // Get all warehouses from logistics manager
        ObservableList<Warehouse> allWarehouses = appModel.getLogisticsManager().getWarehouses();

        // Add all warehouses to the combobox
        comboBoxWarehouse.setItems(allWarehouses);
    }

    // This method configures the combobox for shipments
    // and is run through the lateInitialize method
    private void configureComboboxShipment() {
        // Get all shipments from logistics manager
        ObservableList<Shipment> allShipments = appModel.getLogisticsManager().getAllShipments();

        // Filter shipments based on selected warehouse
        filteredShipments = new FilteredList<>(allShipments, predicate -> true);

        // Add a listener to the warehouse combobox
        comboBoxWarehouse.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Filter shipments based on selected warehouse
            if (newValue != null) {
                filteredShipments.setPredicate(shipment -> {
                    return shipment.getWarehouse().equals(newValue);
                });
                comboBoxShipment.setItems(filteredShipments);
            } else {
                filteredShipments.setPredicate(null);
                comboBoxShipment.setItems(allShipments);
            }

            comboBoxShipment.getSelectionModel().clearSelection();
            handleFilterChangedEvent(null);
        });

        // Add all shipments to the combobox
        comboBoxShipment.setItems(filteredShipments);
    }

    // --- Event Handlers --- //
    // This method is called when the "Add Inspection" button is clicked
    public void handleButtonAddClickEvent(ActionEvent event) {
        if (appModel.getLogisticsManager().getAllShipments().isEmpty()) {
            appController.showAlert("No Shipments to create an inspection on. Please create a shipment first.");
            return;
        }

        appController.switchView("NewInspection");
    }

    // This method is called when the "Delete Inspection" button is clicked
    public void handleButtonDeleteClickEvent(ActionEvent event) {
        // Get selected inspection
        InspectionLog selectedInspection = tableViewInspections.getSelectionModel().getSelectedItem();

        // Check if a inspection is selected
        if (selectedInspection == null) {
            appController.showAlert("Please select a inspection to delete.");
            return;
        }

        // Ask user for confirmation
        if (!appController.showConfirmationDialog(
                "Are you sure you want to delete inspection: " + selectedInspection.getId() + "?")) {
            return;
        }

        // Delete the inspection
        appModel.getLogisticsManager().removeInspectionLog(selectedInspection);

        // Update the filter and table view
        tableViewInspections.getSelectionModel().clearSelection();
        handleFilterChangedEvent(event);
    }

    // This method is called when either of the comboboxes filter is changed
    public void handleFilterChangedEvent(Event event) {
        // Get up to date lists of inspections
        allInspections.setAll(appModel.getLogisticsManager().getAllInspectionLogs());

        // Refresh table views
        tableViewInspections.refresh();

        // Get selected warehouse
        Warehouse selectedWarehouse = comboBoxWarehouse.getSelectionModel().getSelectedItem();
        Shipment selectedShipment = comboBoxShipment.getSelectionModel().getSelectedItem();

        // Filter inspections based on selected warehouse and shipment
        if (selectedWarehouse != null && selectedShipment != null) {
            filteredInspections.setPredicate(inspection -> {
                return inspection.getWarehouse().equals(selectedWarehouse)
                        && inspection.getShipment().equals(selectedShipment);
            });
        } else if (selectedWarehouse != null) {
            filteredInspections.setPredicate(inspection -> {
                return inspection.getWarehouse().equals(selectedWarehouse);
            });
        } else if (selectedShipment != null) {
            filteredInspections.setPredicate(inspection -> {
                return inspection.getShipment().equals(selectedShipment);
            });
        } else {
            filteredInspections.setPredicate(null);
        }
    }

    // This method is called when the "Clear Filter" button is clicked
    public void handleButtonClearFilterClickEvent(ActionEvent event) {
        comboBoxWarehouse.getSelectionModel().clearSelection();
        comboBoxShipment.getSelectionModel().clearSelection();
        handleFilterChangedEvent(event);
    }
}