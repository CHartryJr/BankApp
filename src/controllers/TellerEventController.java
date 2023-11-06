package controllers;
import java.io.*;
import java.net.*;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.junit.jupiter.params.shadow.com.univocity.parsers.common.record.Record;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class TellerEventController implements Initializable {

  @FXML
  Button btnInsert,btnDelete, btnUpdate,btnInspect,btnLogin,btnLogout;

  @FXML
  TextField tfName, tfAccount,tfPassword,tfUsername;
  @FXML
  Text textID;
  @FXML
  TableView<Record> data;
  @FXML
  TableColumn<Record, String> colName;
  @FXML
  TableColumn<Record, Integer> colID, accAmount, colAccount;

  String ip = "127.0.0.1";// loop back until actual usage
  final int PORT = 5001;
 

  @Override
  public void initialize(URL location, ResourceBundle resources) 
  {
    btnLogin.setOnAction(new EventHandler<ActionEvent>() {//login button ACTION

      @Override
      public void handle(ActionEvent arg0)
      {
        if(LogIn(tfUsername.getText(),tfPassword.getText()))
        {
         changeScene("CRUD.fxml");
         textID.setText(tfUsername.getText());
        }
        else
        {

        }
      }
      
    });

  }
  private boolean LogIn(String userName,String pwd)
  {
    String result, query = "SELECT * NAME FROM TELLER WHERE TELLER.USER ='" +userName +"';";
    result = requestData(query);
    String tokens [] = result.split("|");
    if (tokens[3].equals(pwd))
      return true;
    else
      return false;
  }

/**
 * used to send and receive data from the server every action needs data so e
 * send and receive is in one function call.
 * @param data
 * @return Sting representation of database data.
 */
  private String requestData(String data) 
  {
    String result =null;
    try
    {
      Socket sock = new Socket(ip,PORT);
        readData(sock);
        writeData(data, sock);
        result = readData(sock);

    }
    catch (UnknownHostException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return result;
  }

  private String readData(Socket sock) 
  {
    String data = null;
    try 
    {
      Scanner input = new Scanner(sock.getInputStream());
      while(input.hasNextLine())
      {
        data += input.nextLine();
      }
      input.close();
      return input.nextLine();
    } catch (Exception e) 
    {
      e.printStackTrace();
    }
    return data;
  }
// method to write out messages
  private void writeData(String data,Socket sock) 
  {
    try 
    {
      PrintWriter output = new PrintWriter(sock.getOutputStream());
      output.write(data);
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
    private void changeScene(String FXMLFileName)
    {
      String currentDirectory = System.getProperty("user.dir");
    currentDirectory += "/assets/GUI/"+FXMLFileName;
    try
    {
      FXMLLoader root = FXMLLoader.load(new File(currentDirectory).toURI().toURL());
      root.load();
    }
    catch (MalformedURLException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    }


}
