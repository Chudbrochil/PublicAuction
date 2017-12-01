import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller
{

    @FXML
    private Label lblUserOutput, lblBalance;

    Agent agent;

    @FXML
    private void initialize()
    {
        Client client = new Client("Agent1");

        agent = client.getAgent();

        update();



        // TODO: Only for testing, delete as implementation continues
        //AuctionHouse ah = new AuctionHouse();
    }

    private void update()
    {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable()
        {
            @Override
            public void run()
            {
                System.out.println(agent.getAccountBalance());
                lblBalance.setText(String.valueOf(agent.getAccountBalance()));
            }
        }, 0, 40, TimeUnit.MILLISECONDS);
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
