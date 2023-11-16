package controllers.teller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class CRUDController implements Initializable
{
   @FXML
    private Button btnLogout;

    @FXML
    private Button btnSearch;

    @FXML
    private TableColumn<?, ?> colAccount;

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private ChoiceBox<?> sbSearch;

    @FXML
    private Text textID;

    @FXML
    private TextField tfSearch;

    @FXML
    private TableView<?> tvTable;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        
    }
}
