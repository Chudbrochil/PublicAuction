import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

    /**
     * UI TODO's
     *
     * REQUIRED
     * 1. List of items agent has already won.
     * 2. Display for what they have on hold. (i.e. Temp Hold: $544.12)
     *
     *
     *
     * OPTIONAL
     * 1. Status next to each item, highlight red for "in-progress"? Could even update currentbid on item
     * 2. Make scenarios running on timers... i.e. at 20s turn on an auction house. at 40s turn on 2 more, etc.
     * 3. Consolidate the other Controller's/FXML's
     * 4. Error-handling/input-checking on captured fields
     */

    @FXML
    private Label lblBalance;

    @FXML
    private TextArea taAgentOutput;

    @FXML
    private TextField tfBankIP, tfAuctionCentralIP;

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
        lvItems.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println(newValue);
            }
        });
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
                if(client.getAcConnected()) client.updateListOfAHs();

                // Platform syncs this command with the UI, fixes javafx thread bugs
                Platform.runLater(() -> {
                    // If the client has connected to the bank and ac already, update your items
                    if(client.getAcConnected()) updateItemList();
                    lblBalance.setText(String.valueOf(agent.getAccountBalance()));
                });
            }
        }, 0, 250, TimeUnit.MILLISECONDS);
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
                    itemNames.add(listOfItems.get(j).toString());
                }
            }
        }


        //lvItems.getItems().setAll(items); // This puts the actual items on the listview, but causes constant updates and makes the listview basically unclickable
        lvItems.setItems(itemNames);

    }



    @FXML
    private void btnWithdraw() // TODO: handle bad input?
    {
        taAgentOutput.appendText("Accepted withdraw for: " + tfBidAmount.getText() + "\n");
        client.withdraw(Double.valueOf(tfBidAmount.getText()), agent);
    }

    /**
     * placeBid()
     * Handler for user clicking that they want to place a bid.
     */
    @FXML
    private void btnPlaceBid() // TODO: handle bad input?
    {

    }

    @FXML
    private void btnConnectLocalhost()
    {
        client.connectLocalhost();
    }

    @FXML
    private void btnConnectBank()
    {
        client.setBankHostname(tfBankIP.getText()); // TODO: handle bad input?
    }

    @FXML
    private void btnConnectAC()
    {
        client.setAcHostname(tfAuctionCentralIP.getText()); // TODO: handle bad input?
    }



}
