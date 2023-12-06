import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class will act as the client
 */
public class OnlineBank extends Application 
{

  @Override
  public void start(Stage primaryStage) throws Exception 
  {
    String currentDirectory = System.getProperty("user.dir");
    currentDirectory += "/assets/GUI/client/Client-Home.fxml";
    Parent root = FXMLLoader.load(new File(currentDirectory).toURI().toURL());
    primaryStage.setTitle("Welcome GCSU CREDIT UNION");
    primaryStage.setScene(new Scene(root, 600, 600));
    primaryStage.show();
  }

  public static void main(String[] args) 
  {
    launch(args);
  }
}
