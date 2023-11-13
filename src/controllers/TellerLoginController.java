package controllers;

import java.io.*;
import java.net.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class TellerLoginController implements Initializable {

  
  @FXML
  Button btnLogin,btnLogout;
  @FXML
  TextField tfPassword,tfUsername;
  @FXML
   Text txtFailed;
  private Stage st;
  private String user,pwd,buffer,host = "localhost";// loop back until actual usage
  private int port = 5001;
  private Socket sock;

  @Override
  public void initialize(URL location, ResourceBundle resources) 
  {
    btnLogin.setOnAction(new EventHandler<ActionEvent>() 
    {//login button ACTION

      @Override
      public void handle(ActionEvent arg0)
      {
        user =tfUsername.getText();
        pwd = tfPassword.getText();
        if(user != null && pwd !=null)
        if(LogIn(user,pwd))
        {
         switchScene(arg0);
        }
        else
        {
          txtFailed.setText("Login failed try again");
        }
      }
    });//login button end

  }
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
      sock = new Socket(InetAddress.getByName(host),port);
      System.out.println(readData());
      buffer = "query";
      writeData(buffer);
      buffer = readData();
      buffer += "LOWER(USER),PIN FROM TELLER WHERE TELLER.USER = '" +userName.toLowerCase()+"';";
      writeData(buffer);
      buffer = readData();
      String tokens [] = buffer.split("-");
      if (tokens[1].equals(pwd))
      {
        buffer ="exit";
        writeData(buffer);
        buffer ="";
        sock.close();
        return true;
      }
    }
    catch (UnknownHostException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }catch(Exception e)
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
     return false;
  }

  private String readData() 
  {
    String data = null;
    try 
    {
      
      BufferedReader input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
      data = input.readLine();
      return data;
    } catch (Exception e) 
    {
      e.printStackTrace();
    }
    return data;
  }
// method to write out messages
  private void writeData(String data) 
  {
    try 
    {
      
      BufferedWriter output = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
      output.write(data);
      output.newLine();
      output.flush();
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }

    
}
