package controllers.teller;

import java.net.ConnectException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import controllers.GUIOperation;
import javafx.fxml.Initializable;

public class TellerInfoController extends GUIOperation implements Initializable
{
    @FXML
    private TableColumn<Transaction,Float> colAmount;
    @FXML
    private TableColumn<Transaction, String > colDate,colType,colOper;
    @FXML
    private TableView <Transaction> tvTable;
    @FXML
    private TextArea tfReason;
    @FXML
    private Button btnDelete,btnDeleteAccount,btnExit;
    @FXML
    private Text txtAccount,txtCheckings,txtMember,txtMemberDate,txtSavings;
    private String buffer,currentAccount;
    private TellerSearchController tsc;
    
    protected  void setInfo(String bankAccount)
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

            buffer = String.format("SELECT T.AMOUNT,T.DATE,ACC.TYPE,T.DESCRIPTION FROM ALL_TRANS AS T JOIN (SELECT A.ID,TYPE.DESCRIPTION AS TYPE FROM ACCOUNT A JOIN ACCOUNT_TYPE AS TYPE ON A.TYPE = TYPE.ID ) AS ACC ON ACC.ID = T.A_ID WHERE T.B_ID = %s;",currentAccount);
            writeData(buffer);
            received = loadTable(readData());
            if(!received)
            {
                alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("No content found for table");
                alert.show();
            }   
            writeData("exit"); 
            buffer ="";
        }
        catch (ConnectException e)
        {
            alert = new Alert(AlertType.ERROR);
            alert.setContentText("No connection Available check connectivity");
            alert.show();
        }
    }
    
    protected void setExitOperation()
    {
        Stage currentStage = (Stage) btnExit.getScene().getWindow();
        currentStage.setOnHiding(this::onClose);
    }

    protected void setHomeReference(TellerSearchController tsc)
    {
         this.tsc = tsc;
    }

    private void deleteUser(ActionEvent e)
    {
        alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText("Confirm with customer about deletion");
        boolean confirm =  alert.showAndWait().get() == ButtonType.OK ?true:false;
        if(!confirm)
            return;
        alert.setContentText("Once this action is done \nThe account  will permanently deleted is this ok?");
        confirm =  alert.showAndWait().get() == ButtonType.OK ?true:false;
        if(!confirm)
            return;
       
        try
        {
            connect();
            readData();
            buffer = String.format("BEGIN;-" + //since transaction fails i will make my own transaction
                    "DELETE FROM MEMBER WHERE ID  = (SELECT ID FROM CUSTOMER_DATA WHERE ACC_NUM = %1$s);-" +
                    "DELETE FROM ACCOUNT WHERE ID IN (SELECT TYPE FROM ASSOCIATED WHERE BANK_ACCOUNTID = %1$s);-" +
                    "DELETE FROM BANK_ACCOUNT  WHERE ID  = %1$s;-" + 
                    "DELETE FROM OWNS  WHERE BANK_ACCOUNTID =  %1$s;-" + 
                    "DELETE FROM ASSOCIATED  WHERE BANK_ACCOUNTID =  %1$s;-" + 
                    "DELETE FROM BANK_ACCOUNT WHERE ID = %1$s;",txtAccount.getText());
            writeData(buffer);
            System.out.println(readData());
            buffer = "exit";
            writeData(buffer);
            tsc.refreshPage();
            exitScene(e);
        }
        catch(ConnectException ce)
        {}
    }
    /**
     * Uses to load data onto client bank information on  info screen
     */
    private Boolean loadData(String data)
    {
        if( data == null )
            return false;
        String []result,date;
        txtAccount.setText(currentAccount);
        for(String row : data.split(","))
        {
            if(!row.isEmpty())
            {
                result = row.split("-");
                date = result[1].split(" ");
                txtMember.setText(result[0]);
                txtMemberDate.setText(date[0]);
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

    private void exitScene(ActionEvent e)
    {  
        Stage currentStage = (Stage) btnExit.getScene().getWindow();
        currentStage.close();
    }

    private void onClose(WindowEvent e)
    {
        tsc.toggleOpen();
    }

    private Boolean loadTable(String data)
    {
        if( data == null | data.equals("") )
        return false;
        ObservableList<Transaction> Transactions = FXCollections.observableArrayList();
        String []result;
        Transaction T;
        for(String row : data.split(","))
        {
            System.out.println(row);//debugging only
            if(!row.isEmpty())
            {
                result = row.split("-");
                T = new Transaction(Float.parseFloat(result[0]),result[1],result[2],result[3]);
                Transactions.add(T);
            }
            else
            {
                break;
            }
        }
        colAmount.setCellValueFactory(new PropertyValueFactory<Transaction,Float>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<Transaction,String>("date"));
        colType.setCellValueFactory(new PropertyValueFactory<Transaction,String>("type"));
        colOper.setCellValueFactory(new PropertyValueFactory<Transaction,String>("oper"));
        tvTable.setItems(Transactions);
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        btnExit.setOnAction(this::exitScene);
        btnDeleteAccount.setOnAction(this::deleteUser);
    }

    protected class Transaction
    {
        private Float amount;
        private String date,type,oper;
            
        public Transaction(Float amount, String date, String type,String oper)
        {
            this.amount = amount;
            this.date = date;
            this.type = type;
            this.oper = oper;
        }

        /**
         * @return the oper
         */
        public String getOper()
        {
            return oper;
        }

        /**
         * @return the amount
         */
        public float getAmount()
        {
            return amount;
        }

        /**
         * @return the date
         */
        public String getDate()
        {
            return date;
        }

        /**
         * @return the type
         */
        public String getType()
        {
            return type;
        }
    }
}