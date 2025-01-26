/*
 * AppModel.java is the model for the application. It contains the LogisticsManager object which
 * manages all the warehouses and shipments.
 */

package hack.in.black.models;

public class AppModel {
    private LogisticsManager logisticsManager;

    public AppModel() {
        this.logisticsManager = new LogisticsManager();
    }

    public LogisticsManager getLogisticsManager() {
        return logisticsManager;
    }

    public void setLogisticsManager(LogisticsManager warehouseManager) {
        this.logisticsManager = warehouseManager;
    }
}
