package controllers.client;

import java.net.URL;
import java.util.ResourceBundle;

import controllers.GUIOperation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class clientCreateController extends GUIOperation implements Initializable
{

    @FXML
    private Button btnCancel, btnSubmit;
    @FXML
    private TextField tfAddr, tfFNAME, tfLNAME, tfPIN1, tfPIN2, tfUser;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        
    }

}
