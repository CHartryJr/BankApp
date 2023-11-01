
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This program wil be used as a communication hub and will be the engine for query based operations
 */
public class BankServer extends Thread {

  private static AtomicInteger num = new AtomicInteger(0); // a count for the number of active threads change when done to boolean
  private static boolean running = true; //will be uses to shut down server.
  private ServerSocket serverSocket;
  private Socket clientSocket;
  private String threadName, query;
  private int identifier;

  BankServer(int i) {
    try {
      this.serverSocket = new ServerSocket(i);
      identifier = 0;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private BankServer(Socket clientSocket, int identifier) {
    this.clientSocket = clientSocket;
    this.identifier = identifier;
  }

  public void run() {
    if (identifier == 0) { //client connection thread
      try {
        while (running) {
          Socket sockIn = serverSocket.accept();
          BankServer connection = new BankServer(sockIn, num.incrementAndGet());
          System.out.println("A new connection has been made"); //debug only.
          connection.start();
        }
        serverSocket.close();
      } catch (Exception e) {
        try {
          serverSocket.close();
          System.exit(0);
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    } else { // messageing thread
      try {
        threadName = String.format("%d from %s ", identifier, this.getName());
        writeTransaction("Key"); // can send encryption key
        query = (String) readTransaction();
        this.wait(3000);
        query = getResults(query);
        this.wait(1000);
        writeTransaction(query);
        System.out.println(String.format("Competed Query From %s", threadName));
        clientSocket.close();
      } catch (Exception e) {
        try {
          clientSocket.close();
          System.exit(1);
        } catch (IOException e1) {
          System.out.println("Socket did not Close");
          System.exit(1);
        }
      }
    }
  }
   // method to get sql statement from db
  private synchronized String getResults(String query) {
    Connection con = getConnection();
    java.sql.Statement st;
    ResultSet rs;
    String result = "";
    try {
      st = con.createStatement();
      rs = st.executeQuery(query);
      while (rs.next()) {
        result += rs.getString("") + "," + rs.getString(result);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }
// method to read in messages
  private Object readTransaction() {
    try {
      ObjectInputStream input = new ObjectInputStream(
        clientSocket.getInputStream()
      );
      return input.readObject();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
// method to write out messages
  private void writeTransaction(Object o) {
    try {
      ObjectOutputStream output = new ObjectOutputStream(
        clientSocket.getOutputStream()
      );
      output.writeObject(o);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  // returns a db connection
  private Connection getConnection() {
    Connection con = null;
    try {
      con =
        DriverManager.getConnection(
          "jdbc:sqlite:" + getClass().getResource("../assets/Bank.db")
        );
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return con;
  }

  public static void main(String[] args) {
    System.out.println("Server has started");
    BankServer BS = new BankServer(5000);
    BS.run();
  }
}
