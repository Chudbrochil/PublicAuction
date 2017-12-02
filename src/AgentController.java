import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.ConnectException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AgentController
{

    @FXML
    private Label lblUserOutput, lblBalance;

    @FXML
    private TextField tfBidAmount;

    Agent agent; // Inside class that keeps account information and item information
    Client client; // Wrapper class for agent that opens sockets and communicates for agent

    @FXML
    private void initialize()
    {
        client = new Client(true, "Agent1");
        update();
    }

    /**
     * update()
     * Method that will constantly be updating the UI to the user.
     */
    private void update()
    {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable()
        {
            @Override
            public void run()
            {
                agent = client.getAgent();
                // TODO: Auction houses for some reason are not updating in real time
                client.getAgent().setAuctionHouses(client.getListAH(agent.getAuctionHouses()));
                System.out.println(client.getAgent().getAuctionHouses().get(0).getName());
                System.out.println(client.getAgent().getAuctionHouses().get(1).getName());
                // Platform syncs this command with the UI, fixes javafx thread bugs
                Platform.runLater(() -> {
                    lblBalance.setText(String.valueOf(agent.getAccountBalance()));
                });
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }


    /**
     * placeBid()
     * Handler for user clicking that they want to place a bid.
     */
    @FXML
    private void placeBid()
    {
        lblUserOutput.setText("Accepted bid for: " + tfBidAmount.getText());
        client.placeBid(Double.valueOf(tfBidAmount.getText()), client.getAgent());
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
