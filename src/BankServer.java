
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This program wil be used as a communication hub and will be the engine for query based operations
 */
public class BankServer
{
  private static boolean running = true; //will be uses to shut down server.
  private static AtomicInteger index = new AtomicInteger(1);//used to keep a index on all threads
  private final static String HOST = "localhost";
  
  /**
   * This method will be used to listen can create new communications for server.
   * @param s
   * @param identifier
   */
  
  public static void main(String[] args) 
  {
    try
    {
      Connection con = null;
      try 
      {
        String cd = System.getProperty("user.dir");
          cd += "/assets/Data/Bank.db";
        con = DriverManager.getConnection("jdbc:sqlite:" +cd);
      } catch (SQLException e) 
      {
        e.printStackTrace();
      }
      System.out.println("DataBase is now up");
      ServerSocket serverSocket = new ServerSocket(5001,0,InetAddress.getByName(HOST));//for testing
     // ServerSocket serverSocket = new ServerSocket(5001);
      System.out.println("Server has started");
      do
      {
        System.out.println("listening for connection");
        Socket clientSocketSession = serverSocket.accept();// will listen for client connection halts operation on main thread will stop until accepted
        new BankServer().newCommunication(clientSocketSession, index.incrementAndGet(),con);
        System.out.println("A new connection has been made"); //debug only.
      }while (running);
      serverSocket.close();
    }catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  private void newCommunication(Socket s, int identifier,Connection con)
  {
   new Communication(s,identifier,con).start();
  }
  
/**
 * nested class that will host all communications. The communication data will be sql statements. 
 */
private class Communication extends Thread
{
  private Socket clientSocket;
  private String threadName, buffer;
  private int identifier;
  Connection con;
  private Communication(Socket clientSocket, int identifier, Connection con)
  {
    this.clientSocket = clientSocket;
    this.identifier = identifier;
    this.con = con;
  }
  public void run()// action done during thread
  {
    try 
    {
      //since running a while loop will hold up client actions we will on run in and out messages
      threadName = String.format("%d from %s ", identifier, this.getName());
      writeTransaction("You are active send request");
      do
      {
       buffer=readTransaction();
       if(buffer.toLowerCase().equals("key"))
       {
        buffer = "exit";//will use until implemented
       }
       else if (buffer.toLowerCase().equals("query"))
       {
          writeTransaction("SELECT ");
          buffer = readTransaction();
          buffer = getResults(buffer);
          writeTransaction(buffer);
          buffer = readTransaction();
       }

      }while(!buffer.toLowerCase().equals("exit"));
      clientSocket.close();
    } catch (Exception e) 
    {
      try 
      {
        clientSocket.close();
        e.printStackTrace();
        System.exit(1);
      } catch (IOException e1) 
      {
        System.out.println("Socket did not Close");
        System.exit(1);
      }
    }
  }
  /**
   * method  read data recieved from user.
   * @return void
   */
  private String readTransaction() 
  {
    String data = null;
    try 
    {
      BufferedReader input = new BufferedReader(new InputStreamReader( clientSocket.getInputStream()));
      data = input.readLine();
      return data;
    } catch (Exception e) 
    {
      e.printStackTrace();
    }
    return data;
  }
// method to write out messages
  private void writeTransaction(String data) 
  {
    try 
    {
      BufferedWriter output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
      output.write(data);
      output.newLine();
      output.flush();
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
    private synchronized String getResults(String query)
    {
      Statement st;
      ResultSet rs;
      String result = "";
      try 
      {
        st = con.createStatement();
        rs = st.executeQuery(query);
        while (rs.next()) 
        {
          result += rs.getString("") + "," + rs.getString(result);
        }
      } catch (SQLException e)
      {
        e.printStackTrace();
      }
      return result;
    }
  }
}