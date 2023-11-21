package controllers.teller;

import java.net.*;
import java.util.ResourceBundle;
import controllers.GUIOperation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;



public class TellerLoginController extends GUIOperation implements Initializable  {

  @FXML
  private Button btnLogin;
  @FXML
  private TextField tfPassword,tfUsername;
  @FXML
  private Label failedLbl;
  private String user,pwd,buffer;// loop back until actual usage
  Alert alert;
 

  private boolean LogIn(String userName,String pwd)
  {
    try
    {
      connect();
      System.out.println(readData());
      buffer = "SELECT LOWER(USER),PIN FROM TELLER WHERE LOWER(TELLER.USER) = '" +userName.toLowerCase()+"';";
      writeData(buffer);
      buffer = readData();
      String tokens [] = buffer.split("-");
      if (tokens[1].equals(pwd))
      {
        buffer ="exit";
        writeData(buffer);
        buffer ="";
        return true;
      }
    }
    catch(ConnectException ce)
    {
      alert = new Alert(AlertType.WARNING);
      alert.setContentText("No connection Available check connectivity");
      alert.show();
    }
    catch(Exception e)
    {
        buffer ="exit";
        try
        {
          writeData(buffer);
        }
        catch (ConnectException e1)
        {
          e1.printStackTrace();
        }
        buffer ="";
       e.printStackTrace();
       return false;
    }
     return false;
  }

  /*
   * treat this method as the main file for gui
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) 
  {
    btnLogin.setOnAction(new EventHandler<ActionEvent>() 
    {//login button ACTION
      @Override
      public void handle(ActionEvent arg0)
      {
        user = tfUsername.getText();
        pwd = tfPassword.getText();
        if(user != null && pwd !=null)
        {
          if(LogIn(user,pwd))
          {
            switchScene(arg0,"teller","CRUD.fxml",user);
          }
        }
        else
        {
          failedLbl.setText("Critical Error");
        }
      }
    });//login button end
  }
}//EOF
