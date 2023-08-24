package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.OutSourced;
import Model.Part;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;



/**
 * The Modify part Form.
 * Modify Part Form when the Button Modify Part Form is pressed on the Main Screen.
 */
public class ModifyPartController {

    @FXML
    private TextField PartIDField;

    @FXML
    private TextField ModPartField;

    @FXML
    private TextField ModPartInvField;

    @FXML
    private TextField ModPartPriceField;

    @FXML
    private TextField ModPartMaxField;

    @FXML
    private TextField ComMachIDField;

    @FXML
    private TextField ModPartMinField;

    @FXML
    private Button ModPartCancel;
    @FXML
    private Label MachOrComp;


    @FXML
    private RadioButton InHouseButton;

    @FXML
    private RadioButton OutSourcedButton;


    /**
     * Disable the text in the ID Field
     * ID Text Field are randomly Generated.
     */
    @FXML
    private void initialize() {
        PartIDField.setDisable(true);
    }

    private boolean inHouseBool = true;

    private Part selectedPart;

    public void setSelectedPart(Part part) {
        this.selectedPart = part;
        loadPartData();
    }

    private void loadPartData() {
        if (selectedPart != null) {
            PartIDField.setText(String.valueOf(selectedPart.getId()));
            ModPartField.setText(selectedPart.getName());
            ModPartInvField.setText(String.valueOf(selectedPart.getStock()));
            ModPartPriceField.setText(String.valueOf(selectedPart.getPrice()));
            ModPartMaxField.setText(String.valueOf(selectedPart.getMax()));
            ModPartMinField.setText(String.valueOf(selectedPart.getMin()));

            if (selectedPart instanceof InHouse) {
                InHouseButton.setSelected(true);
                ComMachIDField.setPromptText("Enter machine Number");
                ComMachIDField.setText(String.valueOf(((InHouse) selectedPart).getMachineId()));
            } else if (selectedPart instanceof OutSourced) {
                OutSourcedButton.setSelected(true);
                ComMachIDField.setPromptText("Enter name");
                ComMachIDField.setText(((OutSourced) selectedPart).getCompanyName());
            }
        }
    }

    /**
     * When In-house Radio selected it will display Machine Number
     */
    @FXML
    public void OnActionInHouse() {
        System.out.println("In-house button pressed.");

        inHouseBool = true;
        MachOrComp.setText("Enter machine ID");
    }

    /**
     * When Out-Sourced Radio selected it will display Company Name.
     */
    @FXML
    public void OutSourcedButtonPress() {
        System.out.println("Outsourced button pressed.");
        inHouseBool = false;
        MachOrComp.setText("Enter Company");
    }

    /**
     * The Save Button will save all information inputted in the Modify Part Form.
     */
    @FXML
    public void SaveOnPressMod() {
        try {
            if (ModPartField.getText().isEmpty() || ModPartPriceField.getText().isEmpty() || ModPartInvField.getText().isEmpty() ||
                    ModPartMinField.getText().isEmpty() || ModPartMaxField.getText().isEmpty() || ComMachIDField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Make sure all fields Are Correct");
                alert.showAndWait();
            } else if (Integer.parseInt(ModPartMinField.getText()) < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Min Can't be Negative");
                alert.showAndWait();
            } else if (Integer.parseInt(ModPartMinField.getText()) > Integer.parseInt(ModPartMaxField.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Max Can't be Less than Min");
                alert.showAndWait();
            } else if (Integer.parseInt(ModPartMinField.getText()) > Integer.parseInt(ModPartInvField.getText()) ||
                    Integer.parseInt(ModPartMaxField.getText()) < Integer.parseInt(ModPartInvField.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inventory must be between Min and Max");
                alert.showAndWait();
            } else {
                String name = ModPartField.getText();
                double price = Double.parseDouble(ModPartPriceField.getText());
                int inventory = Integer.parseInt(ModPartInvField.getText());
                int min = Integer.parseInt(ModPartMinField.getText());
                int max = Integer.parseInt(ModPartMaxField.getText());
                int id = Integer.parseInt(PartIDField.getText());

                if (inHouseBool) {
                    int machine = Integer.parseInt(ComMachIDField.getText());
                    InHouse update = new InHouse(id, name, price, inventory, min, max, machine);

                    if (selectedPart instanceof InHouse) {
                        ((InHouse) selectedPart).setMachineId(machine);
                        selectedPart.setName(name);
                        selectedPart.setPrice(price);
                        selectedPart.setStock(inventory);
                        selectedPart.setMin(min);
                        selectedPart.setMax(max);

                    } else {
                        updateProduct(id, update);
                    }
                } else {
                    String company = ComMachIDField.getText();
                    OutSourced update = new OutSourced(id, name, price, inventory, min, max, company);

                    if (selectedPart instanceof OutSourced) {
                        ((OutSourced) selectedPart).setCompanyName(company);
                        selectedPart.setName(name);
                        selectedPart.setPrice(price);
                        selectedPart.setStock(inventory);
                        selectedPart.setMin(min);
                        selectedPart.setMax(max);

                    } else {
                        updateProduct(id, update);
                    }
                }
                Stage stage = (Stage) ModPartCancel.getScene().getWindow();
                stage.close();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please Check your Information Again.");
            alert.showAndWait();
        }
    }

    public void updateProduct(int id, Part part) {
        int index = findPartIndex(id);
        if (index != -1) {
            Inventory.updatePart(index, part);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Part with this ID doesn't exist");
            alert.showAndWait();
        }
    }



    public int findPartIndex(int partId) {
        ObservableList<Part> allParts = Inventory.getAllParts();
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getId() == partId) {
                return i;
            }
        }
        return -1;
    }


    /**
     * Cancel all changes made and return to Main Screen.
     */
    @FXML
    public void OnPressCancelMod() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel any changes made?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ModPartCancel.getScene().getWindow();
            stage.close();
        }
    }

}

