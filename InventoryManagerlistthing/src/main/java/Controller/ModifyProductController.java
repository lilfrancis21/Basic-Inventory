package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Modify Product Table Form.
 * Controls the Modify Product Table Form when Modify Product is Pressed from the Main Screen.
 * Future Enhancement: FX-ID needs to be better named or else it will be in the wrong part of the code.
 */
public class ModifyProductController {
    @FXML
    private TableView<Part> ModProdPartTable;

    @FXML
    private TableColumn<Part, Integer> PartIDCol;

    @FXML
    private TableColumn<Part, String> PartNameCol;

    @FXML
    private TableColumn<Part, Integer> ModPartInvCol;

    @FXML
    private TableColumn<Part, Double> ModProdPrice;

    @FXML
    private TableView<Part> ModProdSecondTable;

    @FXML
    private TableColumn<Part, Integer> SecondTableIDCol;

    @FXML
    private TableColumn<Part, String> SecondPartNameCol;

    @FXML
    private TableColumn<Part, Integer> SecondInvLVL;

    @FXML
    private TableColumn<Part, Double> SecondTablePrice;
    @FXML
    private TextField PartSearcherField;


    @FXML
    private Button modprodcancelbutton;

    @FXML
    private TextField ModProdIDField;

    @FXML
    private TextField InsertNameModProd;

    @FXML
    private TextField InsertInventoryModProd;

    @FXML
    private TextField InsertPriceModProd;

    @FXML
    private TextField InsertMaxModProd;

    @FXML
    private TextField InsertMinModProd;

    private Product originalProduct;

    /**
     * Disable Text in Modify Product Form.
     * The ID will be randomly Generated
     */
    @FXML
    private void initialize() {
        ModProdIDField.setDisable(true);
    }

    private Product selectedProduct;

    /**
     *Method for search logic
     */
    private ObservableList<Part> PartSearcherField(String searchString) {
        ObservableList<Part> parts = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();

        for (Part part : allParts) {
            if (part.getName().toLowerCase().contains(searchString.toLowerCase()) || Integer.toString(part.getId()).contains(searchString)) {
                parts.add(part);
            }
        }
        return parts;
    }

    /**
     * Handles the search when the button Search is pressed.
     */
    @FXML
    private void OnActionSearchButton() {
        String searchString = PartSearcherField.getText();
        ObservableList<Part> parts = PartSearcherField(searchString);

        if (parts.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Could not find Product");
            alert.setContentText("Please Try Again");
            alert.show();
        } else {
            ModProdPartTable.setItems(parts);
        }

        PartSearcherField.clear();
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
        ModProdIDField.setText(String.valueOf(selectedProduct.getId()));
        InsertNameModProd.setText(selectedProduct.getName());
        InsertInventoryModProd.setText(String.valueOf(selectedProduct.getStock()));
        InsertPriceModProd.setText(String.valueOf(selectedProduct.getPrice()));
        InsertMaxModProd.setText(String.valueOf(selectedProduct.getMax()));
        InsertMinModProd.setText(String.valueOf(selectedProduct.getMin()));

        PartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ModPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ModProdPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        ModProdPartTable.setItems(Inventory.getAllParts());

        SecondTableIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        SecondPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        SecondInvLVL.setCellValueFactory(new PropertyValueFactory<>("stock"));
        SecondTablePrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        ModProdSecondTable.setItems(selectedProduct.getAllAssociatedParts());
        this.originalProduct = new Product(
                selectedProduct.getId(),
                selectedProduct.getName(),
                selectedProduct.getPrice(),
                selectedProduct.getStock(),
                selectedProduct.getMin(),
                selectedProduct.getMax()
        );
        for (Part part : selectedProduct.getAllAssociatedParts()) {
            this.originalProduct.addAssociatedPart(part);
        }


    }


    /**
     * Add Part to Second Table.
     */

