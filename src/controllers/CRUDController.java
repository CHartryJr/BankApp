package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class CRUDController implements Initializable
{
@FXML
  Text textID;
  @FXML
  TableView<Record> data;
  @FXML
  TableColumn<Record, String> colName;
  @FXML
  TableColumn<Record, Integer> colID, accAmount, colAccount;
  @FXML
  Button btnInsert,btnDelete, btnUpdate,btnInspect;
  @FXML
  TextField tfName, tfAccount;
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initialize'");
    }

    
}
