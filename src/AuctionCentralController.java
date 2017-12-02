import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;
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
        OutputStream out = new OutputStream()
        {
            @Override
            public void write(int b) throws IOException
            {
                appendText(String.valueOf((char)b));
            }
        };

        System.setOut(new PrintStream(out, true));

        // Because Bank runs a "while(true)" to catch incoming messages, it needs a new thread separate from the UI
        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                Server s = new Server(false);
            }
        });

        newThread.start();

    }

    public void appendText(String str)
    {
        Platform.runLater(() -> taOutput.appendText(str));
    }



}
