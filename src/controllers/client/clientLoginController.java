package controllers.client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
import controllers.GUIOperation;
import java.io.File;
import java.io.IOException;
import javafx.scene.Node;



public class clientLoginController extends GUIOperation implements Initializable 
{
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private Button btnLogin, btnCAcct;
    private String buffer;
    
    void login(ActionEvent event) 
    {
        String results = "";
        String USER = tfUsername.getText();
        String PWD = tfPassword.getText();

        if (USER == null || PWD == null)
        {
            alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Make sure all fields are filled.");
            return;
        }
        try
        {
            connect();
            System.out.println(readData());
            buffer = String.format("SELECT USERNAME, PIN, BA.ID, CD.FNAME ||' '|| CD.LNAME AS NAME " +
                                   "FROM BANK_ACCOUNT AS BA JOIN CUSTOMER_DATA AS CD ON BA.ID = CD.ACC_NUM " +
                                   "WHERE BA.USERNAME = '%s';", USER);
            writeData(buffer);
            results = readData();
            writeData("exit");
            if (results.isEmpty())
            {
                alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("No data was found");
                alert.show();
                return;
            }
        } catch (Exception e)
        {
            alert = new Alert(AlertType.ERROR);
            alert.setContentText("No connection available: Check connectivity");
            alert.show();
            e.printStackTrace();
        }

        String tokens[] = results.split("~");
        if (tokens[1].equals(PWD))
        {
            try
            {
                String currentDirectory = System.getProperty("user.dir");
                currentDirectory += "/assets/GUI/client/Client-Info.fxml";
                FXMLLoader loader = new FXMLLoader(new File(currentDirectory).toURI().toURL());
                Parent root = loader.load();
                clientInfoController cic = loader.getController();
                cic.setLoggedInName(tokens[3]);
                // add setters and set




                Scene sc = new Scene(root);
                Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
                st.setScene(sc); // Use setScene to set the new scene on the stage
                st.show();
                
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            
        }
    }
  
    // switch to create new user screen
    void switchToCreate(ActionEvent event)
    {
        switchScene(event, "client", "Client-Create.fxml");
    }

    // main 
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        btnLogin.setOnAction(this::login);
        btnCAcct.setOnAction(this::switchToCreate);
    }
}
