package controllers.client;

import java.net.URL;
import java.util.ResourceBundle;

import controllers.GUIOperation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class clientInfoController extends GUIOperation implements Initializable
{

    @FXML
    private Button btnCheckings, btnSavings, btnSubmit;
    @FXML
    private ChoiceBox<?> cbTranType;
    @FXML
    private TableColumn<?, ?> colAmount;
    @FXML
    private TableColumn<?, ?> colID, colFrom, colDate, colOperation;
    @FXML
    private TextField tfAcct, tfAmount;
    @FXML
    private TableView<?> tvTable;
    @FXML
    private Text txtAcct, txtChecking, txtMemDate, txtSaving;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        System.out.println("handle");
    }

}
