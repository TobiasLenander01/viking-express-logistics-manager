/*
 * LogisticsManager handles all the warehouses in the system.
 * The warehouses contain shipments and the shipments contain logs.
 */

package hack.in.black.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

import hack.in.black.enums.InspectionResult;
import hack.in.black.enums.ShipmentStatus;
import hack.in.black.utilities.LogisticsException;
import hack.in.black.utilities.UniqueID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LogisticsManager {
    private ObservableList<Warehouse> warehouses; // All warehouses in the system

    // Contructors
    public LogisticsManager() {
        this.warehouses = FXCollections.observableArrayList();
    }

    // For loading testdata
    public LogisticsManager(ObservableList<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    // Setter
    public void setWarehouses(ObservableList<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    // --- Warehouse Methods --- //

    public void addWarehouse(Warehouse warehouse) {
        // Add a warehouse to the list of warehouses
        warehouses.add(warehouse);
    }

    public void removeWarehouse(Warehouse warehouse) {
        // Remove a warehouse from the list of warehouses
        warehouses.remove(warehouse);
    }

    public Warehouse getWarehouseById(UniqueID id) {
        // Get a warehouse by its unique id
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getId().equals(id)) {
                return warehouse;
            }
        }
        return null;
    }

    public ObservableList<Warehouse> getWarehouses(ObservableList<Warehouse> warehouses) {
        // Get all warehouses in the list
        ObservableList<Warehouse> allWarehouses = FXCollections.observableArrayList();
        for (Warehouse warehouse : warehouses) {
            allWarehouses.add(warehouse);
        }
        return allWarehouses;
    }

    public ObservableList<Warehouse> getWarehouses() {
        // Backwards compatibility
        return getWarehouses(this.warehouses);
    }

    public int getTotalCapacity(ObservableList<Warehouse> warehouses) {
        // Get the total capacity of all warehouses in the list
        int totalCapacity = 0;
        for (Warehouse warehouse : warehouses) {
            totalCapacity += warehouse.getCapacity();
        }
        return totalCapacity;
    }

    public int getTotalCapacity() {
        // Backwards compatibility
        return getTotalCapacity(this.warehouses);
    }

    public int getRemainingCapacity(ObservableList<Warehouse> warehouses) {
        // Get the total remaining capacity of all warehouses in the list
        int remainingCapacity = 0;
        for (Warehouse warehouse : warehouses) {
            remainingCapacity += warehouse.calculateRemainingCapacity();
        }
        return remainingCapacity;
    }

    public double getRemainingCapacityPercentage(ObservableList<Warehouse> warehouses) {
        // Calculate remaining capacity percentage
        int remainingCapacity = getRemainingCapacity(warehouses);
        int totalCapacity = getTotalCapacity(warehouses);
        double percentage = (double) remainingCapacity / totalCapacity * 100;
        return percentage;
    }

    public double calculateRemainingCapacityPercentage() {
        // Backwards compatibility
        return getRemainingCapacityPercentage(this.warehouses);
    }

    public int getRemainingCapacity() {
        // Backwards compatibility
        return getRemainingCapacity(this.warehouses);
    }

    // It should calculate the average time that shipments spend at the warehouse.
    public int calculateAverageThroughput(Warehouse warehouse) {
        // Find out all shipments that have been at the warehouse
        ArrayList<Shipment> shipments = new ArrayList<>();
        for (ShipmentLog log : getAllShipmentLogs()) {
            if (log.getWarehouse() != null && log.getWarehouse().equals(warehouse) && !shipments.contains(log.getShipment())) {
                shipments.add(log.getShipment());
            }
        }

        // If no shipments have been at the warehouse, return 0
        if (shipments.isEmpty()) {
            return 0;
        }

        // Calculate the total days that shipments have been at the warehouse
        int totalDays = 0;
        for (Shipment shipment : shipments) {
            totalDays += warehouse.calculateDaysInWarehouseForShipment(shipment);
        }

        // Return the average time in days
        return totalDays / shipments.size();
    }

    public Warehouse getBusiestWarehouse(ObservableList<Warehouse> warehouses) {
        // Get the busiest warehouse in the list
        Warehouse busiestWarehouse = null;
        int maxShipments = 0;
        for (Warehouse warehouse : warehouses) {
            int shipmentCount = warehouse.getShipments().size();
            if (shipmentCount > maxShipments) {
                maxShipments = shipmentCount;
                busiestWarehouse = warehouse;
            }
        }
        return busiestWarehouse;
    }

    public Warehouse getBusiestWarehouse() {
        // Backwards compatibility
        return getBusiestWarehouse(this.warehouses);
    }

    public LocalDateTime getLastInspectionDate(Warehouse warehouse) {
        return warehouse.getLastInspectionDate();
    }

    // Total number of unique warehouses the shipment has been stored in
    public int getUniqueWarehouses(Shipment shipment) {
        ObservableList<Warehouse> uniqueWarehouses = FXCollections.observableArrayList();
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getShipments().contains(shipment)) {
                uniqueWarehouses.add(warehouse);
            }
        }
        return uniqueWarehouses.size();
    }

    // --- Shipment Methods --- //
    public Shipment createShipment(Warehouse warehouse, int size) throws LogisticsException {
        if (size > warehouse.calculateRemainingCapacity()) {
            throw new LogisticsException("Failed to create shipment on warehouse " + warehouse.getName()
                    + " because it overflows the capacity.");
        }

        Shipment shipment = new Shipment(size);
        warehouse.addShipment(shipment);
        ShipmentLog log = new ShipmentLog(shipment, ShipmentStatus.CREATED);
        log.setShipment(shipment);
        shipment.addShipmentLog(log);
        return shipment;
    }

    public Shipment createShipment(Warehouse warehouse, int size, LocalDateTime dateTime) {
        Shipment shipment = new Shipment(size);
        warehouse.addShipment(shipment);
        ShipmentLog log = new ShipmentLog(shipment, ShipmentStatus.CREATED, dateTime);
        log.setShipment(shipment);
        shipment.addShipmentLog(log);
        return shipment;
    }

    public void removeShipment(Shipment shipment) {
        Warehouse warehouse = shipment.getWarehouse();
        warehouse.removeShipment(shipment);
    }

    // Send a shipment to another warehouse, logs are created
    public void sendShipment(Shipment shipment, Warehouse destination) throws LogisticsException {
        sendShipment(shipment, destination, LocalDateTime.now());
    }

    // Send a shipment to another warehouse, logs are created
    public void sendShipment(Shipment shipment, Warehouse destination, LocalDateTime dateTime) throws LogisticsException {
        // Get current warehouse
        Warehouse currentWarehouse = shipment.getWarehouse();

        // Check if the shipment has a current warehouse
        if (currentWarehouse == null) {
            throw new LogisticsException("Shipment " + shipment.getId() + " has no current warehouse.");
        }

        // Can't send to the same warehouse
        if (currentWarehouse.equals(destination)) {
            throw new LogisticsException("Can't send a shipment to it's current warehouse!");
        }

        // Can't send a shipment to a warehouse that is at full capacity
        if (destination.calculateRemainingCapacity() < shipment.getSize()) {
            throw new LogisticsException("Destination warehouse " + destination.getName() + " is at full capacity.");
        }

        // Look for transportation loops
        boolean transportationLoop = false;
        for (ShipmentLog shipmentLog : shipment.getShipmentLogs()) {
            if (shipmentLog.getWarehouse().equals(destination)) {
                transportationLoop = true;
            }
        }

        // Add outgoing shipment log to currentWarehouse
        ShipmentLog outgoingLog = new ShipmentLog(shipment, ShipmentStatus.OUTGOING, dateTime);
        shipment.addShipmentLog(outgoingLog);

        // Set new references between shipment and warehouses
        currentWarehouse.removeShipment(shipment);
        destination.addShipment(shipment);

        // Add incoming shipment log to destination
        ShipmentLog incomingLog = new ShipmentLog(shipment, ShipmentStatus.INCOMING, dateTime);
        shipment.addShipmentLog(incomingLog);

        // Update the status of the shipment
        shipment.setStatus(ShipmentStatus.INCOMING);

        // If a transportation loop is detected, create a logisticsexception
        if (transportationLoop) {
            throw new LogisticsException("Transportation loop detected. Investigate shipment " + shipment.getId());
        }
    }

    // Register a new inspection of the warehouse, a log is created
    public InspectionLog inspectShipment(Shipment shipment, InspectionResult result, String inspector,
            LocalDateTime dateTime) {
        InspectionLog log = new InspectionLog(shipment, result, inspector, dateTime);
        shipment.addInspectionLog(log);
        return log;
    }

    public void createShipmentLog(Shipment shipment, ShipmentStatus status, LocalDateTime dateTime) {
        ShipmentLog shipmentLog = new ShipmentLog(shipment, status, dateTime);
        shipment.addShipmentLog(shipmentLog);
    }

    public void createShipmentLog(Shipment shipment, ShipmentStatus status) {
        ShipmentLog shipmentLog = new ShipmentLog(shipment, status);
        shipment.addShipmentLog(shipmentLog);
    }

    public void removeShipmentLog(ShipmentLog log) {
        Shipment shipment = log.getShipment();
        shipment.removeShipmentLog(log);
    }

    public void moveShipmentLog(ShipmentLog log, Shipment fromShipment, Shipment toShipment) {
        fromShipment.removeShipmentLog(log);
        toShipment.addShipmentLog(log);
    }

    public ObservableList<Shipment> getAllShipments() {
        ObservableList<Shipment> shipments = FXCollections.observableArrayList();
        for (Warehouse warehouse : this.warehouses) {
            shipments.addAll(warehouse.getShipments());
        }
        return shipments;
    }

    public ObservableList<ShipmentLog> getAllShipmentLogs() {
        ObservableList<ShipmentLog> shipmentLogs = FXCollections.observableArrayList();
        for (Warehouse warehouse : this.warehouses) {
            shipmentLogs.addAll(warehouse.getShipmentLogs());
        }
        return shipmentLogs;
    }

    public ObservableList<InspectionLog> getAllInspectionLogs() {
        ObservableList<InspectionLog> inspectionLogs = FXCollections.observableArrayList();
        for (Warehouse warehouse : this.warehouses) {
            inspectionLogs.addAll(warehouse.getInspectionLogs());
        }
        return inspectionLogs;
    }

    public void removeInspectionLog(InspectionLog inspection) {
        Shipment shipment = inspection.getShipment();
        if (shipment != null)
            shipment.removeInspectionLog(inspection);
    }
}
