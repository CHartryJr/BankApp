package controllers.client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;

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
    private String buffer;
    
    void login(ActionEvent event) 
    {
        String results = "";
        String usr = tfUsername.getText();
        String passwd = tfPassword.getText();
        if (usr == null || passwd == null)
        {
            alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Make sure all fields are filled.");
            return;
        }
        try
        {
            connect();
            System.out.println(readData());
            buffer = String.format("SELECT USERNAME, PIN, ID FROM BANK_ACCOUNT WHERE BANK_ACCOUNT.USERNAME = '%s';", usr);
            writeData(buffer);
            results = readData();
            writeData("exit");
            if (results.isEmpty())
            {
                return;
            }
        } catch (Exception e)
        {
            alert = new Alert(AlertType.ERROR);
            alert.setContentText("No connection available: check connectivity");
            alert.show();
            e.printStackTrace();
        }
        String tokens[] = results.split("-");
        System.out.println(tokens);
    }

  
    void switchToCreate(ActionEvent event)
    {
        System.out.println("Hello, World!");
    }

    // main 
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        btnLogin.setOnAction(this::login);
        btnCAcct.setOnAction(this::switchToCreate);
    }
}
