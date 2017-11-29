import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller
{

    @FXML
    private Label lblUserOutput, lblBalance;

    @FXML
    private void initialize()
    {
        lblBalance.setText("$1000.00");








        // TODO: Only for testing, delete as implementation continues
        //AuctionHouse ah = new AuctionHouse();
    }


    /**
     * User wants to place a bid
     */
    @FXML
    private void placeBid()
    {
        lblUserOutput.setText("User pressed placeBid button.");
    }

    /**
     * TODO: UI elements needed...
     *
     * 1. A list of available items from the auction houses... OPTIONAL: status next to each item, currentBid etc.
     * 2. A way to select an item... at the start this could be a field that takes an int index.
     *
     */


    /**
     * TODO: Make scenarios running on timers.... OPTIONAL?
     * i.e
     * 0s Turn on 3 auction houses
     * 45s turn on 1 auction house
     * 90s turn off auction house 1
     *
     */





}
