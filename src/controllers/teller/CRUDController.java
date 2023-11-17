package controllers.teller;

import java.net.URL;
import java.util.ResourceBundle;

import controllers.clientCommunication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class CRUDController extends clientCommunication implements Initializable
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
    private TableView<Client> tvTable;
    @FXML
    private TableColumn<Client, ?> colAccount;
    @FXML
    private TableColumn<Client, ?> colAmount;
    @FXML
    private TableColumn<Client, ?> colDate,colName;
    private String buffer;
    private String[] fields = {"FirstName","LastName","Account"};
   
   
   private void loadClients(ActionEvent e)
   {
        connect();
        System.out.println(readData());
        String value = tfSearch.getText();
        buffer = switch(sbSearch.getValue().toLowerCase()) 
        {
            case "firstname" -> "SELECT AH.FNAME||' '||AH.LNAME AS NAME,AH.DATE,BA.ID FROM ACCOUNT_HOLDER AS AH JOIN BANK_ACCOUNT AS BA ON BA.OWNER = AH.ID WHERE AH.FNAME ="+value.toLowerCase()+"';" ;
            case "lastname" -> "SELECT AH.FNAME||' '||AH.LNAME AS NAME,AH.DATE,BA.ID FROM ACCOUNT_HOLDER AS AH JOIN BANK_ACCOUNT AS BA ON BA.OWNER = AH.ID WHERE AH.LNAME ="+value.toLowerCase()+"';" ;
            case "account" -> "SELECT AH.FNAME||' '||AH.LNAME AS NAME,AH.DATE,BA.ID FROM ACCOUNT_HOLDER AS AH JOIN BANK_ACCOUNT AS BA ON BA.OWNER = AH.ID WHERE BA.ID ="+value.toLowerCase()+"';" ;
            default -> null;     
        };
        writeData(buffer);
        loadData(readData());
        writeData("exit");

    } 
   
    private void loadData(String data)
    {

    }







    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        sbSearch.setValue("Search Value");
        sbSearch.getItems().addAll(fields);
        btnSearch.setOnAction(this::loadClients);
    }


     public record Client()
    {

    }
}
