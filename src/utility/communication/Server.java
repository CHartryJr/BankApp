package utility.communicationnication;
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
import java.util.concurrent.atomic.AtomicBoolean;

public class Server implements Communications,Runnable 
{
    private static AtomicInteger index = new AtomicInteger(1);
    private static AtomicBoolean running = new AtomicBoolean(true);
    private Socket clientSocket;
    private String threadName, Buffer;
    private int identifier;

    Server()
    {
        try
    {
      ServerSocket serverSocket = new ServerSocket(5000);
      System.out.println("Server has started");
      do
      {
        System.out.println("listening for connection");
        Socket clientSocketSession = serverSocket.accept();// will listen for client connection halts operation on main thread will stop until accepted
        new Server().(clientSocketSession, index.incrementAndGet());
        System.out.println("A new connection has been made"); //debug only.
      }while (running);
      serverSocket.close();
    }catch(Exception e)
    {
      e.printStackTrace();
    }
    }

    @Override
    public void run()
    {
        try 
    {
      //since running a while loop will hold up client actions we will on run in and out messages
      threadName = String.format("%d from %s ", identifier);
      writeTransaction("Key"); // can send encryption key
      Buffer = readTransaction();
      //handshake

      writeTransaction("need query");
      Buffer = readTransaction();//get query
      this.wait(2000);
      this.wait(2000);
      writeTransaction(Buffer);//sent result out
      System.out.println(String.format("Competed Query From %s", threadName));
      index.decrementAndGet();
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

    @Override
    public String readTransaction()
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
    return data;);
    }

    @Override
    public void writeTransaction(String data)
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
    
}
