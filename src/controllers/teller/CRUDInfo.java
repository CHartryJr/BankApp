package controllers.teller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import controllers.GUIOperation;
import javafx.fxml.Initializable;

public class CRUDInfo extends GUIOperation implements Initializable
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

    private String currentUser;

    protected void setLoggedInName(String name)
    {
        currentUser = name;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initialize'");
    }
}