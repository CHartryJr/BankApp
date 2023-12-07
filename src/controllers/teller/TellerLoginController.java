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

/**
 * 
 *  A GUI Operation.Used to operate Teller login
 * @author Carl Hartry Jr.
 */
public class TellerLoginController extends GUIOperation implements Initializable  {

  @FXML
  private Button btnLogin;
  @FXML
  private TextField tfPassword,tfUsername;
  @FXML
  private Label failedLbl;
  private String user,pwd,buffer;// loop back until actual usage
 

  private boolean LogIn(String userName,String pwd)
  {
    if(userName.isEmpty() | pwd.isEmpty())
      return false;
    Boolean loggedIn = false;
    try
    {
      connect();
      System.out.println(readData());
      buffer = "SELECT LOWER(USER),PIN FROM TELLER WHERE LOWER(TELLER.USER) = '" +userName.toLowerCase()+"';";
      writeData(buffer);
      buffer = readData();
      if(buffer.isEmpty())
        return false;

      String tokens [] = buffer.split("~");
      if (tokens[1].equals(pwd))
        loggedIn = true;
      buffer ="exit";
      writeData(buffer);
      buffer ="";
    }
    catch(ConnectException ce)
    {
      alert = new Alert(AlertType.ERROR);
      alert.setContentText("No connection Available check connectivity");
      alert.show();
      return false;
    }
    return loggedIn;
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
            switchScene(arg0,"teller","TellerSearch.fxml",user);
          }
          else
          {
            alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("You have entered the wrong credentials try again");
            alert.show();
          }
          
        }
        else
        {
          alert = new Alert(AlertType.ERROR);
          alert.setContentText("No connection available please check connectivity");
          alert.show();
        }
      }
    });//login button end
  }
}//EOF