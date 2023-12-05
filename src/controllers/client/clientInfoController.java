package controllers.client;

import java.net.URL;
import java.util.ResourceBundle;

import controllers.GUIOperation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class clientInfoController extends GUIOperation implements Initializable 
{
    @FXML
    private Button btnCheckings;
    @FXML
    private Button btnSavings;
    @FXML
    private TableColumn<?, ?> colDate;
    @FXML
    private TableColumn<?, ?> totalAmount;
    @FXML
    private TableColumn<?, ?> totalOper;
    @FXML
    private TableView<?> tvTable;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        throw new UnsupportedOperationException("Unimplemented method 'initialize'");
    }
}
