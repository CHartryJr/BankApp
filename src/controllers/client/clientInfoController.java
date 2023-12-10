package controllers.client;

import java.net.URL;
import java.util.ResourceBundle;
import controllers.GUIOperation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
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
    private String buffer;

    // display checking info
    void checkings(ActionEvent event)
    {
        try
        {
            buffer = "";
        } catch (Exception e)
        {
            alert = new Alert(AlertType.ERROR);
            alert.setContentText("");
            alert.show();
            e.printStackTrace();
        }
    }

    // display savings info
    void savings(ActionEvent event)
    {
        try
        {
            buffer = "";
        } catch (Exception e)
        {
            alert = new Alert(AlertType.ERROR);
            alert.setContentText("");
            alert.show();
            e.printStackTrace();
        }
    }

    // submit changes (transfer, withdrawl, deposit) made to bank account
    void submit(ActionEvent event)
    {
        try
        {
            buffer = "";
        } catch (Exception e)
        {
            alert = new Alert(AlertType.ERROR);
            alert.setContentText("");
            alert.show();
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // btnCheckings.setOnAction(this::checkings);
        // btnSavings.setOnAction(this::savings);
        // btnSubmit.setOnAction(this::submit);
    }
    
}
