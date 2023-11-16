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
    private Text textID;
    @FXML
    private TextField tfSearch;
    @FXML
    private Button btnLogout, btnSearch;
     @FXML
    private ChoiceBox<String> sbSearch;
    @FXML
    private TableView<?> tvTable;
    @FXML
    private TableColumn<?, ?> colAccount;
    @FXML
    private TableColumn<?, ?> colAmount;
    @FXML
    private TableColumn<?, ?> colDate,colName;

    private String[] fields = {"All","Name","Account#"};
   
   
    

   







    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        sbSearch.setValue("Search By");
        sbSearch.getItems().addAll(fields);
    }
}
