/*
 * WarehousesController.java is the controller for the Warehouses Overview.
 * It handles the display of warehouses in a table, region selection, and warehouse details.
 */

package hack.in.black.controllers;

import java.time.LocalDateTime;

import hack.in.black.enums.Region;
import hack.in.black.models.Warehouse;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;

public class WarehousesController extends ViewController {

    // --- FXML Fields --- //
    @FXML
    private TableView<Warehouse> tableViewWarehouses;

    @FXML
    private TableColumn<Warehouse, String> tableColumnID, tableColumnName, tableColumnAddress;

    @FXML
    private TableColumn<Warehouse, Region> tableColumnRegion;

    @FXML
    private ComboBox<Region> comboBoxRegion;

    @FXML
    private Label labelTotalCapacity, labelRemainingCapacityLabel,
            labelBusiestWarehousLabel, labelSelectedWarehouseTotalCapacity, labelSelectedWarehouseCurrentStockLevel,
            labelSelectedWarehouseRemainingCapacity, labelSelectedWarehouseThroughput,
            labelSelectedWarehouseShipments, labelSelectedWarehouseLastInspection;

    @FXML
    private VBox vboxWarehouseDetails;

    @FXML
    private ListView<String> listViewInspectors;

    // --- Fields --- //
    private ObservableList<Warehouse> allWarehouses;
    private FilteredList<Warehouse> filteredWarehouses;

    // --- Initialization --- //
    @Override
    public void lateInitialize() {
        // Configure table view, combo box, and listeners
        configureTableViewWarehouses();
        configureTableViewWarehousesListeners();
        configureComboBoxRegion();

        // Hide warehouse details by default
        vboxWarehouseDetails.setVisible(false);

        // Update overview details
        updateOverviewDetails();
    }

    private void configureTableViewWarehouses() {
        // Get all warehouses
        allWarehouses = appModel.getLogisticsManager().getWarehouses();

        // Create filtered warehouses list with a predicate for the selected region
        filteredWarehouses = new FilteredList<>(allWarehouses, warehouse -> true);

        // Configure table columns to display warehouse fields
        tableColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tableColumnRegion.setCellValueFactory(new PropertyValueFactory<>("region"));

        // Set table columns to be editable
        tableColumnName.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnAddress.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnRegion.setCellFactory(ComboBoxTableCell.forTableColumn(Region.values()));

        // Set items to the table view
        tableViewWarehouses.setItems(filteredWarehouses);
    }

