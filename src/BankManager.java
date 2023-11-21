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
    currentDirectory += "/assets/GUI/teller/TellerLogin.fxml";
    Parent root = FXMLLoader.load(new File(currentDirectory).toURI().toURL());
    String css = System.getProperty("user.dir");
    css += "/assets/Styles/Global.css";
    primaryStage.setTitle("GCSU CREDIT UNION OPERATIONS");
    Scene sc = new Scene(root);
    sc.getStylesheets().add(new File(css).toURI().toURL().toExternalForm());
    primaryStage.setScene(sc);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
    // run server
  }
}
