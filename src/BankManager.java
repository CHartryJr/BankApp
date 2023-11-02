import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BankManager extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    String currentDirectory = System.getProperty("user.dir");
    currentDirectory += "/assets/GUI/CRUD.fxml";
    Parent root = FXMLLoader.load(new File(currentDirectory).toURI().toURL());
    String css = this.getClass().getResource("Global.css").toExternalForm();
    primaryStage.setTitle("GCSU CREDIT UNION OPERATIONS");
    Scene sc = new Scene(root);
    sc.getStylesheets().add(css);
    primaryStage.setScene(sc);
    
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
    // run server
  }
}
