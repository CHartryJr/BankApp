package controllers.client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

import java.net.URL;
import java.util.ResourceBundle;

import controllers.GUIOperation;

public class clientLoginController extends GUIOperation implements Initializable 
{
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private Button btnLogin;
    @FXML
    private ToggleButton btnCAcct;
    
    void login(ActionEvent event) 
    {

    }

  
    void switchToCreate(ActionEvent event)
    {

    }

    // main 
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        btnLogin.setOnAction(this::login);
        btnCAcct.setOnAction(this::switchToCreate);
    }
}
