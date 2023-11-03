
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This program wil be used as a communication hub and will be the engine for query based operations
 */
public class BankServer
{
  private static boolean running = true; //will be uses to shut down server.
  private static AtomicInteger index = new AtomicInteger(1);//used to keep a index on all threads
  
  /**
   * This method will be used to listen can create new communications for server.
   * @param s
   * @param identifier
   */
  private void newCommunication(Socket s, int identifier)
  {
   new Communication(s,identifier).start();
  }
  public static void main(String[] args) 
  {
    try
    {
      ServerSocket serverSocket = new ServerSocket(5000);
      System.out.println("Server has started");
      while (running) 
      {
        System.out.println("listening for connection");
        Socket clientSocketSession = serverSocket.accept();// will listen for client connection halts operation on main thread will stop until accepted
        new BankServer().newCommunication(clientSocketSession, index.incrementAndGet());
        System.out.println("A new connection has been made"); //debug only.
      }
      serverSocket.close();
    }catch(Exception e)
    {
      e.printStackTrace();
    }
  }

/**
 * nested class that will host all communications. The communication data will be sql statements. 
 */
private class Communication extends Thread
{
  private Socket clientSocket;
  private String threadName, Buffer;
  private int identifier;
  private Communication(Socket clientSocket, int identifier)
  {
    this.clientSocket = clientSocket;
    this.identifier = identifier;
  }
  public void run()// action done during thread
  {
    try 
    {
      //since running a while loop will hold up client actions we will on run in and out messages
      threadName = String.format("%d from %s ", identifier, this.getName());
      writeTransaction("Key"); // can send encryption key
      Buffer = readTransaction();
      //handshake


      this.wait(3000);
      Buffer = getResults(Buffer);//parse query
      this.wait(1000);
      writeTransaction(Buffer);//it out
      System.out.println(String.format("Competed Query From %s", threadName));
      index.getAndDecrement();
      clientSocket.close();
    } catch (Exception e) 
    {
      try 
      {
        clientSocket.close();
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
      Scanner input = new Scanner(clientSocket.getInputStream());
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
  private void writeTransaction(String data) 
  {
    try 
    {
      PrintWriter output = new PrintWriter(clientSocket.getOutputStream());
      output.write(data);
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  // returns a db connection
    private Connection getConnection() 
    {
      Connection con = null;
      try 
      {
        con =DriverManager.getConnection("jdbc:sqlite:" + getClass().getResource("../assets/Data/Bank.db"));
      } catch (SQLException e) 
      {
        e.printStackTrace();
      }
      return con;
    }

    private synchronized String getResults(String query)
    {
      Connection con = getConnection();
      java.sql.Statement st;
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
