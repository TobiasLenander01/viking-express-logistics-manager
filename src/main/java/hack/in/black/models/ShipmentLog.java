/*
 * ShipmentLog class represents individual log entries for a specific shipment.
 */

package hack.in.black.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import hack.in.black.enums.ShipmentStatus;
import hack.in.black.utilities.UniqueID;

public class ShipmentLog {
    private final UniqueID id;
    private LocalDateTime dateTime;
    private ShipmentStatus status;
    private Shipment shipment;
    private Warehouse warehouse;

    // Constructor - For automatic date entry
    public ShipmentLog(Shipment shipment, ShipmentStatus status) {
        this.id = new UniqueID(8);
        this.dateTime = LocalDateTime.now();
        this.status = status;
        this.shipment = shipment;
        this.warehouse = shipment.getWarehouse();
    }

    // Constructor - For manual date entry
    public ShipmentLog(Shipment shipment, ShipmentStatus status, LocalDateTime dateTime) {
        this.id = new UniqueID(8);
        this.dateTime = dateTime;
        this.status = status;
        this.shipment = shipment;
        this.warehouse = shipment.getWarehouse();
    }

    // Getters
    public UniqueID getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public Shipment getShipment() {
        return shipment;
    }

    // Setters
    public void setDateTime(LocalDateTime date) {
        this.dateTime = date;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = dateTime.format(formatter);
        return formattedDateTime + " - Shipment with ID " + shipment.getId() + " was " + status + " at " + warehouse.getName();
    }

    public String toStringForTest() {
        String blue = "\u001B[34m";
        String green = "\u001B[32m";
        String yellow = "\u001B[33m";
        String cyan = "\u001B[36m";
        String reset = "\u001B[0m";

        if (warehouse == null) {
            return blue + "ShipmentLog" + reset +
                    "{id=" + green + id + reset +
                    ", date=" + yellow + dateTime + reset +
                    ", status=" + cyan + status + reset +
                    ", shipment=" + yellow + shipment.getId() + reset +
                    '}';
        } else {
            return blue + "ShipmentLog" + reset +
                    "{id=" + green + id + reset +
                    ", date=" + yellow + dateTime + reset +
                    ", status=" + cyan + status + reset +
                    ", warehouse=" + green + warehouse.getName() + reset +
                    ", shipment=" + yellow + shipment.getId() + reset +
                    '}';
        }
    }
}