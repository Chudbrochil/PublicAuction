import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import static java.lang.System.out;

public class BankController
{

    @FXML
    TextArea taOutput;


    @FXML
    public void initialize()
    {
        taOutput.setText("This is a bank.");

        Server s = new Server(true);

        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException
            {
                appendText(String.valueOf((char)b));
            }
        };

        System.setOut(new PrintStream(out, true));
    }

    public void appendText(String str)
    {
        Platform.runLater(() -> taOutput.appendText(str));
    }



}
