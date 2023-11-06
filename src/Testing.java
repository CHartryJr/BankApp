

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import controllers.TellerEventController;
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
