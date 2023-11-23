package controllers.teller;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.io.File;

import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import controllers.GUIOperation;
import javafx.fxml.Initializable;

public class TellerInfoController extends GUIOperation implements Initializable
{
    @FXML
    private TableColumn<Transaction,Integer > colAmount,colID;
    @FXML
    private TableColumn<Transaction, String > colDate,colType;
    @FXML
    private TextArea tfReason;
    @FXML
    private Button btnDelete,btnDeleteAccount,btnExit;
    @FXML
    private Text txtAccount,txtCheckings,txtMember,txtMemberDate,txtSavings;
    private String buffer,currentAccount;

    public void getInfo(String bankAccount)
    {
        currentAccount = bankAccount;
        try
        {
         connect();
         readData();//empty buffer
         buffer = String.format("SELECT CD.FNAME||' '||CD.LNAME AS NAME,CD.DATE AS MEMBER_DATE,BD.AMOUNT,BD.DATE AS ACC_DATE,BD.DESCRIPTION FROM BANK_DATA AS BD JOIN CUSTOMER_DATA AS CD ON BD.ID = CD.ACC_NUM WHERE BD.ID  = '%s';",currentAccount);
         writeData(buffer);
         Boolean received = loadData(readData());
            if(!received)
            {
                alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("No content found for member");
                alert.show();
            }   
         writeData("exit"); 
         buffer ="";
        }
        catch (ConnectException e)
        {
        
        }
    }

    /**
     * Uses to load data onto client bank information on  info screen
     */
    private Boolean loadData(String data)
    {
        if( data == null | data.equals("") )
            return false;
        String []result;
        txtAccount.setText(currentAccount);
        for(String row : data.split(","))
        {
            if(!row.isEmpty())
            {
                result = row.split("-");
                txtMember.setText(result[0]);
                txtMemberDate.setText(result[0]);
                if(result[4].toLowerCase().equals("savings"))
                {
                    txtSavings.setText(result[2]);
                }
                else
                {
                    txtCheckings.setText(result[2]);
                }
            }
            else
            {
                break;
            }
        }
        return true;
    }
    private void exitScene(ActionEvent e)//fix 
    {
        try
        {
            String currentDirectory = System.getProperty("user.dir");
            currentDirectory += "/assets/GUI/teller/TellerSearch.fxml";
            FXMLLoader loader = new FXMLLoader(new File(currentDirectory).toURI().toURL());
            TellerSearchController tsc = loader.getController();
            tsc.toggleOpen();
            Stage currentStage = (Stage) btnExit.getScene().getWindow();
            currentStage.close();
        }
        catch (MalformedURLException e1)
        {
            e1.printStackTrace();
        }
    }
    
    
    private Boolean loadTable(String data)
    {
        if( data == null | data.equals("") )
        return false;
        // ObservableList<Transaction> Transactions = FXCollections.observableArrayList();
        String []result;
        Transaction T;
        for(String row : data.split(","))
        {
            System.out.println(row);//debugging only
            if(!row.isEmpty())
            {
            result = row.split("-");
            //T = new Transaction(result[0], result[1],Integer.parseInt(result[2]));
            //Transactions.add(T);
            }
            else
            {
                break;
            }
        }
        // colAmount.setCellValueFactory(new PropertyValueFactory<Client,Integer>("account"));
        // colDate.setCellValueFactory(new PropertyValueFactory<Client,String>("date"));
        // tvTable.setItems(Transactions);
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
      btnExit.setOnAction(this::exitScene);
    }

    protected class Transaction
    {

    }
}