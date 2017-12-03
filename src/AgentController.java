import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AgentController
{

    @FXML
    private Label lblBalance;

    @FXML
    private TextArea taAgentOutput;

    @FXML
    private ListView lvItems;

    @FXML
    private TextField tfBidAmount;

    Agent agent; // Inside class that keeps account information and item information
    Client client; // Wrapper class for agent that opens sockets and communicates for agent

    @FXML
    private void initialize()
    {
        client = new Client(true, Main.askName(), taAgentOutput);
        agent = client.getAgent();
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
                // Getting the latest list of auction houses that are up and updates the item list
                client.updateListOfAHs();

                // Platform syncs this command with the UI, fixes javafx thread bugs
                Platform.runLater(() -> {
                    updateItemList();
                    lblBalance.setText(String.valueOf(agent.getAccountBalance()));
                });
            }
        }, 0, 250, TimeUnit.MILLISECONDS);
    }



    @FXML
    private void btnWithdraw()
    {
        taAgentOutput.appendText("Accepted withdraw for: " + tfBidAmount.getText() + "\n");
        client.withdraw(Double.valueOf(tfBidAmount.getText()), agent);
    }

    /**
     * placeBid()
     * Handler for user clicking that they want to place a bid.
     */
    @FXML
    private void btnPlaceBid()
    {

    }

    private void updateItemList()
    {
        ArrayList<AuctionHouse> listOfAHs = agent.getAuctionHouses();
        ArrayList<Item> items = new ArrayList<>();
        ObservableList<String> itemNames = FXCollections.observableArrayList();
        for(int i = 0; i < listOfAHs.size(); ++i)
        {
            HashMap<Integer, Item> ahItems = listOfAHs.get(i).getItems();
            ArrayList<Item> listOfItems = new ArrayList<Item>(ahItems.values());

            for(int j = 0; j < listOfItems.size(); ++j)
            {
                // Checking to make sure the global items list doesn't already have the item before adding it
                if(!items.contains(listOfItems.get(j)))
                {
                    items.add(listOfItems.get(j));
                    itemNames.add("Name: " + listOfItems.get(j).ITEM_NAME + " ID:" + listOfItems.get(j).getItemID() + " AH: " + listOfItems.get(j).getAhID());
                }
            }
        }

        lvItems.setItems(itemNames);

    }


    /**
     * TODO: UI elements needed...
     *
     * 1. A list of available items from the auction houses... OPTIONAL: status next to each item, currentBid etc.
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
