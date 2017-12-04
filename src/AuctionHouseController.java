import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.PrintStream;

public class AuctionHouseController
{

    @FXML
    TextArea taOutput;

    @FXML
    TextField tfAuctionCentralIP;

    Client client;

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
                client = new Client(false, name);
            }

        });

        newThread.start();
    }

    @FXML
    private void btnConnectAC()
    {
        client.setAcHostname(tfAuctionCentralIP.getText()); // TODO: handle bad input?
    }
}
