package testing;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import BankServer;
//future refences testing is done outside source folder. same as testing is done out side of production.
public class Testing 
{
   

    @BeforeAll
    public static void startServer()
    {
       BankServer.main(null);
    }

    @Test 
    public void TellerLogin()
    {
        
    }
}
