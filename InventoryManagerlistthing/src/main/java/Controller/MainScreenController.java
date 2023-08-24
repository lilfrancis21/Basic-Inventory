package Controller;

import Model.Inventory;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Model.Product;
import Model.Part;
import javafx.scene.Node;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Francis P
 * This Class Creates the Main Screen and
 * all the function from the Mainscreen controller
 * JavaDoc is located at /javadocs
 */

public class MainScreenController implements Initializable {


    @FXML
    private TextField PartSearchMain;
    @FXML
    private TableView<Product> ProductsTable;
    @FXML
    private TextField ProductSearchMain;

    @FXML
    private TableView<Part> PartsTable;
    @FXML
    private TableColumn<Part, Integer> PartIDMainCol;
    @FXML
    private TableColumn<Part, String> PartNameMainCol;
    @FXML
    private TableColumn<Part, Integer> PartInventoryLvlMainCol;
    @FXML
    private TableColumn<Part, Double> PricePartMainCol;

    @FXML
    private TableColumn<Product, Integer> IDProductMainCol;
    @FXML
    private TableColumn<Product, String> ProductNameMainCol;
    @FXML
    private TableColumn<Product, Integer> ProductInventoryMainCol;
    @FXML
    private TableColumn<Product, Double> PriceProductMainCol;

    /**
     * This set the names for table view information.
     * @param resourceBundle the resources used to localize
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        PartIDMainCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartNameMainCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartInventoryLvlMainCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        PricePartMainCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        PartsTable.setItems(Inventory.getAllParts());

        IDProductMainCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        ProductNameMainCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ProductInventoryMainCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        PriceProductMainCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        ProductsTable.setItems(Inventory.getAllProducts());
        ProductSearchMain.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                resetProductsTable();
            }
        });
        PartsTable.setItems(Inventory.getAllParts());
        PartSearchMain.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                resetPartsTable();
            }
        });



    }

    private void resetProductsTable() {
        ProductsTable.setItems(Inventory.getAllProducts());
    }
    private void resetPartsTable() {
        PartsTable.setItems(Inventory.getAllParts());
    }
    private Part lookupPartByID(int id) {
        for (Part part : Inventory.getAllParts()) {
            if (part.getId() == id) {
                return part;
            }
        }
        return null;
    }

    private Product lookupProductByID(int id) {
        for (Product product : Inventory.getAllProducts()) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }




    /**
     * Handles the table information inside both of the tables for the Main Screen.
     * <p>
     * This table handles all information inside the part column
     * The one above will help with the search button and classes.
     */

    @FXML
    private void SearchPartMainButtonPress() {
        String searchString = PartSearchMain.getText();
        ObservableList<Part> parts;

        if (isInteger(searchString)) {
            Part part = lookupPartByID(Integer.parseInt(searchString));
            if (part != null) {
                parts = FXCollections.observableArrayList(part);
            } else {
                parts = FXCollections.observableArrayList();
            }
        } else {
            parts = Inventory.lookupPart(searchString);
        }

        PartsTable.setItems(parts);
        if (parts.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("No part was found");
            alert.setContentText("Try a new search");
            alert.showAndWait();
        }
    }



    /**
     * add part button will take you to the ApartScreen.fxml
     * Pressing the Add Part Button will relocate to The Add Part Screen
     * Runtime ERROR: I forgot to reference the screen to FXMLLoader correctly.
     *  FUTURE ENHANCEMENT: Next time referenced everything before running it
     * Another Error is importing everything before trying it.
     */

