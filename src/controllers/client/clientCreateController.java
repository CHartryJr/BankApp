package controllers.client;

import java.net.URL;
import java.util.ResourceBundle;
import controllers.GUIOperation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;


public class clientCreateController extends GUIOperation implements Initializable
{

    @FXML
    private Button btnCancel, btnSubmit;
    @FXML
    private TextField tfFNAME, tfLNAME, tfADDR, tfUSER, tfPIN1, tfPIN2;
    private String buffer;

    // submit new user's info to database
    void submit(ActionEvent event)
    {
        String FNAME = tfFNAME.getText();
        String LNAME = tfLNAME.getText();
        String ADDR = tfADDR.getText();
        String USER = tfUSER.getText();
        String PIN1 = tfPIN1.getText();
        String PIN2 = tfPIN2.getText();

        if (FNAME == null || LNAME == null || ADDR == null || USER == null | PIN1 == null || PIN2 == null)
        {
            alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Make sure all fields are filled.");
            return;
        }
        if (!PIN1.equals(PIN2))
        {
            return;
        }

        try
        {
            connect();
            System.out.println(readData());
            buffer = String.format( "BEGIN;~" +
            "INSERT INTO MEMBER(FNAME,LNAME,ADDRESS) VALUES ('%1$s', '%2$s', '%3$s')~" +
            "INSERT INTO BANK_ACCOUNT(DATE,PIN,USERNAME) VALUES ('%4$s','%5$s','%6$s')~" +
            "INSERT INTO OWNS(BANK_ACCOUNTID,MEMBERID) SELECT" +
            "(SELECT ID FROM BANK_ACCOUNT WHERE USERNAME = '%6$s') AS B_ID," +
            "(SELECT ID FROM MEMBER WHERE FNAME = '%1$s' AND LNAME = '%2$s') AS M_ID;", FNAME,LNAME,ADDR,getCurrentDateTime(),PIN1,USER); 
            writeData(buffer);
            readData();
            writeData("exit");
            switchScene(event, "client", "Client-Home.fxml");
        } catch (Exception e)
        {
            alert = new Alert(AlertType.ERROR);
            alert.setContentText("Connectivity error");
            alert.show();
            e.printStackTrace();
        }
    }

    // switch back to login screen
    public void cancel(ActionEvent event)
    {
       switchScene(event, "client", "Client-Home.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        btnCancel.setOnAction(this::cancel);
        btnSubmit.setOnAction(this::submit);
    }
}
