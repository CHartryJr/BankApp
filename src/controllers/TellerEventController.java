package controllers;
import java.io.*;
import java.net.*;
import java.util.ResourceBundle;
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

  String buffer,host = "localhost";// loop back until actual usage
  int port = 5001;
  Socket sock;
 

  @Override
  public void initialize(URL location, ResourceBundle resources) 
  {
    btnLogin.setOnAction(new EventHandler<ActionEvent>() 
    {//login button ACTION

      @Override
      public void handle(ActionEvent arg0)
      {
        if(LogIn(tfUsername.getText(),tfPassword.getText()))
        {
          textID.setText(tfUsername.getText());
         changeScene("CRUD.fxml");
        }
        else
        {
        System.err.println("LOGIN FAILED");
        }
      }
    });//login button end

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
      buffer += "* LOWER(NAME) FROM TELLER WHERE TELLER.USER = '" +userName+"';";
      writeData(buffer);
      buffer = readData();
      String tokens [] = buffer.split("|");
      if (tokens[3].equals(pwd))
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
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }catch(Exception e){}
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
