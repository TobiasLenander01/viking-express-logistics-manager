/*
 * Module configuration for the application.
 */

 module hack.in.black {
    // Export packages
    exports hack.in.black;
    exports hack.in.black.models;
    exports hack.in.black.enums;
    exports hack.in.black.utilities;

    // Requires modules
    requires javafx.fxml;
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.base;

    // Opens packages
    opens hack.in.black.controllers to javafx.fxml;
}
