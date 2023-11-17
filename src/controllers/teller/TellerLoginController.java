package controllers.teller;

import java.io.*;
import java.net.*;
import java.util.ResourceBundle;
import controllers.clientCommunication;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class TellerLoginController extends clientCommunication implements Initializable  {

  @FXML
  private Button btnLogin;
  @FXML
  private TextField tfPassword,tfUsername;
  @FXML
  private Label failedLbl;
  private Stage st;
  private String user,pwd,buffer;// loop back until actual usage

  private void switchScene(ActionEvent event) {
    String currentDirectory = System.getProperty("user.dir");
    currentDirectory += "/assets/GUI/CRUD.fxml";
    try {
        Parent root = FXMLLoader.load(new File(currentDirectory).toURI().toURL());
        st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene sc = new Scene(root);
        st.setScene(sc); // Use setScene to set the new scene on the stage
        st.show();
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }catch(Exception e){
      e.printStackTrace();
    }
}

  private boolean LogIn(String userName,String pwd)
  {
    try
    {
      connect();
      System.out.println(readData());
      buffer = "SELECT LOWER(USER),PIN FROM TELLER WHERE TELLER.USER = '" +userName.toLowerCase()+"';";
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
    catch(Exception e)
    {
        buffer ="exit";
        writeData(buffer);
        buffer ="";
       e.printStackTrace();
       return false;
    }
        buffer ="exit";
        writeData(buffer);
        buffer ="";
        failedLbl.setText("login Failed");
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
            switchScene(arg0);
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