    @FXML
    public void OnActionAddPress() {
        Part partToMove = ModProdPartTable.getSelectionModel().getSelectedItem();
        if (partToMove == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"No part selected");
            alert.showAndWait();
        } else {
            if (selectedProduct == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"No product selected");
                alert.showAndWait();
            } else {
                selectedProduct.addAssociatedPart(partToMove);
                ModProdSecondTable.refresh();
            }
        }
    }



    /**
     * This button will save information changes.
     * “FUTURE ENHANCEMENT Null Exception pointer Error needs to be corrected to have persistent storage
     */



    @FXML
    public void OnActionSavePress() {
        try {
            if (InsertNameModProd.getText().isEmpty() || InsertInventoryModProd.getText().isEmpty() || InsertPriceModProd.getText().isEmpty() || InsertMinModProd.getText().isEmpty() || InsertMaxModProd.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Make sure all the boxes are filled in.");
                alert.showAndWait();
            } else if (InsertNameModProd.getText().trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Make sure all the boxes are filled in.");
                alert.showAndWait();
            } else if (Integer.parseInt(InsertMinModProd.getText()) < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The Min Can't not be a negative");
                alert.showAndWait();
            } else if (Integer.parseInt(InsertMinModProd.getText()) > Integer.parseInt(InsertMaxModProd.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Max can't be less than Min");
                alert.showAndWait();
            } else if (Integer.parseInt(InsertMinModProd.getText()) > Integer.parseInt(InsertInventoryModProd.getText()) || Integer.parseInt(InsertMaxModProd.getText()) < Integer.parseInt(InsertInventoryModProd.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inventory needs to be between min and max");
                alert.showAndWait();
            } else {
                int id = Integer.parseInt(ModProdIDField.getText());
                String name = InsertNameModProd.getText();
                double price = Double.parseDouble(InsertPriceModProd.getText());
                int inventory = Integer.parseInt(InsertInventoryModProd.getText());
                int min = Integer.parseInt(InsertMinModProd.getText());
                int max = Integer.parseInt(InsertMaxModProd.getText());

                if (selectedProduct == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "No product selected");
                    alert.showAndWait();
                    return;
                }

                Product updatedProduct = new Product(id, name, price, inventory, min, max);
                for (Part part : selectedProduct.getAllAssociatedParts()) {
                    updatedProduct.addAssociatedPart(part);
                }
                int index = Inventory.getAllProducts().indexOf(selectedProduct);

                if (index != -1) {
                    Inventory.updateProduct(index, updatedProduct);
                }

                Stage stage = (Stage) modprodcancelbutton.getScene().getWindow();
                stage.close();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Check boxes for correct information.");
            alert.showAndWait();
        }
    }


    /**
     * This will delete the part in the Second Table
     */

    @FXML
    public void OnActionDeleteSecondTablePart() {
        if (selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"No product selected");
            alert.showAndWait();
            return;
        }

        Part partToMove = ModProdSecondTable.getSelectionModel().getSelectedItem();
        if (partToMove == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"No part selected");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("WARNING!");
            alert.setHeaderText("Removing Part May Cause Problems");
            alert.setContentText("Are you sure you want to remove this part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean isRemoved = selectedProduct.deleteAssociatedPart(partToMove);
                if (isRemoved) {
                    ModProdSecondTable.setItems(selectedProduct.getAllAssociatedParts());
                } else {
                    Alert alertFailure = new Alert(Alert.AlertType.ERROR, "Failed to remove part");
                    alertFailure.showAndWait();
                }
            }
        }
    }






    /**
     * This action event will cancel everything and go back to the Main Screen
     * On pressed the program will return to the Main Screen.
     */

    @FXML
    public void OnActionCancelPress() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel any changes made?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            if (selectedProduct != null) {
                selectedProduct.setId(originalProduct.getId());
                selectedProduct.setName(originalProduct.getName());
                selectedProduct.setPrice(originalProduct.getPrice());
                selectedProduct.setStock(originalProduct.getStock());
                selectedProduct.setMin(originalProduct.getMin());
                selectedProduct.setMax(originalProduct.getMax());

                ObservableList<Part> tempParts = FXCollections.observableArrayList(selectedProduct.getAllAssociatedParts());
                for (Part part : tempParts) {
                    selectedProduct.deleteAssociatedPart(part);
                }

                for (Part part : originalProduct.getAllAssociatedParts()) {
                    selectedProduct.addAssociatedPart(part);
                }
            }

            Stage stage = (Stage) modprodcancelbutton.getScene().getWindow();
            stage.close();
        }
    }

}


