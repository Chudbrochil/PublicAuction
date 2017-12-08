import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.PrintStream;

// TODO: BankController and ACController are almost exactly the same. Please fix this...
public class BankController
{
    @FXML
    private TextArea taOutput;

    @FXML
    private Label lblAgentsList, lblConnectionInfo;

    @FXML
    private TextField tfAuctionCentralIP;

    @FXML
    public void initialize()
    {
        // We are allowing standard out to be printed to our text area.
        System.setOut(new PrintStream(Main.getStandardOutCapture(taOutput), true));

        // Because Bank runs a "while(true)" to catch incoming messages, it needs a new thread separate from the UI
        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                Server server = new Server(true, lblAgentsList, lblConnectionInfo);
            }
        });

        newThread.start();
    }

    // TODO: Remember to do error checking for if the bank isn't connected in AC/Server.
    @FXML
    private void btnConnectAC()
    {
        Server.setPeerConnection(tfAuctionCentralIP.getText());
    }


}
