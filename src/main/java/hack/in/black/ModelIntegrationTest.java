/*
 * ModelIntegrationTest.java is a class that tests the integration of the model classes.
 * It creates instances of the LogisticsManager, Warehouse, and Shipment classes, and performs various operations on them.
 * 
 * The results of the operations are printed to the console.
 */

package hack.in.black;

import java.time.LocalDateTime;

import hack.in.black.enums.InspectionResult;
import hack.in.black.enums.Region;
import hack.in.black.models.InspectionLog;
import hack.in.black.models.LogisticsManager;
import hack.in.black.models.Shipment;
import hack.in.black.models.ShipmentLog;
import hack.in.black.models.Warehouse;
import hack.in.black.utilities.LogisticsException;

public class ModelIntegrationTest {

        public static void main(String[] args) {
                final String greenText = "\u001B[32m";
                final String resetText = "\u001B[0m";

                // Create an instance of the LogisticsManager
                LogisticsManager logisticsManager = new LogisticsManager();

                // Create 10 warehouses
                Warehouse w1 = new Warehouse("Asgard Archives", Region.NORTH, "221 Odin Blvd, Asgard, 08820", 8000);
                Warehouse w2 = new Warehouse("Midgard Storage", Region.MIDDLE, "567 Thor Ln, Midgard, 90001", 6500);
                Warehouse w3 = new Warehouse("Vanaheim Supply", Region.SOUTH, "789 Freya Ave, Vanaheim, 32801", 9000);
                Warehouse w4 = new Warehouse("Jotunheim Depot", Region.NORTH, "123 Loki St, Jotunheim, 60601", 7500);
                Warehouse w5 = new Warehouse("Alfheim Warehouse", Region.MIDDLE, "456 Baldur Ave, Alfheim, 77001", 6800);
                Warehouse w6 = new Warehouse("Nidavellir Storage", Region.SOUTH, "789 Eitri Blvd, Nidavellir, 33101", 6500);
                Warehouse w7 = new Warehouse("Helheim Depot", Region.NORTH, "123 Hel Ln, Helheim, 98101", 7000);
                Warehouse w8 = new Warehouse("Svartalfheim Warehouse", Region.MIDDLE, "456 Sindri St, Svartalfheim, 94016", 7500);
                Warehouse w9 = new Warehouse("Muspelheim Storage", Region.SOUTH, "789 Surtur Ave, Muspelheim, 30301", 8000);
                Warehouse w10 = new Warehouse("Niflheim Depot", Region.NORTH, "123 Hela Blvd, Niflheim, 02101", 7500);

                // Add these warehouses to the logistics manager
                logisticsManager.addWarehouse(w1);
                logisticsManager.addWarehouse(w2);
                logisticsManager.addWarehouse(w3);
                logisticsManager.addWarehouse(w4);
                logisticsManager.addWarehouse(w5);
                logisticsManager.addWarehouse(w6);
                logisticsManager.addWarehouse(w7);
                logisticsManager.addWarehouse(w8);
                logisticsManager.addWarehouse(w9);
                logisticsManager.addWarehouse(w10);

                // Create 20 shipments
                Shipment s1 = logisticsManager.createShipment(w10, 1500, LocalDateTime.of(2024, 12, 1, 9, 0));
                Shipment s2 = logisticsManager.createShipment(w1, 1400, LocalDateTime.of(2024, 12, 2, 10, 0));
                Shipment s3 = logisticsManager.createShipment(w2, 1600, LocalDateTime.of(2024, 12, 3, 11, 0));
                Shipment s4 = logisticsManager.createShipment(w3, 1500, LocalDateTime.of(2024, 12, 1, 12, 0));
                Shipment s5 = logisticsManager.createShipment(w4, 1400, LocalDateTime.of(2024, 12, 2, 13, 0));
                Shipment s6 = logisticsManager.createShipment(w5, 1650, LocalDateTime.of(2024, 12, 3, 14, 0));
                Shipment s7 = logisticsManager.createShipment(w6, 1700, LocalDateTime.of(2024, 12, 1, 15, 0));
                Shipment s8 = logisticsManager.createShipment(w7, 1300, LocalDateTime.of(2024, 12, 2, 16, 0));
                Shipment s9 = logisticsManager.createShipment(w8, 1800, LocalDateTime.of(2024, 12, 3, 17, 0));
                Shipment s10 = logisticsManager.createShipment(w9, 1600, LocalDateTime.of(2024, 12, 1, 18, 0));
                Shipment s11 = logisticsManager.createShipment(w10, 1500, LocalDateTime.of(2024, 12, 1, 9, 0));
                Shipment s12 = logisticsManager.createShipment(w1, 1450, LocalDateTime.of(2024, 12, 1, 10, 0));
                Shipment s13 = logisticsManager.createShipment(w2, 1600, LocalDateTime.of(2024, 12, 2, 11, 0));
                Shipment s14 = logisticsManager.createShipment(w3, 1550, LocalDateTime.of(2024, 12, 2, 12, 0));
                Shipment s15 = logisticsManager.createShipment(w4, 1400, LocalDateTime.of(2024, 12, 2, 13, 0));
                Shipment s16 = logisticsManager.createShipment(w5, 1650, LocalDateTime.of(2024, 12, 2, 14, 0));
                Shipment s17 = logisticsManager.createShipment(w6, 1700, LocalDateTime.of(2024, 12, 2, 15, 0));
                Shipment s18 = logisticsManager.createShipment(w7, 1350, LocalDateTime.of(2024, 12, 2, 16, 0));
                Shipment s19 = logisticsManager.createShipment(w8, 1800, LocalDateTime.of(2024, 12, 2, 17, 0));
                Shipment s20 = logisticsManager.createShipment(w9, 1700, LocalDateTime.of(2024, 12, 2, 18, 0));

                // Now, send these shipments to different destination warehouses
                try {
                        // Send shipment 1 to different warehouses
                        logisticsManager.sendShipment(s1, w3, LocalDateTime.of(2024, 12, 4, 10, 0));
                        logisticsManager.sendShipment(s1, w4, LocalDateTime.of(2024, 12, 5, 11, 30));
                        logisticsManager.sendShipment(s1, w5, LocalDateTime.of(2024, 12, 7, 12, 0));
                        logisticsManager.sendShipment(s1, w2, LocalDateTime.of(2025, 1, 1, 8, 30));

                        // Send shipment 2 to different warehouses
                        logisticsManager.sendShipment(s2, w7, LocalDateTime.of(2024, 12, 3, 10, 30));
                        logisticsManager.sendShipment(s2, w9, LocalDateTime.of(2024, 12, 6, 12, 30));
                        logisticsManager.sendShipment(s2, w10, LocalDateTime.of(2024, 12, 8, 13, 0));
                        logisticsManager.sendShipment(s2, w3, LocalDateTime.of(2025, 1, 2, 9, 45));

                        // Send shipment 3 to different warehouses
                        logisticsManager.sendShipment(s3, w5, LocalDateTime.of(2024, 12, 5, 10, 0));
                        logisticsManager.sendShipment(s3, w6, LocalDateTime.of(2024, 12, 7, 11, 30));
                        logisticsManager.sendShipment(s3, w8, LocalDateTime.of(2024, 12, 9, 12, 0));
                        logisticsManager.sendShipment(s3, w1, LocalDateTime.of(2025, 1, 3, 10, 15));

                        // Send shipment 4 to different warehouses
                        logisticsManager.sendShipment(s4, w10, LocalDateTime.of(2024, 12, 5, 9, 30));
                        logisticsManager.sendShipment(s4, w6, LocalDateTime.of(2024, 12, 8, 11, 30));
                        logisticsManager.sendShipment(s4, w8, LocalDateTime.of(2024, 12, 10, 12, 0));
                        logisticsManager.sendShipment(s4, w2, LocalDateTime.of(2025, 1, 4, 11, 30));

                        // Send shipment 5 to different warehouses
                        logisticsManager.sendShipment(s5, w3, LocalDateTime.of(2024, 12, 7, 10, 0));
                        logisticsManager.sendShipment(s5, w7, LocalDateTime.of(2024, 12, 9, 11, 30));
                        logisticsManager.sendShipment(s5, w9, LocalDateTime.of(2024, 12, 11, 12, 0));
                        logisticsManager.sendShipment(s5, w1, LocalDateTime.of(2025, 1, 5, 13, 45));

                        // Send shipment 6 to different warehouses
                        logisticsManager.sendShipment(s6, w2, LocalDateTime.of(2024, 12, 6, 9, 30));
                        logisticsManager.sendShipment(s6, w7, LocalDateTime.of(2024, 12, 10, 11, 30));
                        logisticsManager.sendShipment(s6, w10, LocalDateTime.of(2024, 12, 12, 12, 0));
                        logisticsManager.sendShipment(s6, w4, LocalDateTime.of(2025, 1, 6, 15, 00));

                        // Send shipment 7 to different warehouses
                        logisticsManager.sendShipment(s7, w1, LocalDateTime.of(2024, 12, 7, 9, 30));
                        logisticsManager.sendShipment(s7, w5, LocalDateTime.of(2024, 12, 11, 11, 30));
                        logisticsManager.sendShipment(s7, w8, LocalDateTime.of(2024, 12, 13, 12, 0));
                        logisticsManager.sendShipment(s7, w3, LocalDateTime.of(2025, 1, 6, 8, 30));

                        // Send shipment 8 to different warehouses
                        logisticsManager.sendShipment(s8, w2, LocalDateTime.of(2024, 12, 8, 9, 30));
                        logisticsManager.sendShipment(s8, w6, LocalDateTime.of(2024, 12, 12, 11, 30));
                        logisticsManager.sendShipment(s8, w9, LocalDateTime.of(2024, 12, 14, 12, 0));
                        logisticsManager.sendShipment(s8, w4, LocalDateTime.of(2025, 1, 7, 12, 15));

                        // Send shipment 9 to different warehouses
                        logisticsManager.sendShipment(s9, w1, LocalDateTime.of(2024, 12, 9, 9, 30));
                        logisticsManager.sendShipment(s9, w3, LocalDateTime.of(2024, 12, 11, 10, 0));
                        logisticsManager.sendShipment(s9, w2, LocalDateTime.of(2024, 12, 15, 12, 0));
                        logisticsManager.sendShipment(s9, w5, LocalDateTime.of(2024, 12, 16, 9, 45));

                        // Send shipment 10 to different warehouses
                        logisticsManager.sendShipment(s10, w4, LocalDateTime.of(2024, 12, 10, 9, 30));
                        logisticsManager.sendShipment(s10, w8, LocalDateTime.of(2024, 12, 14, 11, 30));
                        logisticsManager.sendShipment(s10, w10, LocalDateTime.of(2024, 12, 16, 12, 0));
                        logisticsManager.sendShipment(s10, w6, LocalDateTime.of(2025, 1, 8, 15, 30));

                        // Send shipment 11 to different warehouses
                        logisticsManager.sendShipment(s11, w1, LocalDateTime.of(2024, 12, 11, 9, 30));
                        logisticsManager.sendShipment(s11, w3, LocalDateTime.of(2024, 12, 13, 10, 0));
                        logisticsManager.sendShipment(s11, w7, LocalDateTime.of(2024, 12, 17, 12, 0));
                        logisticsManager.sendShipment(s11, w5, LocalDateTime.of(2024, 12, 18, 11, 30));

                        // Send shipment 12 to different warehouses
                        logisticsManager.sendShipment(s12, w2, LocalDateTime.of(2024, 12, 12, 9, 30));
                        logisticsManager.sendShipment(s12, w4, LocalDateTime.of(2024, 12, 14, 10, 0));
                        logisticsManager.sendShipment(s12, w8, LocalDateTime.of(2024, 12, 18, 12, 0));
                        logisticsManager.sendShipment(s12, w6, LocalDateTime.of(2024, 12, 16, 11, 30));

                        // Send shipment 13 to different warehouses
                        logisticsManager.sendShipment(s13, w1, LocalDateTime.of(2024, 12, 13, 9, 30));
                        logisticsManager.sendShipment(s13, w3, LocalDateTime.of(2024, 12, 15, 10, 0));
                        logisticsManager.sendShipment(s13, w5, LocalDateTime.of(2024, 12, 17, 11, 30));
                        logisticsManager.sendShipment(s13, w7, LocalDateTime.of(2024, 12, 19, 12, 0));

                        // Send shipment 14 to different warehouses
                        logisticsManager.sendShipment(s14, w2, LocalDateTime.of(2024, 12, 14, 9, 30));
                        logisticsManager.sendShipment(s14, w4, LocalDateTime.of(2024, 12, 16, 10, 0));
                        logisticsManager.sendShipment(s14, w6, LocalDateTime.of(2024, 12, 18, 11, 30));
                        logisticsManager.sendShipment(s14, w8, LocalDateTime.of(2024, 12, 20, 12, 0));

                        // Send shipment 15 to different warehouses
                        logisticsManager.sendShipment(s15, w1, LocalDateTime.of(2024, 12, 15, 9, 30));
                        logisticsManager.sendShipment(s15, w3, LocalDateTime.of(2024, 12, 17, 10, 0));
                        logisticsManager.sendShipment(s15, w5, LocalDateTime.of(2024, 12, 19, 11, 30));
                        logisticsManager.sendShipment(s15, w7, LocalDateTime.of(2024, 12, 21, 12, 0));

                        // Send shipment 16 to different warehouses
                        logisticsManager.sendShipment(s16, w2, LocalDateTime.of(2024, 12, 16, 9, 30));
                        logisticsManager.sendShipment(s16, w4, LocalDateTime.of(2024, 12, 18, 10, 0));
                        logisticsManager.sendShipment(s16, w6, LocalDateTime.of(2024, 12, 20, 11, 30));
                        logisticsManager.sendShipment(s16, w8, LocalDateTime.of(2024, 12, 22, 12, 0));

                        // Send shipment 17 to different warehouses
                        logisticsManager.sendShipment(s17, w1, LocalDateTime.of(2024, 12, 17, 9, 30));
                        logisticsManager.sendShipment(s17, w3, LocalDateTime.of(2024, 12, 19, 10, 0));
                        logisticsManager.sendShipment(s17, w5, LocalDateTime.of(2024, 12, 21, 11, 30));
                        logisticsManager.sendShipment(s17, w9, LocalDateTime.of(2024, 12, 23, 12, 0));

                        // Send shipment 18 to different warehouses
                        logisticsManager.sendShipment(s18, w2, LocalDateTime.of(2024, 12, 18, 9, 30));
                        logisticsManager.sendShipment(s18, w4, LocalDateTime.of(2024, 12, 20, 10, 0));
                        logisticsManager.sendShipment(s18, w6, LocalDateTime.of(2024, 12, 22, 11, 30));
                        logisticsManager.sendShipment(s18, w9, LocalDateTime.of(2024, 12, 24, 12, 0));

                        // Send shipment 19 to different warehouses
                        logisticsManager.sendShipment(s19, w1, LocalDateTime.of(2024, 12, 19, 9, 30));
                        logisticsManager.sendShipment(s19, w3, LocalDateTime.of(2024, 12, 21, 10, 0));
                        logisticsManager.sendShipment(s19, w5, LocalDateTime.of(2024, 12, 23, 11, 30));
                        logisticsManager.sendShipment(s19, w10, LocalDateTime.of(2024, 12, 25, 12, 0));

                        // Send shipment 20 to different warehouses
                        logisticsManager.sendShipment(s20, w2, LocalDateTime.of(2024, 12, 20, 9, 30));
                        logisticsManager.sendShipment(s20, w4, LocalDateTime.of(2024, 12, 22, 10, 0));
                        logisticsManager.sendShipment(s20, w6, LocalDateTime.of(2024, 12, 24, 11, 30));
                        logisticsManager.sendShipment(s20, w10, LocalDateTime.of(2024, 12, 26, 12, 0));
                } catch (LogisticsException e) {
                        System.out.println("Error sending shipment: " + e.getMessage());
                }

                // Inspect shipments
                logisticsManager.inspectShipment(s1, InspectionResult.PASSED, "Thor",
                                LocalDateTime.of(2024, 12, 1, 9, 30));
                logisticsManager.inspectShipment(s2, InspectionResult.FAILED, "Loki",
                                LocalDateTime.of(2024, 12, 2, 10, 0));
                logisticsManager.inspectShipment(s3, InspectionResult.PASSED, "Odin",
                                LocalDateTime.of(2024, 12, 3, 14, 15));
                logisticsManager.inspectShipment(s4, InspectionResult.FAILED, "Freya",
                                LocalDateTime.of(2024, 12, 4, 11, 45));
                logisticsManager.inspectShipment(s5, InspectionResult.PASSED, "Heimdall",
                                LocalDateTime.of(2024, 12, 5, 13, 30));
                logisticsManager.inspectShipment(s6, InspectionResult.FAILED, "Baldur",
                                LocalDateTime.of(2024, 12, 6, 12, 0));
                logisticsManager.inspectShipment(s7, InspectionResult.PASSED, "Tyr",
                                LocalDateTime.of(2024, 12, 7, 15, 45));
                logisticsManager.inspectShipment(s8, InspectionResult.FAILED, "Frigg",
                                LocalDateTime.of(2024, 12, 8, 16, 15));
                logisticsManager.inspectShipment(s9, InspectionResult.PASSED, "Sif",
                                LocalDateTime.of(2024, 12, 9, 9, 30));
                logisticsManager.inspectShipment(s10, InspectionResult.FAILED, "Njord",
                                LocalDateTime.of(2024, 12, 10, 10, 0));
                logisticsManager.inspectShipment(s11, InspectionResult.PASSED, "Skadi",
                                LocalDateTime.of(2024, 12, 11, 14, 15));
                logisticsManager.inspectShipment(s12, InspectionResult.FAILED, "Forseti",
                                LocalDateTime.of(2024, 12, 12, 11, 45));
                logisticsManager.inspectShipment(s13, InspectionResult.PASSED, "Bragi",
                                LocalDateTime.of(2024, 12, 13, 13, 30));
                logisticsManager.inspectShipment(s14, InspectionResult.FAILED, "Idun",
                                LocalDateTime.of(2024, 12, 14, 12, 0));
                logisticsManager.inspectShipment(s15, InspectionResult.PASSED, "Vidar",
                                LocalDateTime.of(2024, 12, 15, 15, 45));
                logisticsManager.inspectShipment(s16, InspectionResult.FAILED, "Vili",
                                LocalDateTime.of(2024, 12, 16, 16, 15));
                logisticsManager.inspectShipment(s17, InspectionResult.PASSED, "Ve",
                                LocalDateTime.of(2024, 12, 17, 9, 30));
                logisticsManager.inspectShipment(s18, InspectionResult.FAILED, "Ullr",
                                LocalDateTime.of(2024, 12, 18, 10, 0));
                logisticsManager.inspectShipment(s19, InspectionResult.PASSED, "Hermod",
                                LocalDateTime.of(2024, 12, 19, 14, 15));
                logisticsManager.inspectShipment(s20, InspectionResult.FAILED, "Hodr",
                                LocalDateTime.of(2024, 12, 20, 11, 45));

                //////////////
                /// TESTS ////
                //////////////

                System.out.println(greenText + "\nTransportation loop warning message:" + resetText);
                try {
                        logisticsManager.sendShipment(s7, w5);
                } catch (LogisticsException e) {
                        System.out.println(e.getMessage());
                }

                System.out.println(greenText + "\nShow necessary information for all warehouses:" + resetText);
                for (Warehouse warehouse : logisticsManager.getWarehouses()) {
                        System.out.println(warehouse.toStringForTest());
                }

                System.out.println(greenText + "\nShow necessary information for all shipments" + resetText);
                for (Shipment shipment : logisticsManager.getAllShipments()) {
                        System.out.println(shipment.toStringForTest());
                }

                System.out.println(greenText + "\nShow necessary information for all shipments logs" + resetText);
                for (ShipmentLog shipmentLog : logisticsManager.getAllShipmentLogs()) {
                        System.out.println(shipmentLog.toStringForTest());
                }

                System.out.println(greenText + "\nShow necessary information for all inspection logs" + resetText);
                for (InspectionLog log : logisticsManager.getAllInspectionLogs()) {
                        System.out.println(log.toStringForTest());
                }

                System.out.println(greenText + "\nWarehouse capacity can not be negative" + resetText);
                w1.setCapacity(-100);
                System.out.println("Warehouse capacity: " + w1.getCapacity());

                System.out.println(greenText
                                + "\nShow the total number of warehouses that a given shipment has been stored in"
                                + resetText);
                System.out.println(logisticsManager.getUniqueWarehouses(s1));

                System.out.println(greenText + "\nShow shipments needing a attention" + resetText);
                for (Warehouse warehouse : logisticsManager.getWarehouses()) {
                        if (!warehouse.getShipmentsNeedingAttention().isEmpty()) {
                                System.out.println(warehouse.getShipmentsNeedingAttention());
                        }
                }

                System.out.println(greenText
                                + "\nCalculate remaining capacity of a given warehouse and display it as a percentage"
                                + resetText);
                System.out.println(w1.calculateRemainingCapacity());

                System.out.println(greenText + "\nCalculate the throughput of a given warehouse" + resetText);
                System.out.println(logisticsManager.calculateAverageThroughput(w1));

                System.out.println(greenText
                                + "\nPrevent the user from adding a shipment to a warehouse that is at capacity"
                                + resetText);
                try {
                        logisticsManager.createShipment(w1, 10000);
                } catch (LogisticsException e) {
                        System.out.println(e.getMessage());
                }

                // Show current available capacity across all warehouses.
                System.out.println(greenText + "\nCurrent available capacity across all warehouses: " + resetText);
                System.out.println(logisticsManager.getRemainingCapacity());

                // Display information on the busiest warehouse.
                System.out.println(greenText + "\nBusiest Warehouse: " + resetText);
                System.out.println(logisticsManager.getBusiestWarehouse());

                // List inspectors involved in inspections at a certain warehouse.
                System.out.println(
                                greenText + "\nNames of everyone who have been involved in inspections at Warehouse 3: "
                                                + resetText);
                for (InspectionLog log : w3.getInspectionLogs()) {
                        System.out.println(log.getInspector());
                }
                System.out.println();
        }
}