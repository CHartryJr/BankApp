package controllers.teller;

import java.io.File;
import java.net.ConnectException;
import java.net.URL;
import java.util.ResourceBundle;
import controllers.GUIOperation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
/**
 * This is a event driven class that will operate the CRUD Search portion of the program. The fmxl that it operatates on is CRUD.fxml
 * @author Carl Hartry Jr.
 */
public class TellerSearchController extends GUIOperation implements Initializable
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
    private  boolean open  = false;
    ObservableList<Client> clientList;
    


    protected  void toggleOpen()
    {
        open = !open;
    }
   /**
    * used to load the client data gathered to The Table View 
    * @param e
    */
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
            alert = new Alert(AlertType.ERROR);
            alert.setContentText("No connection Available check connectivity");
            alert.show();
        }
        
    } 
    
    private boolean loadData(String data)
    {
        if( data == null | data.isEmpty())
        return false;
        clientList = FXCollections.observableArrayList();
        String []result;
        Client c;
        for(String row : data.split(","))
        {
            
            if(!row.isEmpty())
            {
                result = row.split("~");
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

    private void setInfoPage(MouseEvent m)
    {
        if(clientList.isEmpty())
            return;
        if(open)
        {
            alert = new Alert(AlertType.WARNING);
            alert.setContentText("An Info Page Is Already Open");
            alert.show();
            return;
        }
        try
        {
            int index = tvTable.getSelectionModel().getSelectedIndex();
            String currentDirectory = System.getProperty("user.dir");
            currentDirectory += "/assets/GUI/teller/TellerInfo.fxml";
            FXMLLoader loader = new FXMLLoader(new File(currentDirectory).toURI().toURL());
            Parent Root1 = (Parent) loader.load();
            TellerInfoController tc = loader.getController();
            String account = colAccount.getCellData(index).toString();
            tc.setCurrentUser(this.currentUser);
            tc.setHomeReference(this);
            tc.setInfo(account);
            Stage Stage = new Stage();
            Stage.setTitle("Information Page");
            Stage.setScene(new Scene(Root1));
            tc.setExitOperation();
            Stage.show();
            
        }catch(Exception ie){
            ie.printStackTrace();
        }
        toggleOpen();
    }

    protected void refreshPage()
    {
         clientList.clear();
    }
    /**
     * logout controller
     * @param e
     */
     private void logout(ActionEvent e)
    {
        switchScene(e,"teller","TellerLogin.fxml","");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        sbSearch.setValue("Search Value");
        sbSearch.getItems().addAll(fields);
        btnSearch.setOnAction(this::loadClients);
        btnLogout.setOnAction(this::logout);
        tvTable.setOnMouseClicked(this::setInfoPage);
    }

    /**
     * Wrapper class used to represent client data
     */
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
         * @return the date
         */
        public String getDate()
        {
            return date;
        }
        /**
         * @return the account
         */
        public Integer getAccount()
        {
            return account;
        }
    }
}//EOF