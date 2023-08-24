package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.OutSourced;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;


/**
 * The Add Part Form.
 * If the Add Part Button is Selected from the Main Screen, the User will locate here.
 */
public class AddPartController {
    public ToggleGroup Options;
    public Button addpartsavebutton;
    public Button addpartcancelbutton;
    Stage stage;
    Parent scene;

    /**
     * The Part class that manages the part objects and related operations.
     * RUNTIME ERROR: application was not checking whether a part was selected in the TableView before trying to modify it, leading to a Null Pointer Exception runtime error when the modify button was clicked with no selection
     * FUTURE ENHANCEMENT: A potential enhancement for the Part class could be to include additional attributes for the part.
     */

    @FXML
    private RadioButton AddPartInHouseRadio;

    @FXML
    private RadioButton AddPartOutSourcedRadio;

    @FXML
    private TextField AddPartIDField;

    @FXML
    private TextField AddPartNameField;

    @FXML
    private TextField AddPartInvField;

    @FXML
    private TextField AddPartPriceCostField;

    @FXML
    private TextField AddPartMax;

    @FXML
    private TextField AddPartMin;

    @FXML
    private TextField AddPartMachineText;


    @FXML
    private Label AddPartMachCompany;


    /**
     * Disable text in ID field.
     */
    @FXML
    private void initialize() {
        AddPartIDField.setDisable(true);
    }

    /**
     * Set as an event to go to Main Screen after pressing Cancel.
     * @param event to Main Screen resolve relative path.
     * @throws IOException if the fxml file for the main screen cannot be loaded.
     */
    public void goToMainScreen(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/MainScreen.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * In-House Radio Press will display Enter Machine Number.
     */

    @FXML
    private void AddPartRadioInHousePress() {
        AddPartMachCompany.setText("Machine ID");
        AddPartMachineText.setPromptText(" Enter machine Number");
    }

    /**
     * Out-Sourced Radio will display Company Name.
     */

    @FXML
    private void AddPartOutSourcedPress() {
        AddPartMachCompany.setText("Company Name");
        AddPartMachineText.setPromptText(" Enter Company Name");
    }

    /**
     * Save to the Main Table Screen.
     * @param event An Event to handle a Save.
     * @throws IOException IF there's an error handling the save.
     */


    @FXML
    private void AddPartSavePress(ActionEvent event) throws IOException {
        boolean isInHouse = AddPartInHouseRadio.isSelected();

        try {

            int uniqueId = Inventory.incrementPartId();

            if (isInHouse) {
                Inventory.addPart(new InHouse(
                        uniqueId,
                        AddPartNameField.getText(),
                        Double.parseDouble(AddPartPriceCostField.getText()),
                        Integer.parseInt(AddPartInvField.getText()),
                        Integer.parseInt(AddPartMin.getText()),
                        Integer.parseInt(AddPartMax.getText()),
                        Integer.parseInt(AddPartMachineText.getText())));
            } else {
                Inventory.addPart(new OutSourced(
                        uniqueId,
                        AddPartNameField.getText(),
                        Double.parseDouble(AddPartPriceCostField.getText()),
                        Integer.parseInt(AddPartInvField.getText()),
                        Integer.parseInt(AddPartMin.getText()),
                        Integer.parseInt(AddPartMax.getText()),
                        AddPartMachineText.getText()));
            }

            goToMainScreen(event);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter valid number!");
            alert.showAndWait();
        }
    }



    /**
     * Cancel The Add Part Controller
     * @param event That Handles the Cancel event
     * @throws IOException To handle the error while in transition.
     */
    @FXML
    private void AddPartCancelPress(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will cancel any changes, would you like to proceed?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            AddPartNameField.clear();
            AddPartPriceCostField.clear();
            AddPartInvField.clear();
            AddPartMin.clear();
            AddPartMax.clear();
            AddPartMachineText.clear();
            AddPartInHouseRadio.setSelected(false);
            AddPartOutSourcedRadio.setSelected(false);

            goToMainScreen(event);
        }
    }
}
