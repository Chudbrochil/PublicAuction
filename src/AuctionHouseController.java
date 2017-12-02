import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.PrintStream;

public class AuctionHouseController
{

    @FXML
    TextArea taOutput;

    @FXML
    public void initialize()
    {
        // We are allowing standard out to be printed to our text area.
        System.setOut(new PrintStream(Main.getStandardOutCapture(taOutput), true));

        String name = Main.askName();

        // New thread for Auction Central while(true)
        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                Client c = new Client(false, name);
            }
        });

        newThread.start();
    }
}
