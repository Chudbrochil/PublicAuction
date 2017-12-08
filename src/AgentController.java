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
import javafx.scene.image.ImageView;

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

    @FXML
    private ImageView ivTest;

    Item currentSelectedItem;

    ArrayList<Item> itemsAsList;


    Agent agent; // Inside class that keeps account information and item information
    Client client; // Wrapper class for agent that opens sockets and communicates for agent

    /**
     * initialize()
     * Initializes the Agent Controller.
     */
    @FXML
    private void initialize()
    {
        itemsAsList = new ArrayList<>();
        client = new Client(true, Main.askName(), taAgentOutput);

        // TODO: Jacob, unleash this thread on the wild when clientListening is ready...
        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run()
            {

                client.clientListening();

            }
        });
        newThread.start();
        agent = client.getAgent();
        lvItems.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                setCurrentSelectedItem(newValue);
            }
        });
        update();
    }

    /**
     * update()
     * Method that will constantly be updating the UI to the user. Runs on an executor thread.
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

    /**
     * updateItemList()
     *
     * Updates the GUI with the current list of items from each auction house.
     */
    private void updateItemList()
    {
        ArrayList<AuctionHouse> listOfAHs = agent.getAuctionHouses();
        ArrayList<Item> items = new ArrayList<>();
        ObservableList<String> itemNames = FXCollections.observableArrayList();

        if(listOfAHs != null)
        {
            for(int i = 0; i < listOfAHs.size(); ++i)
            {
                HashMap<Integer, Item> ahItems = listOfAHs.get(i).getItems();
                itemsAsList = new ArrayList<Item>(ahItems.values());

                for(int j = 0; j < itemsAsList.size(); ++j)
                {
                    // Checking to make sure the global items list doesn't already have the item before adding it
                    if(!items.contains(itemsAsList.get(j)))
                    {
                        items.add(itemsAsList.get(j));
                        itemNames.add(itemsAsList.get(j).toString());
                    }
                }
            }
        }

        lvItems.setItems(itemNames);
    }


    /**
     * btnWithdraw()
     *
     * On action method for the withdraw button on the GUI.
     */
    @FXML
    private void btnWithdraw() // TODO: handle bad input?
    {
        taAgentOutput.appendText("Submitted withdraw request to bank for: " + tfBidAmount.getText() + "\n");
        client.withdraw(Double.valueOf(tfBidAmount.getText()), agent);
    }

    /**
     * setCurrentSelectedItem()
     *
     * This is called by the change listener on the listview. When a new item is selected, the item inside the
     * controller is updated. This can be used for various item updates.
     *
     * @param itemString The string of the item appearing on the listview.
     */
    private void setCurrentSelectedItem(String itemString)
    {
        if(itemString != null)
        {
            for(int i = 0; i < itemsAsList.size(); ++i)
            {
                if(itemString.equals(itemsAsList.get(i).toString()))
                {
                    currentSelectedItem = itemsAsList.get(i);
                    break;
                }
            }
        }

    }

    /**
     * placeBid()
     * Handler for user clicking that they want to place a bid.
     */
    @FXML
    private void btnPlaceBid() // TODO: handle bad input?
    {
        Double bidAmount = Double.valueOf(tfBidAmount.getText());
        taAgentOutput.appendText("Placing bid for " + bidAmount + " on item:\n" + currentSelectedItem.toString() + "\n");
        client.placeAHBid(bidAmount, agent.getBiddingKey(), currentSelectedItem);
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
