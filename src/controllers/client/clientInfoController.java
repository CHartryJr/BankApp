package controllers.client;

import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.text.Text;

public class clientInfoController extends GUIOperation implements Initializable
{

    @FXML
    private Button btnChecking, btnSavings, btnSubmit, btnLogout;
    @FXML
    private ChoiceBox<String> cbTranType, cbTranFrom, cbTranTo;
    @FXML
    private TableColumn<Transaction, Float> colAmount;
    @FXML
    private TableColumn<Transaction, String> colFrom, colID, colDate, colOperation;
    @FXML
    private TextField tfAmount;
    @FXML
    private TableView<Transaction> tvTable;
    @FXML
    private Text txtAcct, txtSaving, txtChecking, txtMemDate;
    private String buffer, checkNum, saveNum,currentDisplay ="Checking";
    private ArrayList<Transaction> transactions;
    private ObservableList<Transaction> fromAccount  = FXCollections.observableArrayList();
    private String[] fields = {"Deposit","Withdrawal","Transfer"};
    private String[] types = {"Savings", "Checking"};

    protected void refresh()
    {
        try
        {
            connect();
            System.out.println(readData());
            String FNAME = currentUser.split(" ")[0],LNAME = currentUser.split(" ")[1];
            buffer = String.format("SELECT BD.ID,CD.DATE AS MEMBER_DATE,BD.AMOUNT,BD.DATE AS ACC_DATE,BD.DESCRIPTION FROM BANK_DATA AS BD " +
                                   "JOIN CUSTOMER_DATA AS CD ON BD.ID = CD.ACC_NUM " + 
                                   "WHERE CD.FNAME ='%1$s' AND CD.LNAME ='%2$s';",FNAME,LNAME);
            writeData(buffer);
            buffer = readData();
            String[] result, date;
            for(String row : buffer.split(","))
            {
                if(!row.isEmpty())
                {
                    result = row.split("~");
                    date = result[1].split(" ");
                    txtAcct.setText(result[0]);
                    txtMemDate.setText(date[0]);
                    if(result[4].toLowerCase().equals("savings"))
                    {
                        txtSaving.setText(result[2]);
                    }
                    else
                    {
                        txtChecking.setText(result[2]);
                    }
                }
                else
                {
                    break;
                }
            }
            //second write
            buffer = String.format("SELECT T.ID,T.AMOUNT,T.DATE,ACC.TYPE,T.DESCRIPTION,T.A_ID,T.EFFECTED_ACC FROM ALL_TRANS AS T " +
                                   "JOIN (SELECT A.ID,TYPE.DESCRIPTION AS TYPE FROM ACCOUNT A JOIN ACCOUNT_TYPE AS TYPE ON A.TYPE = TYPE.ID) " + 
                                   "AS ACC ON ACC.ID = T.A_ID WHERE T.B_ID = %s LIMIT 20;",txtAcct.getText());
            writeData(buffer);
            String results = readData();
            writeData("exit");
            transactions = new ArrayList<Transaction>();
            Transaction T;
            for(String row : results.split(","))
            {

                if(!row.isEmpty())
                {
                    result= row.split("~");
                    T = new Transaction(result[0],Float.parseFloat(result[1]),result[2],result[3],result[4],result[5],result[6]);
                    transactions.add(T); 
                    if(result[3].toLowerCase().equals("savings"))
                    {
                        saveNum = result[5];
                    }
                    else
                    {
                        checkNum = result[5];
                    }
                }
                else
                {
                    break;
                }
            }
          setDISPLAY();
        }
        catch(Exception e)
        {
            alert = new Alert(AlertType.ERROR);
            alert.setContentText("Critical error with loading data");
            alert.show();
            e.printStackTrace();
             try
			{
				writeData("exit");
			}
			catch (ConnectException e1)
			{
				e1.printStackTrace();
			}
        }
       
    }
 
    private void setDISPLAY()
    {
        
        colFrom.setText("Transactions From " + currentDisplay);
        for(Transaction t: transactions)
        {
            if(t.getType().toLowerCase().equals(currentDisplay.toLowerCase()))
            {
                fromAccount.add(t);
            }
        }
        colID.setCellValueFactory(new PropertyValueFactory<Transaction,String>("id"));
        colAmount.setCellValueFactory(new PropertyValueFactory<Transaction,Float>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<Transaction,String>("date"));
        colOperation.setCellValueFactory(new PropertyValueFactory<Transaction,String>("oper"));
        tvTable.setItems(fromAccount);
    }

    // display checking info
    private void checking(ActionEvent event)
    {
        currentDisplay ="Checking";
        fromAccount.clear();
        setDISPLAY();
    }

    // display savings info
    private void savings(ActionEvent event)
    {
        currentDisplay ="Savings";
        fromAccount.clear();
        setDISPLAY();
    }

