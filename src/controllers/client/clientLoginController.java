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
            buffer = String.format("SELECT USERNAME, PIN, BA.ID, CD.FNAME ||' '|| CD.LNAME AS NAME " +
                                   "FROM BANK_ACCOUNT AS BA JOIN CUSTOMER_DATA AS CD ON BA.ID = CD.ACC_NUM " +
                                   "WHERE BA.USERNAME = '%s';", usr);
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
        if (tokens[1].equals(passwd))
        {
            switchScene(event, "client", "Client-Info.fxml", tokens[3]);
        }
    }
  
    void switchToCreate(ActionEvent event)
    {
        System.out.println("Hello, World!");
        switchScene(event, "client", "Client-Create.fxml", "-");
    }

    // main 
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        btnLogin.setOnAction(this::login);
        btnCAcct.setOnAction(this::switchToCreate);
    }
}
