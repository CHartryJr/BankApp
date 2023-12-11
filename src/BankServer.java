
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
  private static AtomicInteger index = new AtomicInteger(1);//used to keep a index on all threads
  private final static String HOST = "10.90.27.235"; //"localhost";
  private static Lock lock =  new ReentrantLock();
  private Connection con = null;
  public static void main(String[] args) 
  {
    try
    {
      ServerSocket serverSocket = new ServerSocket(5001,0,InetAddress.getByName(HOST));//for testing
     // ServerSocket serverSocket = new ServerSocket(5001);
      System.out.println("Server has started");
      while (true)
      {
        System.out.println("listening for connection");
        Socket clientSocketSession = serverSocket.accept();// will listen for client connection halts operation on main thread will stop until accepted
        new BankServer().newCommunication(clientSocketSession, index.incrementAndGet());
      }
      //serverSocket.close();//bad practice will implement stop thread later
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
  private void newCommunication(Socket s, int identifier)
  {
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
   new Communication(s,identifier,con).run();
  }
  
/**
 * nested class that will host all communications. The communication data will be sql statements. 
 */
private class Communication implements Runnable
{
  private Socket clientSocket;
  private String buffer;
  private int identifier;
  private Connection con;

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
    InetAddress clientIP = clientSocket.getInetAddress();
    System.out.println("A new connection has been made from "+ clientIP.getHostAddress()); //debug only.
    try 
    {
      writeTransaction(String.format(" You are Thread%d in the Que.I am ready for your requests ",identifier));
      while(true)
      {
          buffer = readTransaction();
          if(buffer == null | buffer.toLowerCase().equals("exit"))
            break;
          lock.lock();
          buffer =  buffer.indexOf("BEGIN;") == -1 ?getResults(buffer) : updateDB(buffer);
          Thread.sleep( 300);
          lock.unlock();
          writeTransaction(buffer);
      }
      System.out.println("Service completed");
      index.decrementAndGet();
      clientSocket.close();
      con.close();
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

    private String getResults(String query)
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
              //System.out.println("Column " + i + ": " + columnValue);//debug only
              unToken += columnValue+"~";
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
   
    private String updateDB(String transaction) 
    {
      String result = "";
      String [] queries = transaction.split("~");
      int updateCount = 0;
      try 
      {
          con.setAutoCommit(false);
  
          for (int i =1;i < queries.length; i++)
          {
              try (PreparedStatement pStmt = con.prepareStatement(queries[i]))
              {
                updateCount += pStmt.executeUpdate();
              } 
              catch (SQLException statementException) 
              {
                con.rollback();
                result += "Error executing statement: " + queries[i] + "\n";
                statementException.printStackTrace();
              }
          }
          result += "Computed " + updateCount +"queries";
          con.commit();
      } 
      catch (SQLException e) 
      {
          try 
          {
              con.rollback();
              e.printStackTrace();
              System.err.println("Error during transaction execution");
          } catch (SQLException rollbackException) 
          {
              rollbackException.printStackTrace();
          }
      } 
      finally 
      {
          try 
          {
              con.setAutoCommit(true);
          } catch (SQLException autoCommitException) 
          {
              autoCommitException.printStackTrace();
          }
      }
      return result;
  }
  }
}