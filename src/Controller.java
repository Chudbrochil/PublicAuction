import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class Controller
{

    @FXML
    private TextField tfUserOutput;

    @FXML
    private void initialize()
    {









        // TODO: Only for testing, delete as implementation continues
        //AuctionHouse ah = new AuctionHouse();
    }


    /**
     * User wants to place a bid
     */
    @FXML
    private void placeBid()
    {
        tfUserOutput.setText("User pressed placeBid button.");
    }

    /**
     * TODO: UI elements needed...
     *
     * 1. A list of available items from the auction houses... OPTIONAL: status next to each item, currentBid etc.
     * 2. A way to select an item... at the start this could be a field that takes an int index.
     *
     */





}
