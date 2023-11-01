
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CRUDEventController implements Initializable {

  @FXML
  Button btnInsert, btnDelete, btnUpdate;

  @FXML
  TextField tfName, tfDate, tfMember, tf, account;

  @FXML
  TableView<Account> data;

  @FXML
  TableColumn<Account, String> colName;

  @FXML
  TableColumn<Account, Integer> colID, accAmount, colAccount;

  /**
   * This will be used to Memorize queries
   */
  HashMap<String, String> queryTable = new HashMap<String, String>();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    throw new UnsupportedOperationException(
      "Unimplemented method 'initialize'"
    );
  }

  /**
   * nested class tyo represents teh accounts used for the gui
   */
  class Account {

    private String name, date;
    private int id, account, amount;

    /**
     * @return the name
     */
    public String getName() {
      return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
      this.name = name;
    }

    /**
     * @return the date
     */
    public String getDate() {
      return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
      this.date = date;
    }

    /**
     * @return the id
     */
    public int getId() {
      return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
      this.id = id;
    }

    /**
     * @return the account
     */
    public int getAccount() {
      return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(int account) {
      this.account = account;
    }

    /**
     * @return the amount
     */
    public int getAmount() {
      return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(int amount) {
      this.amount = amount;
    }
  }
}
