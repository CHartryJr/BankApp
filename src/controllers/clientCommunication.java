package controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Class used to make a uniform button communications
 */
public class clientCommunication 
{
  private String host = "localhost";// loop back until actual usage
  private int port = 5001;
  private Socket sock;
  private boolean connected = false;
    
   protected String readData() 
  {
    if(connected == false)
      try
      {
        throw new ConnectException("Must use Connect Method first");
      }
      catch (ConnectException e)
      {
        e.printStackTrace();
      }

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

    protected void writeData(String data) 
  {
    if(connected == false)
      try
      {
        throw new ConnectException("Must use Connect Method first");
      }
      catch (ConnectException e)
      {
        e.printStackTrace();
      }
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
}
