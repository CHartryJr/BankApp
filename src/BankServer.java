
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @apiNote This program wil be used as a communication hub and will be the engine for query based operations
 * @author Carl Hartry jr
 */
public class BankServer
{
  private static boolean running = true; //will be uses to shut down server.
  private static AtomicInteger index = new AtomicInteger(1);//used to keep a index on all threads
  private final static String HOST = "localhost";
  private static Lock lock =  new ReentrantLock();
  
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
      }
       catch (SQLException e) 
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
      }while (running);//bad practice will implement stop thread later
      serverSocket.close();
      con.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
/**
 * 
 * @param s
 * @param identifier
 * @param con
 *  will call a new communication per client 
 */
  private void newCommunication(Socket s, int identifier,Connection con)
  {
   new Communication(s,identifier,con).run();
  }
  
/**
 * nested class that will host all communications. The communication data will be sql statements. 
 */
private class Communication implements Runnable
{
  private Socket clientSocket;
  String buffer;
  private int identifier;
  
  Connection con;

  private Communication(Socket clientSocket, int identifier, Connection con)
  {
    this.clientSocket = clientSocket;
    this.identifier = identifier-1;
    this.con = con;
  }
 /*
  * read then write pattern only. The job of the communications class is to send ResultSets in string form back from queries given by clients
  */
  public void run()// action done during thread
  {
    try 
    {
      writeTransaction(String.format(" You are Thread%d in the Que.I am ready for your requests ",identifier));
      do
      {
          buffer = readTransaction();
          lock.lock();
          buffer = getResults(buffer);
          Thread.sleep( 500);
          lock.unlock();
          writeTransaction(buffer);
          buffer = readTransaction();
      }while(buffer != null & !buffer.toLowerCase().equals("exit"));
      System.out.println("Service completed");
      index.decrementAndGet();
      clientSocket.close();
    } 
    catch(NullPointerException e)
    {
      System.out.println("Host Disconnected");
      try
      {
        index.decrementAndGet();
        clientSocket.close();
      }
      catch (IOException e1)
      {
        e1.printStackTrace();
      }
    }
    catch (Exception e) 
    {
     e.printStackTrace();
     System.exit(1);
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
      BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      data = input.readLine();
      return data;
    } 
    catch (Exception e) 
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
    }
     catch (Exception e)
    {
      e.printStackTrace();
    }
  }
    private String getResults(String query)///fix parsing data
    {
      String result = "";
      try( Statement st = con.createStatement(); ResultSet rs = st.executeQuery(query))
      {
        while (rs.next()) 
        {
          String unToken ="";
          int columnCount = rs.getMetaData().getColumnCount();
          for (int i = 1; i <= columnCount; i++) 
          {
              String columnValue = rs.getString(i);
              System.out.println("Column " + i + ": " + columnValue);
              unToken += columnValue+"-";
          }
          result+=unToken+",";
        }
        st.close();
        rs.close();
      } 
      catch (SQLException e)
      {
        e.printStackTrace();
      }
      return result;
    }
  }
}