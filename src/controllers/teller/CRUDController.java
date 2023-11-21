package controllers.teller;

import java.net.ConnectException;
import java.net.URL;
import java.util.ResourceBundle;
import controllers.GUIOperation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class CRUDController extends GUIOperation implements Initializable
{
    
    @FXML
    private TextField tfSearch;
    @FXML
    private Button btnLogout, btnSearch;
     @FXML
    private ChoiceBox<String> sbSearch;
    @FXML
    private TableView<Client> tvTable;
    @FXML
    private TableColumn<Client,Integer> colAccount;
    @FXML
    private TableColumn<Client,String> colDate,colName;
    private String buffer;
    private String[] fields = {"FirstName","LastName","Account"};
    Alert alert;
   
   private void loadClients(ActionEvent e)
   {
        String value = tfSearch.getText();
        if(value.isEmpty())
        {
            alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Must have a value for Searching");
            alert.show();
            return;
        }

        buffer = switch(sbSearch.getValue().toLowerCase())
        {
            case "firstname" -> "SELECT FNAME||' '||LNAME AS NAME,DATE,ACC_NUM FROM CUSTOMER_DATA WHERE LOWER(FNAME) ='"+value.toLowerCase()+"';" ;
            case "lastname" -> "SELECT FNAME||' '||LNAME AS NAME,DATE,ACC_NUM FROM CUSTOMER_DATA WHERE LOWER(LNAME) ='"+value.toLowerCase()+"';" ;
            case "account" -> "SELECT FNAME||' '||LNAME AS NAME,DATE,ACC_NUM FROM CUSTOMER_DATA WHERE ACC_NUM ='"+value.toLowerCase()+"';" ;
            default -> null;     
        };

         if(buffer == null)
        {
            alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Use A Correct Search Field");
            alert.show();
            return;
        }
        try
        {
            connect();
            System.out.println(readData());
            writeData(buffer);
            Boolean received = loadData(readData());
            if(!received)
            {
                alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("No content found");
                alert.show();
            }
            writeData("exit");
        }
        catch (ConnectException e1)
        {
            e1.printStackTrace();
        }
        
    } 
   
    private boolean loadData(String data)
    {
        if( data == null | data.equals("") )
        return false;
        ObservableList<Client> clientList = FXCollections.observableArrayList();
        String []result;
        Client c;
        for(String row : data.split(","))
        {
            
            if(!row.isEmpty())
            {
            result = row.split("-");
            c = new Client(result[0], result[1],Integer.parseInt(result[2]));
            clientList.add(c);
            }
            else
            {
                break;
            }
        }
        colAccount.setCellValueFactory(new PropertyValueFactory<Client,Integer>("account"));
        colDate.setCellValueFactory(new PropertyValueFactory<Client,String>("date"));
        colName.setCellValueFactory(new PropertyValueFactory<Client,String>("name"));
        tvTable.setItems(clientList);
        return true;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        sbSearch.setValue("Search Value");
        sbSearch.getItems().addAll(fields);
        btnSearch.setOnAction(this::loadClients);
    }

    protected class Client
    {
        protected String name,date;
        protected Integer account;  
        public  Client(String name, String date, Integer account)
        {
            this.account = account;
            this.date = date;
            this.name  = name;
        }
            /**
             * @return the name
             */
            public String getName()
            {
                return name;
            }
            /**
             * @param name the name to set
             */
            public void setName(String name)
            {
                this.name = name;
            }
            /**
             * @return the date
             */
            public String getDate()
            {
                return date;
            }
            /**
             * @param date the date to set
             */
            public void setDate(String date)
            {
                this.date = date;
            }
            /**
             * @return the account
             */
            public Integer getAccount()
            {
                return account;
            }
            /**
             * @param account the account to set
             */
            public void setAccount(Integer account)
            {
                this.account = account;
            }
        
    }
}
