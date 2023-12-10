package controllers;

import java.io.*;
import java.net.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *  A used to control GUI Operations. These operations consist of read write and scene transition
 * @author Carl Hartry Jr
 */
public class GUIOperation 
{
  @FXML
  private Text textID;
  protected String currentUser,host = "localhost";// loop back until actual usage
  private int port = 5001;
  private Socket sock;
  protected Alert alert;
  private boolean connected = false;
  private LocalDateTime currentDateTime;
  private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:SS");
    
  protected String  getCurrentDateTime()
  {
    currentDateTime = LocalDateTime.now();
    return currentDateTime.format(dateFormat);
  } 

   /**
    * this will be used to read data provide from the server
     * @implNote Must Use Connect before usage of this method
     * @param data
     * @throws ConnectException
     */
   protected String readData() throws ConnectException
  {
    if(connected == false)
        throw new ConnectException(" Can not read must use Connect Method first");

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
    /**
     * this will be used to write to the server
     * @implNote Must Use Connect before usage of this method
     * @param data
     * @throws ConnectException
     */
    protected void writeData(String data) throws ConnectException
  {
    if(connected == false)
        throw new ConnectException("Can not write must use Connect Method first");
     
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
/**
 * used to establish a connection must be used first
 */
  protected void connect()
  {
    try
    {
      this.sock = (new Socket(InetAddress.getByName(host),port));
      connected = true;
    }
    catch (UnknownHostException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  protected  void switchScene(ActionEvent event,String type,String gui,String user) 
  {
    String currentDirectory = System.getProperty("user.dir");
    currentDirectory += String.format("/assets/GUI/%1$s/%2$s",type,gui);
    try {
        FXMLLoader loader = new FXMLLoader(new File(currentDirectory).toURI().toURL());
        Parent root = loader.load();
        if(!user.isEmpty())
        {
          GUIOperation cd = loader.getController();
          cd.setLoggedInName(user.toUpperCase());
        }
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene sc = new Scene(root);
        st.setScene(sc); // Use setScene to set the new scene on the stage
        st.show();
    } catch (IOException e) {
        e.printStackTrace();
    }catch(Exception e){
      e.printStackTrace();
    }
  }
  
  protected  void switchScene(ActionEvent event,String type,String gui) 
  {
    String currentDirectory = System.getProperty("user.dir");
    currentDirectory += String.format("/assets/GUI/%1$s/%2$s",type,gui);
    try {
        FXMLLoader loader = new FXMLLoader(new File(currentDirectory).toURI().toURL());
        Parent root = loader.load();
        GUIOperation cd = loader.getController();        
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene sc = new Scene(root);
        st.setScene(sc); // Use setScene to set the new scene on the stage
        st.show();
    } catch (IOException e) {
        e.printStackTrace();
    }catch(Exception e){
      e.printStackTrace();
    }
  }
  
  public void setLoggedInName(String name)
    {
        currentUser = name;
        textID.setText(name);
    }
  protected String getLoggedInName()
    {
        return currentUser;
    }
}