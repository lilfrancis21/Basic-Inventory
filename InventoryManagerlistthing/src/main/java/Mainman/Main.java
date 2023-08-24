package Mainman;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Model.InHouse;
import Model.Inventory;
import Model.OutSourced;
import Model.Part;
import Model.Product;

import java.util.Arrays;
import java.util.List;

/** This is the Inventory System Management */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/View/MainScreen.fxml"));
        stage.setTitle("How many stuff I can fit into my inventory");
        stage.setScene(new Scene(root, 1150,700));
        stage.show();
    }
    public static void main(String[] args) {

        List<Part> parts = Arrays.asList(
                new InHouse(1, "Metal", 95.00, 6, 1, 20, 1234),
                new InHouse(2, "Wood", 900.00, 3, 1, 20, 12345),
                new InHouse(3, "Stone", 12.00, 18, 1, 20, 123456),
                new InHouse(4, "Cobblestone", 30.00, 16, 1, 20, 1234567),
                new InHouse(5, "Obedience", 29.99, 15, 1, 20, 12344567),
                new OutSourced(6, "Mercury", 35.00, 6, 1, 20, "AquaFina"));

        parts.forEach(Inventory::addPart);

        List<Product> products = Arrays.asList(
                new Product(1, "Legendary Sword", 999.99, 5, 0, 15),
                new Product(2, "Rare Sword", 699.99, 9, 0, 15),
                new Product(3, "Uncommon Sword", 300.00, 2, 0, 15)
        );
        products.forEach(Inventory::addProduct);

        products.get(0).addAssociatedPart(Inventory.lookupPart(1));
        products.get(0).addAssociatedPart(Inventory.lookupPart(2));
        products.get(0).addAssociatedPart(Inventory.lookupPart(3));

        products.get(1).addAssociatedPart(Inventory.lookupPart(4));
        products.get(1).addAssociatedPart(Inventory.lookupPart(5));
        products.get(1).addAssociatedPart(Inventory.lookupPart(6));

        products.get(2).addAssociatedPart(Inventory.lookupPart(7));
        products.get(2).addAssociatedPart(Inventory.lookupPart(8));
        products.get(2).addAssociatedPart(Inventory.lookupPart(1));

        launch(args);
    }
}

