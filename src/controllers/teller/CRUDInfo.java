package controllers.teller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import controllers.clientCommunication;
import javafx.fxml.Initializable;

public class CRUDInfo extends clientCommunication implements Initializable
{
    @FXML
    private TableColumn<?, ?> ColAmount;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TextArea tfReason;

     @FXML
    private Button btnDelete,btnDeleteAccount, btnExit;

    @FXML
    private Text txtAccount,txtCheckings,txtMember,txtMemberDate,txtSavings;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initialize'");
    }
}