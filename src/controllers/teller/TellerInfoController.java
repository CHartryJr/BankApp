package controllers.teller;

import java.net.ConnectException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import controllers.GUIOperation;
import controllers.teller.TellerSearchController.Client;
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
         buffer = String.format("SELECT CD.FNAME||' '||CD.LNAME AS NAME,CD.DATE AS MEMBER_DATE,BD.AMOUNT,BD.DESCRIPTION FROM BANK_DATA AS BD JOIN CUSTOMER_DATA AS CD ON bd.ID = CD.ACC_NUM WHERE BD.ID  = '%s';",currentAccount);
         writeData(buffer);
         Boolean received = loadData(readData());
            if(!received)
            {
                alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("No content found for member");
                alert.show();
            }
            buffer  ="SELECT ID,AMOUNT,DATE FROM TRANSACTION_HISORY WHERE  ";
        writeData(buffer);

         writeData("exit"); 
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
                if(result[3].toLowerCase().equals("savings"))
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
       System.out.println("hello");
    }

    protected class Transaction
    {

    }
}