    private void configureTableViewWarehousesListeners() {
        // Update selected warehouse details when a warehouse is selected
        tableViewWarehouses.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection == null) {
                        vboxWarehouseDetails.setVisible(false);
                    } else {
                        updateSelectedWarehouseDetails(newSelection);
                        vboxWarehouseDetails.setVisible(true);
                    }
                });

        // Update warehouse fields when they are edited
        tableColumnName.setOnEditCommit(event -> {
            Warehouse selectedWarehouse = event.getRowValue();
            selectedWarehouse.setName(event.getNewValue());
        });

        tableColumnAddress.setOnEditCommit(event -> {
            Warehouse selectedWarehouse = event.getRowValue();
            selectedWarehouse.setAddress(event.getNewValue());
        });

        tableColumnRegion.setOnEditCommit(event -> {
            Warehouse selectedWarehouse = event.getRowValue();
            selectedWarehouse.setRegion(event.getNewValue());
        });
    }

    private void configureComboBoxRegion() {
        // Add all regions from enum to the combo box
        comboBoxRegion.getItems().setAll(Region.values());
        comboBoxRegion.getSelectionModel().clearSelection();
    }

    // --- Event Handlers --- //
    public void handleButtonAddClickEvent(ActionEvent event) {
        // Switch to NewWarehouse view
        appController.switchView("NewWarehouse");
    }

    public void handleButtonDeleteClickEvent(ActionEvent event) {
        // Get selected warehouse and delete it
        Warehouse selectedWarehouse = tableViewWarehouses.getSelectionModel().getSelectedItem();

        // Show alert if no warehouse is selected
        if (selectedWarehouse == null) {
            appController.showAlert("Please select a warehouse to delete.");
            return;
        }

        // Ask user for confirmation
        if (!appController.showConfirmationDialog(
                "Are you sure you want to delete warehouse: " + selectedWarehouse.getName() + "?")) {
            return;
        }

        // Remove warehouse from logistics manager
        appModel.getLogisticsManager().removeWarehouse(selectedWarehouse);

        // Refresh table view
        handleFilterChangedEvent(event);
    }

    public void handleButtonClearFilterClickEvent(ActionEvent event) {
        // Clear region filter
        comboBoxRegion.getSelectionModel().clearSelection();
    }

    public void handleFilterChangedEvent(Event event) {
        // Get up to date list
        allWarehouses.setAll(appModel.getLogisticsManager().getWarehouses());

        // Refresh table view
        tableViewWarehouses.refresh();

        // Get selected region
        Region selectedRegion = comboBoxRegion.getSelectionModel().getSelectedItem();

        // Filter warehouses by selected region
        filteredWarehouses.setPredicate(warehouse -> {
            if (selectedRegion == null) {
                return true;
            }
            return warehouse.getRegion().equals(selectedRegion);
        });

        // Update overview details
        updateOverviewDetails();
    }

    private void updateOverviewDetails() {
        // Get overview data from logistics manager
        int totalCapacity = appModel.getLogisticsManager().getTotalCapacity(filteredWarehouses);
        Warehouse busiestWarehouse = appModel.getLogisticsManager().getBusiestWarehouse(filteredWarehouses);
        int remainingCapacity = appModel.getLogisticsManager().getRemainingCapacity(filteredWarehouses);
        Double percentage = appModel.getLogisticsManager().getRemainingCapacityPercentage(filteredWarehouses);

        // Update labels
        labelTotalCapacity.setText("Total Capacity: " + totalCapacity);
        labelRemainingCapacityLabel.setText("Total Remaining Capacity: " + remainingCapacity + " (" + String.format("%.0f%%", percentage) + ")");
        if (busiestWarehouse != null) {
            labelBusiestWarehousLabel.setText("Busiest Warehouse: " + busiestWarehouse.getName());
        }
    }

    private void updateSelectedWarehouseDetails(Warehouse selectedWarehouse) {
        // Update selected warehouse details
        int totalCapacity = selectedWarehouse.getCapacity();
        int currentStockLevel = selectedWarehouse.calculateCurrentStockLevel();
        int throughput = appModel.getLogisticsManager().calculateAverageThroughput(selectedWarehouse);
        int shipmentsCount = selectedWarehouse.getShipments().size();
        LocalDateTime lastInspectionDate = appModel.getLogisticsManager().getLastInspectionDate(selectedWarehouse);
        String percentage = String.format("%.0f%%", selectedWarehouse.calculateRemainingCapacityPercentage());

        // Update labels and list view
        labelSelectedWarehouseTotalCapacity.setText("Total Capacity: " + totalCapacity);
        labelSelectedWarehouseCurrentStockLevel.setText("Current Stock Level: " + currentStockLevel);
        labelSelectedWarehouseRemainingCapacity.setText("Remaining Capacity: " + percentage);
        labelSelectedWarehouseThroughput.setText("Average Throughput: " + throughput + " days");
        labelSelectedWarehouseShipments.setText("Shipments: " + shipmentsCount);
        if (lastInspectionDate.equals(LocalDateTime.MIN)) {
            labelSelectedWarehouseLastInspection.setText("Last Inspection: NaN");
        } else {
            labelSelectedWarehouseLastInspection.setText("Last Inspection: " + lastInspectionDate.toLocalDate());
        }

        // Get inspectors for the selected warehouse and filter out duplicates
        String[] inspectors = selectedWarehouse.getInspectors().stream().distinct().toArray(String[]::new);
        listViewInspectors.getItems().setAll(inspectors);
    }
}