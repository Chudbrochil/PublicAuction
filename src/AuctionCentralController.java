import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class AuctionCentralController
{
    @FXML
    TextArea taOutput;


    @FXML
    public void initialize()
    {
        taOutput.setText("This is a AC.");
    }



}
