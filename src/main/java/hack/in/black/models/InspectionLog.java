/*
 * Inspection class represents the inspection of a specific shipment.
 */

package hack.in.black.models;

import java.time.LocalDateTime;

import hack.in.black.enums.InspectionResult;
import hack.in.black.utilities.UniqueID;

public class InspectionLog {
    private final UniqueID id;
    private InspectionResult result;
    private String inspector;
    private LocalDateTime inspectionDate;
    private Warehouse warehouse;
    private Shipment shipment;

    // Constructor - For automatic date entry
    public InspectionLog(Shipment shipment, InspectionResult result, String inspector) {
        this.id = new UniqueID(8);
        this.inspectionDate = LocalDateTime.now();
        this.result = result;
        this.inspector = inspector;
        this.shipment = shipment;
        this.warehouse = shipment.getWarehouse();
    }

    // Constructor - For manual date entry
    public InspectionLog(Shipment shipment, InspectionResult result, String inspector, LocalDateTime dateTime) {
        this.id = new UniqueID(8);
        this.inspectionDate = dateTime;
        this.result = result;
        this.inspector = inspector;
        this.shipment = shipment;
        this.warehouse = shipment.getWarehouse();
    }

    // Getters
    public UniqueID getId() {
        return id;
    }

    public InspectionResult getResult() {
        return result;
    }

    public String getInspector() {
        return inspector;
    }

    public LocalDateTime getInspectionDate() {
        return inspectionDate;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    // Setters
    public void setResult(InspectionResult result) {
        this.result = result;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public void setInspectionDate(LocalDateTime inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public String toString() {
        return "InspectionLog: " + id;
    }

    public String toStringForTest() {
        String blue = "\u001B[34m";
        String green = "\u001B[32m";
        String yellow = "\u001B[33m";
        String cyan = "\u001B[36m";
        String reset = "\u001B[0m";

        return blue + "InspectionLog" + reset +
                "{id=" + green + id + reset +
                ", result=" + yellow + result + reset +
                ", inspectionDate=" + cyan + inspectionDate + reset +
                ", inspector='" + green + inspector + reset + '\'' +
                ", shipment=" + yellow + shipment.getId() + reset +
                ", warehouse=" + cyan + warehouse.getName() + reset +
                '}';
    }
}