    // submit changes (transfer, withdrawl, deposit) made to bank account
    private void submit(ActionEvent event)
    {
        if (tfAmount.getText().isEmpty())
        {
            alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Must enter in an amount");
            alert.show();
            return;
        }
        if (cbTranType.getValue().toLowerCase().equals("transfer"))
        {
            if (cbTranFrom.getValue().isEmpty() || cbTranTo.getValue().isEmpty() || cbTranFrom.getValue().equals(cbTranTo.getValue()))
            {
                alert = new Alert(AlertType.INFORMATION);
                alert.setContentText("Must enter in an account name");
                alert.show();
                return;
            }
                if (cbTranFrom.getValue().toLowerCase().equals("savings"))
                {
                    buffer = String.format("BEGIN;~" + 
                                       "INSERT INTO TRANSACTION_HISTORY(ACCOUNTID,AMOUNT,DATE,TYPE,EFFECTED_ACC) " + 
                                       "VALUES ('%1$s','-%2$s','%3$s','%4$s','%5$s');~" + 
                                       "INSERT INTO TRANSACTION_HISTORY(ACCOUNTID,AMOUNT,DATE,TYPE,EFFECTED_ACC) " + 
                                       "VALUES ('%5$s','%2$s','%3$s','%4$s','%1$s');", saveNum,tfAmount.getText(),getCurrentDateTime(),1,checkNum);
                }
                else
                {
                    buffer = String.format("BEGIN;~" + 
                                       "INSERT INTO TRANSACTION_HISTORY(ACCOUNTID,AMOUNT,DATE,TYPE,EFFECTED_ACC) " + 
                                       "VALUES ('%1$s','-%2$s','%3$s','%4$s','%5$s');~" + 
                                       "INSERT INTO TRANSACTION_HISTORY(ACCOUNTID,AMOUNT,DATE,TYPE,EFFECTED_ACC) " + 
                                       "VALUES ('%5$s','%2$s','%3$s','%4$s','%1$s');", checkNum,tfAmount.getText(),getCurrentDateTime(),1,saveNum);
                }
        }
        else
        {
            if (cbTranType.getValue().toLowerCase().equals("deposit"))
            {
                buffer = switch(cbTranTo.getValue().toLowerCase())
                {
                    case "savings" -> String.format("BEGIN;~" + 
                                                     "INSERT INTO TRANSACTION_HISTORY(ACCOUNTID,AMOUNT,DATE,TYPE,EFFECTED_ACC) " +
                                                     "VALUES ('%1$s','%2$s','%3$s','%4$s','%1$s');", saveNum,tfAmount.getText(),getCurrentDateTime(),3,checkNum);
                    case "checking" -> String.format("BEGIN;~" + 
                                                     "INSERT INTO TRANSACTION_HISTORY(ACCOUNTID,AMOUNT,DATE,TYPE,EFFECTED_ACC) " +
                                                     "VALUES ('%1$s','%2$s','%3$s','%4$s','%1$s');", checkNum,tfAmount.getText(),getCurrentDateTime(),3,saveNum);
                    default -> "";
                };
            }// TRANSACTION_HISTORY  TRANSACTION_HISORY
            else
            {
                buffer = switch(cbTranTo.getValue().toLowerCase())
                {
                    case "savings" -> String.format("BEGIN;~" + 
                                                     "INSERT INTO TRANSACTION_HISTORY(ACCOUNTID,AMOUNT,DATE,TYPE,EFFECTED_ACC)" +
                                                     " VALUES ('%1$s','-%2$s','%3$s','%4$s','%1$s');", saveNum,tfAmount.getText(),getCurrentDateTime(),2,checkNum);
                    case "checking" -> String.format("BEGIN;~" + 
                                                     "INSERT INTO TRANSACTION_HISTORY(ACCOUNTID,AMOUNT,DATE,TYPE,EFFECTED_ACC) " +
                                                     "VALUES ('%1$s','-%2$s','%3$s','%4$s','%1$s');", checkNum,tfAmount.getText(),getCurrentDateTime(),2,saveNum);
                    default -> "";
                };
            }
        }
        try
        {
            connect();
            System.out.println(readData());
            writeData(buffer);
            readData();
            writeData("exit");
        } 
        catch (Exception e)
        {
            alert = new Alert(AlertType.ERROR);
            alert.setContentText("Connectivity error");
            alert.show();
            e.printStackTrace();
        }
        refresh();
        fromAccount.clear();
        setDISPLAY();
    }

    // log out from current user back to the login screen
    private void logout(ActionEvent event)
    {
        switchScene(event, "client", "Client-Home.fxml");
    }

    private void release(ActionEvent e)
    {
        if(cbTranType.getValue().equals("Transfer"))
            cbTranFrom.setVisible(true);
        else
            cbTranFrom.setVisible(false);
        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        cbTranFrom.setVisible(false);
        cbTranType.setValue("Select Transaction Type");
        cbTranFrom.setValue("Transfer From");
        cbTranTo.setValue("Transfer To");
        cbTranType.getItems().addAll(fields);
        cbTranFrom.getItems().addAll(types);
        cbTranTo.getItems().addAll(types);
        cbTranType.setOnAction(this::release);
        btnChecking.setOnAction(this::checking);
        btnSavings.setOnAction(this::savings);
        btnSubmit.setOnAction(this::submit);
        btnLogout.setOnAction(this::logout);
    }
    
    protected class Transaction
    {
        private Float amount;
        private String date,type,oper,id,transAcc,effAcc;
            
        public Transaction(String id, Float amount, String date, String type, String oper, String transAcc, String effAcc)
        {
            this.effAcc = effAcc;
            this.transAcc = transAcc;
            this.id = id;
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

        /**
         * @return the id
         */
        public String getId()
        {
            return id;
        }

        /**
         * @return the transAcc
         */
        public String getTransAcc()
        {
            return transAcc;
        }

        /**
         * @return the effAcc
         */
        public String getEffAcc()
        {
            return effAcc;
        }
        
    }   
}
