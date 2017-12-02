import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import java.io.PrintStream;

// TODO: BankController and ACController are almost exactly the same. Please fix this...
public class AuctionCentralController
{
    @FXML
    TextArea taOutput;

    @FXML
    public void initialize()
    {
        // We are allowing standard out to be printed to our text area.
        System.setOut(new PrintStream(Main.getStandardOutCapture(taOutput), true));

        // New thread for Auction Central while(true)
        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                Server s = new Server(false);
            }
        });

        newThread.start();
    }





}
