/**
 *JavaDoc is located at /javadocs
 * This is the Main Module for Java docs
 * Here are including my Controller,Model, and View it's under Resources.
 * @author Francis Pagtama
 */
module grinding.inventorymanagerlistthing {
    requires javafx.controls;
    requires javafx.fxml;

    opens Controller to javafx.fxml;
    opens Mainman to javafx.graphics;
    opens Model to javafx.fxml, javafx.controls, javafx.base;

}



