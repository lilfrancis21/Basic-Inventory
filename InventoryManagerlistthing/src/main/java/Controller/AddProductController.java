package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.stage.Modality;
import javafx.stage.Stage;
import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;



import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The Add Product Form
 * If Add Product Button is Pressed, located Add Product form.
 */
public class AddProductController implements Initializable {


    @FXML
    private TableView<Part> AddPartTable, SecondPartTable;

    @FXML
    private TableColumn<Part, Integer> AddProdColID, SecondPartID;

    @FXML
    private TableColumn<Part, String> AddProdPartName, SecondPartName;

    @FXML
    private TableColumn<Part, Integer> AddProdColInvLvl, SecondInvLevel;

    @FXML
    private TableColumn<Part, Double> AddProdColPriceUnit;
    @FXML
    private TableColumn<?, ?> SecondPriceUnit;

    @FXML
    private TextField AddProdID, AddProdName, AddInvField, AddProPrice, AddProdMax, AddProdSearchField, AddProdMin;

    private static final ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * Initializes the AddProduct by setting cell value for Add Part Table View.
     * RUNTIME ERROR: the table wasn't updating when search was made in the search text field. Causing the tableview to
     * display incorrect information.
     * I added a listener to the text field and thus updates the results in the text field.
     * FUTURE ENHANCEMENT: Adding another form of search feature that will handle spelling errors and minor changes.
     * @param url Used to resolve the location of the root object.
     * @param resourceBundle find the original root object or produce a null.
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        AddProdID.setDisable(true);

        AddProdColID.setCellValueFactory(new PropertyValueFactory<>("id"));
        AddProdPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        AddProdColInvLvl.setCellValueFactory(new PropertyValueFactory<>("stock"));
        AddProdColPriceUnit.setCellValueFactory(new PropertyValueFactory<>("price"));

        SecondPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        SecondPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        SecondInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        SecondPriceUnit.setCellValueFactory(new PropertyValueFactory<>("price"));

        AddPartTable.setItems(Inventory.getAllParts());
        SecondPartTable.setItems(associatedParts);
        AddProdSearchField.textProperty().addListener((observable, oldValue, newValue) -> SecondPartTable.setItems(searchParts(newValue)));
    }
    private ObservableList<Part> searchParts(String searchString){
        ObservableList<Part> parts = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();

        for(Part part : allParts){
            if(part.getName().toLowerCase().contains(searchString.toLowerCase()) || Integer.toString(part.getId()).contains(searchString)){
                parts.add(part);
            }
        }
        if (parts.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Can not find Part");
            alert.setContentText("Please Try Again");
            alert.showAndWait();
        }
        return parts;
    }

    /**
     * AddProd will add a part to the associate add part table.
     */

    @FXML
    private void AddProdAddButtonPress() {
        Part selectedPart = getSelectedPart(AddPartTable);
        addAssociatedPart(selectedPart, SecondPartTable);
    }

    /**
     * This Handles if parts aren't Selected
     * @param partTable Refer to the Part Table
     * @return Handles TableView or if no Parts is selected.
     */

    private Part getSelectedPart(TableView<Part> partTable) {
        Part selectedPart = partTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"No part selected");
            alert.showAndWait();
        }
        return selectedPart;
    }

    private void addAssociatedPart(Part selectedPart, TableView<Part> associatedPartTable) {
        if (selectedPart != null) {
            AddProductController.associatedParts.add(selectedPart);
            associatedPartTable.setItems(AddProductController.associatedParts);
        }
    }

    /**
     * This will save the information inputted in the add part form.
     * @param actionEvent the event will trigger the save when pressed.
     */
    @FXML
    private void AddProdTableSavePress(ActionEvent actionEvent) {
        try {
            validateFields();

            int id = getNewID();
            String name = AddProdName.getText();
            double price = Double.parseDouble(AddProPrice.getText());
            int inventory = Integer.parseInt(AddInvField.getText());
            int min = Integer.parseInt(AddProdMin.getText());
            int max = Integer.parseInt(AddProdMax.getText());

            Inventory.addProduct(new Product(id, name, price, inventory, min, max));
            for (Part part : associatedParts) {
                Objects.requireNonNull(Inventory.lookupProduct(id)).addAssociatedPart(part);
            }

            switchToMainScreen(actionEvent);
        }
        catch (NumberFormatException e) {
            displayAlert("Check fields for correct input values");
        }
        catch (InvalidInputException e) {
            displayAlert(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends out Error when rules are broken.
     * @throws InvalidInputException if the field text rules are broken.
     */
    private void validateFields() throws InvalidInputException {
        if(AddProdName.getText().trim().isEmpty() ||
                AddProPrice.getText().isEmpty() ||
                AddInvField.getText().isEmpty() ||
                AddProdMin.getText().isEmpty() ||
                AddProdMax.getText().isEmpty()) {
            throw new InvalidInputException("Make sure all fields are Correctly Filled.");
        }

        int min = Integer.parseInt(AddProdMin.getText());
        int max = Integer.parseInt(AddProdMax.getText());
        int inventory = Integer.parseInt(AddInvField.getText());

        if(min < 0) {
            throw new InvalidInputException("Min Can't be negative");
        }

        if(min > max) {
            throw new InvalidInputException("Max can't be less than Min");
        }

        if(min > inventory || max < inventory) {
            throw new InvalidInputException("Inventory must be between Min and Max");
        }
    }

    /**
     * Random ID's given to New Product's
     * @return This will return the ID along the product.
     */

    private int getNewID() {
        int newID = 1;
        while ( Inventory.lookupProduct(newID) != null){
            newID++;
        }
        return newID;
    }

    private void displayAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

    /**
     * Back to Main Screen
     * @param event holder for when save or cancel is initialized.

     */
    private void switchToMainScreen(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Mainscreen.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This will delete the Product from the second table.
     */

    @FXML
    private void AddProdDeletePart() {
        Part selectedPart = getSelectedPart(SecondPartTable);
        deleteAssociatedPart(selectedPart, SecondPartTable);
    }

    /**
     * This will delete the part from the associate part area.
     */
    private void deleteAssociatedPart(Part selectedPart, TableView<Part> associatedPartTable) {
        if (selectedPart != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("WARNING!");
            alert.setHeaderText("Removing a Part May Cause Problems");
            alert.setContentText("Are you sure you want to remove this part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                AddProductController.associatedParts.remove(selectedPart);
                associatedPartTable.setItems(AddProductController.associatedParts);
            }
        }
    }


    /**
     * This will Close the Add product screen and go back to Main screen.
     */

    @FXML
    private void AddPartTableCancelPress(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/Mainscreen.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root1));
            stage.setTitle("Add Product Please");

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Custom Exception class to handle Invalid Input.
     * RUNTIME ERROR: Invalid inputs was causing RunTime Error. To correct it
     * I added an error message.
     * FUTURE ENHANCEMENT: having a class that handles errors and user feedback.
     */
    public static class InvalidInputException extends Exception {
        public InvalidInputException(String message) {
            super(message);
        }
    }

}
