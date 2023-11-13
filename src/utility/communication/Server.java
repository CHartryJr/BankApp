package utility.communication;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server implements Communications,Runnable 
{
    private static AtomicInteger index = new AtomicInteger(2);
    private static AtomicBoolean running = new AtomicBoolean(true);
    private ServerSocket serverSocket; 
    private Socket clientSocket;
    // private String = "localhost";
    private String hostname,buffer;
    private int port,identifier;
    

    public Server()//listener stopper
    {
      identifier = 0;
    }
    
    private Server(int port, String hostname)//listener
    {
      identifier =1;
      this.hostname = hostname;
      this.port = port;
    }
    
    private Server(int identifier,Socket clientSocket)//client action thread
    {
      this.identifier = identifier;
      this.clientSocket = clientSocket;
    }  

    public void startServer(int port, String hostname)
    {
      new Server(port )
    }
    public void stopServer(int port, String hostname)
    {
      running.set(false);
    }


    @Override
    public void run()
    {
      if(identifier == 0)//starter
       System.out.println("This thread is for activation or shuting down server only");
      if(identifier == 1)//listener
      {
          try
        {
          if(hostname == null)
          serverSocket = new ServerSocket(port,0,InetAddress.getByName(hostname));//for testing
          else
          serverSocket = new ServerSocket(port,0,InetAddress.getByName(hostname));
        // ServerSocket serverSocket = new ServerSocket(5001);
          System.out.println("Server has started");
          do
          {
            System.out.println("listening for connection");
            Socket clientSocketSession = serverSocket.accept();// will listen for client connection halts operation on main thread will stop until accepted
            new Server(index.incrementAndGet(),clientSocketSession).run();
            System.out.println("A new connection has been made"); //debug only.
          }while (running.get());
          serverSocket.close();
        }catch(Exception e)
        {
          e.printStackTrace();
        }
      }
      else//client action 
      {
        buffer ="";
        try 
        {
          //since running a while loop will hold up client actions we will on run in and out messages
          do
          {
          buffer=read();
            System.out.println(buffer);
            write("");
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
    }

    @Override
    public String read()
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

    @Override
    public void write(String data)
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
}
