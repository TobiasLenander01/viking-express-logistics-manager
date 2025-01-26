/*
 * Shipment.java is the model for a shipment. It contains the shipment's ID, status, size, warehouse,
 * shipment logs, and inspection logs.
 */

package hack.in.black.models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import hack.in.black.enums.ShipmentStatus;
import hack.in.black.utilities.UniqueID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Shipment {
    private final UniqueID id;
    private ShipmentStatus status;
    private int size;
    private Warehouse warehouse;
    private final ObservableList<ShipmentLog> shipmentLogs;
    private final ObservableList<InspectionLog> inspectionLogs;

    // Constructor
    public Shipment(int size) {
        this.id = new UniqueID(8);
        this.status = ShipmentStatus.CREATED;
        this.size = size;
        this.shipmentLogs = FXCollections.observableArrayList();
        this.inspectionLogs = FXCollections.observableArrayList();
        this.warehouse = null;
    }

    // Getters & Setters - No setters for ID, ShipmentLogs, and InspectionLogs
    public UniqueID getId() {
        return id;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (size < 0) {
            throw new NumberFormatException("Size cannot be negative.");
        }

        this.size = size;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public void addShipmentLog(ShipmentLog log) {
        // Check if the log is already in the list
        if (shipmentLogs.contains(log)) {
            System.out.println("ShipmentLog already exists in the list.");
            return;
        }

        // Add the log to the list
        this.shipmentLogs.add(log);
        log.setShipment(this);
    }

    public void removeShipmentLog(ShipmentLog log) {
        // Check if the log is in the list
        if (!shipmentLogs.contains(log)) {
            System.out.println("ShipmentLog does not exist in the list.");
            return;
        }

        // Remove the log from the list
        this.shipmentLogs.remove(log);
        log.setShipment(null);
    }

    public void addInspectionLog(InspectionLog log) {
        // Check if the log is already in the list
        if (inspectionLogs.contains(log)) {
            System.out.println("InspectionLog already exists in the list.");
            return;
        }

        // Add the log to the list
        this.inspectionLogs.add(log);
        log.setShipment(this);
    }

    public void removeInspectionLog(InspectionLog log) {
        // Check if the log is in the list
        if (!inspectionLogs.contains(log)) {
            System.out.println("InspectionLog does not exist in the list.");
            return;
        }

        // Remove the log from the list
        inspectionLogs.remove(log);
    }

    public ObservableList<ShipmentLog> getShipmentLogs() {
        return shipmentLogs;
    }

    public ObservableList<InspectionLog> getInspectionLogs() {
        return inspectionLogs;
    }

    public boolean getNeedsAttention() {
        return calculateDaysInCurrentWarehouse() > 14;
    }

    private int calculateDaysInCurrentWarehouse() {
        // Check if the shipment has been in a warehouse
        if (shipmentLogs.isEmpty() || warehouse == null) {
            return 0;
        }

        // Find the arrival date in the current warehouse
        LocalDateTime arrivalInCurrentWarehouse = null;
        for (ShipmentLog log : shipmentLogs) {
            if ((log.getStatus() == ShipmentStatus.INCOMING || log.getStatus() == ShipmentStatus.CREATED)
                    && log.getWarehouse() != null && log.getWarehouse().equals(warehouse)) {
                arrivalInCurrentWarehouse = log.getDateTime();
            }
        }

        if (arrivalInCurrentWarehouse == null) {
            System.err.println("Error calculating arrival in current warehouse because of missing logs.");
            // This happens if both CREATED and INCOMING logs are removed by the user
            return 0;
        }

        // Find out days in current warehouse warehouse
        Duration duration = Duration.between(arrivalInCurrentWarehouse, LocalDateTime.now());
        int daysInWarehouse = (int) duration.toDays();
        return daysInWarehouse;
    }

    public LocalDateTime getLastInspectionDate() {
        if (inspectionLogs.isEmpty()) {
            return null;
        }

        LocalDateTime lastInspectionDate = LocalDateTime.MIN;
        for (InspectionLog log : inspectionLogs) {
            if (log.getInspectionDate().isAfter(lastInspectionDate)) {
                lastInspectionDate = log.getInspectionDate();
            }
        }

        return lastInspectionDate;
    }

    // Get the number of warehouses the shipment has been in
    public int getNumberOfWarehouses() {
        int numberOfWarehouses = 0;

        ArrayList<Warehouse> readWarehouses = new ArrayList<>();
        for (ShipmentLog log : shipmentLogs) {
            if (log.getWarehouse() != null && !readWarehouses.contains(log.getWarehouse())) {
                readWarehouses.add(log.getWarehouse());
                numberOfWarehouses++;
            }
        }

        return numberOfWarehouses;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    public String toStringElaborate() {
        return "Shipment: " + id + " of size " + size + " at " + warehouse.getName();
    }

    public String toStringForTest() {
        String blue = "\u001B[34m";
        String green = "\u001B[32m";
        String yellow = "\u001B[33m";
        String cyan = "\u001B[36m";
        String reset = "\u001B[0m";

        return blue + "Shipment" + reset +
                "{id=" + green + id + reset +
                ", status=" + yellow + status + reset +
                ", size=" + cyan + size + reset +
                ", warehouse=" + green + warehouse.getName() + reset +
                ", lastInspectionDate=" + yellow + getLastInspectionDate() + reset +
                ", shipmentLogsSize=" + cyan + shipmentLogs.size() + reset +
                ", inspectionLogsSize=" + green + inspectionLogs.size() + reset +
                '}';
    }
}
