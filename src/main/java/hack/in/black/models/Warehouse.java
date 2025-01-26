/*
 * Warehouse.java is the model for a warehouse. It contains the warehouse's ID, name, region, address,
 * capacity, and shipments.
 */

package hack.in.black.models;

import java.time.Duration;
import java.time.LocalDateTime;

import hack.in.black.enums.Region;
import hack.in.black.enums.ShipmentStatus;
import hack.in.black.utilities.UniqueID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Warehouse {
    private final UniqueID id;
    private String name;
    private Region region;
    private String address;
    private int capacity;
    private final ObservableList<Shipment> shipments;

    // Constructor
    public Warehouse(String name, Region region, String address, int capacity) {
        this.id = new UniqueID(8);
        this.name = name;
        this.region = region;
        this.address = address;
        this.capacity = capacity;
        this.shipments = FXCollections.observableArrayList();
    }

    // Getters
    public UniqueID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Region getRegion() {
        return region;
    }

    public String getAddress() {
        return address;
    }

    public int getCapacity() {
        return capacity;
    }

    public ObservableList<Shipment> getShipments() {
        return shipments;
    }

    public ObservableList<ShipmentLog> getShipmentLogs() {
        ObservableList<ShipmentLog> shipmentLogs = FXCollections.observableArrayList();
        for (Shipment shipment : shipments) {
            shipmentLogs.addAll(shipment.getShipmentLogs());
        }
        return shipmentLogs;
    }

    public ObservableList<InspectionLog> getInspectionLogs() {
        ObservableList<InspectionLog> inspectionLogs = FXCollections.observableArrayList();
        for (Shipment shipment : shipments) {
            inspectionLogs.addAll(shipment.getInspectionLogs());
        }
        return inspectionLogs;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCapacity(int capacity) {
        if (capacity >= 0) {
            this.capacity = capacity;
        }
    }

    public void addShipment(Shipment shipment) {
        // Check if shipment already exists in warehouse
        if (shipments.contains(shipment)) {
            System.out.println("Shipment already exists in warehouse.");
            return;
        }

        // Check if shipment size exceeds warehouse capacity
        if (shipment.getSize() > calculateRemainingCapacity()) {
            System.out.println("Shipment size exceeds warehouse capacity.");
            return;
        }

        // Add shipment
        this.shipments.add(shipment);
        shipment.setWarehouse(this);
    }

    public void removeShipment(Shipment shipment) {
        // Check if shipment exists in warehouse
        if (!shipments.contains(shipment)) {
            System.out.println("Shipment does not exist in warehouse.");
            return;
        }

        // Remove shipment
        this.shipments.remove(shipment);
        shipment.setWarehouse(null);
    }

    // Calculate current stock level
    public int calculateCurrentStockLevel() {
        int currentStockLevel = 0;
        for (Shipment shipment : shipments) {
            currentStockLevel += shipment.getSize();
        }
        return currentStockLevel;
    }

    // Capacity check and update
    public int calculateRemainingCapacity() {
        return this.capacity - this.calculateCurrentStockLevel();
    }

    public double calculateRemainingCapacityPercentage() {
        // Calculate remaining capacity percentage
        int remainingCapacity = calculateRemainingCapacity();
        int totalCapacity = getCapacity();
        double percentage = (double) remainingCapacity / totalCapacity * 100;
        return percentage;
    }

    public int calculateDaysInWarehouseForShipment(Shipment shipment) {
        LocalDateTime arrivalDate = findArrivalDateForShipment(shipment);
        LocalDateTime departureDate = findDepartureDateForShipment(shipment);

        if (arrivalDate != null && departureDate != null) {
            Duration duration = Duration.between(arrivalDate, departureDate);
            return (int) duration.toDays();
        }
        return 0;
    }

    private LocalDateTime findArrivalDateForShipment(Shipment shipment) {
        for (ShipmentLog log : shipment.getShipmentLogs()) {
            if ((log.getStatus() == ShipmentStatus.INCOMING || log.getStatus() == ShipmentStatus.CREATED)
                    && log.getWarehouse() != null && log.getWarehouse().equals(this)) {
                return log.getDateTime();
            }
        }
        return null;
    }

    private LocalDateTime findDepartureDateForShipment(Shipment shipment) {
        for (ShipmentLog log : shipment.getShipmentLogs()) {
            if (log.getStatus() == ShipmentStatus.OUTGOING
                    && log.getWarehouse() != null && log.getWarehouse().equals(this)) {
                return log.getDateTime();
            }
        }
        return LocalDateTime.now();
    }

    // Check if Shipments Need Attention
    public ObservableList<Shipment> getShipmentsNeedingAttention() {
        ObservableList<Shipment> shipmentsNeedingAttention = FXCollections.observableArrayList();
        for (Shipment shipment : shipments) {
            if (shipment.getNeedsAttention()) {
                shipmentsNeedingAttention.add(shipment);
            }
        }
        return shipmentsNeedingAttention;
    }

    // Get List of Inspectors
    public ObservableList<String> getInspectors() {
        ObservableList<String> inspectors = FXCollections.observableArrayList();
        for (InspectionLog inspectionLog : getInspectionLogs()) {
            inspectors.add(inspectionLog.getInspector());
        }
        return inspectors;
    }

    public LocalDateTime getLastInspectionDate() {
        LocalDateTime lastInspectionDate = LocalDateTime.MIN;
        for (Shipment shipment : shipments) {
            if (shipment.getLastInspectionDate() != null
                    && shipment.getLastInspectionDate().isAfter(lastInspectionDate)) {
                lastInspectionDate = shipment.getLastInspectionDate();
            }
        }
        return lastInspectionDate;
    }

    @Override
    public String toString() {
        return name;
    }

    public String toStringForTest() {
        String blue = "\u001B[34m";
        String green = "\u001B[32m";
        String yellow = "\u001B[33m";
        String cyan = "\u001B[36m";
        String reset = "\u001B[0m";

        return blue + "Warehouse" + reset +
                "{id=" + green + id + reset +
                ", name='" + yellow + name + reset + '\'' +
                ", region=" + cyan + region + reset +
                ", address='" + yellow + address + reset + '\'' +
                ", capacity=" + cyan + capacity + reset +
                ", shipmentsSize=" + shipments.size() +
                ", shipmentLogsSize=" + getShipmentLogs().size() +
                ", inspectionLogsSize=" + getInspectionLogs().size() +
                '}';
    }

}