    @FXML
    private void AddPartButtonMainPress(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/Addpartscreen.fxml"));
        Parent root1 = fxmlLoader.load();


        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        currentStage.close();


        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root1));
        stage.setTitle("Add Part Please");

        stage.show();
    }




    /**
     * RUNTIME Error: Location is not set.
     * I keep getting the wrong FXML file when adding or deleting certain files.
     * FUTURE ENHANCEMENT: Need to properly word and organized on action words.
     * Get the controller of the loaded fxml.
     * Pass the selected part to the controller.
     */



    @FXML
    private void AddModifyButtonMainPress() {
        Part selectedPart = PartsTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"No part selected");
            alert.showAndWait();
        } else {
            openInNewStage(selectedPart);
        }
    }

    private void openInNewStage(Part part) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ModifyPart.fxml"));
            Parent root = loader.load();

            ModifyPartController controller = loader.getController();

            controller.setSelectedPart(part);

            Stage stage = new Stage();
            stage.setTitle("Modify Part");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This will delete the part from the table in Main Screen.
     *
     */

    @FXML
    private void DeletePartButtonMainPress() {
        Part selectedPart = PartsTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"No part selected");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("WARNING!");
            alert.setHeaderText("You are about to delete a part");
            alert.setContentText("Are you sure you want to delete this part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(selectedPart);

                PartsTable.setItems(Inventory.getAllParts());
            }
        }
    }



    /**
         * This is the line where I separate the two tables.
         * <p>
         * this table handles product table column
     * This handles the search bar for Product in Main Screen.

         */

    @FXML
    private void SearchProductMainButtonPress() {
        String searchString = ProductSearchMain.getText();
        ObservableList<Product> products;

        if (isInteger(searchString)) {
            Product product = lookupProductByID(Integer.parseInt(searchString));
            if (product != null) {
                products = FXCollections.observableArrayList(product);
            } else {
                products = FXCollections.observableArrayList();
            }
        } else {
            products = Inventory.lookupProduct(searchString);
        }

        ProductsTable.setItems(products);
        if (products.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("No product was found");
            alert.setContentText("Try new search");
            alert.show();
        }
    }

    /**
     * Load the FXML file for the new scene
     * <p>
     * New Stage
     * showing the new stage
     * RUNTIME ERROR: Exception in thread "JavaFX Application Thread" java.lang.RuntimeException: java.lang.reflect.InvocationTargetException
     * Invalid input when entered into the application.
     * FUTURE ENHANCEMENT: class for handling errors and user feedback.
     * Go to Add Product Screen when pressed.
     */

@FXML
private void OnPressAddProductButton(ActionEvent event) throws IOException {

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/Addproducttable.fxml"));
    Parent root1 = fxmlLoader.load();

    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

    currentStage.close();

    Stage stage = new Stage();
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setScene(new Scene(root1));
    stage.setTitle("Add Product Please");

    stage.show();
}


    /**
     * Goes to Modify Product Screen when pressed.

     */

    @FXML
    private void OnActionModifyProductMain() throws IOException {
        Product selectedProduct = ProductsTable.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"No product selected");
            alert.showAndWait();
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/ModifyProducttable.fxml"));
            Parent root1 = fxmlLoader.load();

            ModifyProductController modifyProductController = fxmlLoader.getController();
            modifyProductController.setSelectedProduct(selectedProduct);


            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root1));
            stage.setTitle("Modify product");
            stage.show();
        }
    }

    /**
     * Select a product from the ProductsTable
     * if no products is selected, display an error message.
     * If a product is selected, confirmation to delete the product
     * if the product have parts, display an error to remove the associated parts before deleting.

     */


@FXML
private void OnActionDeleteProductMain() {
    Product selectedProduct = getSelectedProduct();

    if (selectedProduct == null) {
        displayErrorAlert("No product selected");
    } else if (confirmDeletion()) {
        if (hasAssociatedParts(selectedProduct)) {
            displayErrorAlert("This product has items related to the Product. Modify product and remove parts");
        } else {
            deleteProduct(selectedProduct);
        }
    }
}

    private Product getSelectedProduct() {
        return ProductsTable.getSelectionModel().getSelectedItem();
    }

    private void displayErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

    /**
     * sent out a warning every time before deleting product.
     *
     */

    private boolean confirmDeletion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("WARNING!");
        alert.setHeaderText("You are about to delete a product");
        alert.setContentText("Are you sure you want to delete this product?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private boolean hasAssociatedParts(Product product) {
        return !product.getAllAssociatedParts().isEmpty();
    }

    /**
     * This will refresh the table view.
     *
     */

    private void deleteProduct(Product product) {
        Inventory.deleteProduct(product);
        ProductsTable.refresh();
    }


    /**
     * This will end the program when I press exit.
     *
     */
    @FXML
    private void ExitMainButtonPress() {
        System.exit(0);
    }

}


