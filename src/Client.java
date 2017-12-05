import javafx.application.Platform;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Client
{
    private Agent agent;
    private AuctionHouse auctionHouse;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket bankSocket;
    private Socket auctionCentralSocket;
    private TextArea taAgentOutput;
    private Label lblAuctionHouseList;
    private String bankHostname;
    private String acHostname;
    private boolean isAgent;
    private boolean acConnected;
    private boolean bankConnected;

    /**
     * Client()
     * Constructor useful for running on command line
     *
     * @param isAgent Boolean value representing whether or not we're making an Agent or AH. true makes Agent, false AH
     * @param name    Name of the object (agent or AH) we are creating.
     */
    public Client(boolean isAgent, String name)
    {
        this(isAgent, name, null);
    }

    /**
     * Client()
     * Regular constructor for Client that gets called and updates the GUI via a text area
     *
     * @param isAgent          Boolean value representing whether or not we're making an Agent or AH. true makes Agent, false AH
     * @param name             Name of the object (agent or AH) we are creating.
     * @param agentOrAHControl This Control represents either an auction house label that will get updated with
     *                         a list of items or an agent text area that gets status updates
     */
    public Client(boolean isAgent, String name, Control agentOrAHControl)
    {
        this.isAgent = isAgent;

        bankConnected = false;
        acConnected = false;

        if (name == null)
        {
            name = "NONAME CLIENT";
        }

        if (isAgent)
        {
            taAgentOutput = (TextArea) agentOrAHControl;
            agent = new Agent(name);
            if (taAgentOutput != null)
            {
                taAgentOutput.appendText("Hello, " + agent.getName() + ".\n");
            }
        }
        else
        {
            lblAuctionHouseList = (Label) agentOrAHControl;
            auctionHouse = new AuctionHouse(name);
            System.out.println("Welcome, " + auctionHouse.getName() + ".\n");
            updateAuctionHouseListLabel();
        }

        // Console only supports localhost connections for now.
        if (taAgentOutput == null && lblAuctionHouseList == null)
        {
            connectLocalhost();
        }

    }

    /**
     * updateAuctionHouseListLabel()
     *
     * Spins up a thread to update the label on the AuctionHouse GUI. This Label corresponds to the list of items
     * that is currently in the Auction House.
     */
    private void updateAuctionHouseListLabel()
    {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable()
        {
            @Override
            public void run()
            {
                // Displaying the auction house' items in a label
                Platform.runLater(() -> {
                    lblAuctionHouseList.setText(auctionHouse.getItemsAsString());
                });
            }
        }, 0, 250, TimeUnit.MILLISECONDS);
    }

    /**
     * registerAgentWithBank()
     * <p>
     * Sends a message from this client(agent) to bank to register itself as an agent.
     * This initializes the agent's account(including bankkey, accountnum and startingbalance)
     *
     * @throws IOException            Can be thrown from bad input/output in the streams
     * @throws ClassNotFoundException Can be thrown from bad cast from readObject()
     */
    private void registerAgentWithBank() throws IOException, ClassNotFoundException
    {
        out = new ObjectOutputStream(bankSocket.getOutputStream());
        in = new ObjectInputStream(bankSocket.getInputStream());
        out.writeObject(new Message(MessageType.REGISTER_AGENT, new Account(agent.getName())));
        Message response = (Message) in.readObject();
        agent.setAccountInfo(response.getAccount());

        if (taAgentOutput != null)
        {
            taAgentOutput.appendText("Starting Balance: " + agent.getAccountBalance() + "\n");
            taAgentOutput.appendText("Account Number: " + agent.getAccountNum() + "\n");
            taAgentOutput.appendText("Bank Key: " + agent.getBankKey() + "\n");
        }
        bankConnected = true;

    }

    /**
     * registerAgentWithAC()
     * <p>
     * Sends a message from this client(agent) to auction central to register itself as an agent.
     * This is how an agent gets its bidding key.
     *
     * @throws IOException            Can be thrown from bad input/output in the streams
     * @throws ClassNotFoundException Can be thrown from bad cast from readObject()
     */
    private void registerAgentWithAC() throws IOException, ClassNotFoundException
    {
        out = new ObjectOutputStream(auctionCentralSocket.getOutputStream());
        in = new ObjectInputStream(auctionCentralSocket.getInputStream());
        out.writeObject(new Message(MessageType.REGISTER_AGENT, agent.getName(), agent.getBankKey(), ""));
        Message response = (Message) in.readObject();
        agent.setBiddingKey(response.getBiddingKey());
        taAgentOutput.appendText("Bidding Key: " + response.getBiddingKey() + "\n");
        acConnected = true;
    }

    /**
     * registerAHWithAC()
     * <p>
     * Sends a message from this client(auction house) to auction central to register itself as an AH.
     * This initializes everything about the auction house.
     *
     * @throws IOException            Can be thrown from bad input/output in the streams
     * @throws ClassNotFoundException Can be thrown from bad cast from readObject()
     */
    private void registerAHWithAC() throws IOException, ClassNotFoundException
    {
        out = new ObjectOutputStream(auctionCentralSocket.getOutputStream());
        in = new ObjectInputStream(auctionCentralSocket.getInputStream());
        out.writeObject(new Message(MessageType.REGISTER_AH, auctionHouse));
        Message incomingMessage = (Message) in.readObject();
        auctionHouse = incomingMessage.getAuctionHouse();
        acConnected = true;
    }

    /**
     * TODO: Can I somehow get rid of this method?
     * getAgent()
     *
     * @return The agent that is held within this client
     */
    public Agent getAgent()
    {
        return agent;
    }


    public void placeAHBid(double bidAmount, String biddingKey, Item item)
    {
        try
        {
            //todo: Is there a reason this runs every time? Can we put an if in here to make this once? --Anna
            auctionCentralSocket = new Socket(acHostname, Main.auctionCentralPort);
            out = new ObjectOutputStream(auctionCentralSocket.getOutputStream());
            in = new ObjectInputStream(auctionCentralSocket.getInputStream());
            
            out.writeObject(new Message(MessageType.PLACE_BID, biddingKey, bidAmount, item));
            Message response = (Message) in.readObject();

            //todo: AuctionHouse has placeBid() and receiveHoldResponse() methods. These would be good to use as the contain
            //this logic. --Anna
            if (response.getBidResponse() == BidResponse.ACCEPT && response.getItem().getCurrentBid() < response.getBidAmount()
                && response.getItem().getMinimumBid() < response.getBidAmount())
            //if (response.getBidResponse() == BidResponse.ACCEPT && auctionHouse.placeBid(biddingKey, bidAmount, item.getItemID(), auctionHouse.getPublicID()))
            {
                //todo: save prev bidder so AH can issue a "pass" response --Anna
                response.getItem().setCurrentBidAndBidder(response.getBidAmount(), response.getName());
            }

        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }

    }


    /**
     * AuctionHouse and Agent messaging methods live here
     */


    /**
     * withdraw()
     * <p>
     * Sends a message to the bank to withdraw a fixed amount from their account. This wasn't a requirement
     * of the project, but serves as a great proof of concept and theoretical feature.
     *
     * @param withdrawl The amount the agent is trying to withdraw
     * @param agent     The agent that wants to withdraw money.
     */
    public void withdraw(double withdrawl, Agent agent)
    {
        try
        {
            if (bankConnected)
            {
                bankSocket = new Socket(bankHostname, Main.bankPort);
                out = new ObjectOutputStream(bankSocket.getOutputStream());
                in = new ObjectInputStream(bankSocket.getInputStream());

                // Sending a message of type Withdraw
                out.writeObject(new Message(MessageType.WITHDRAW, agent.getBankKey(), withdrawl));
                Message response = (Message) in.readObject();

                if (response.getBidResponse() == BidResponse.ACCEPT)
                {
                    agent.deductAccountBalance(response.getBidAmount());
                    if (taAgentOutput != null)
                    {
                        taAgentOutput.appendText("Withdraw accepted. New balance: " + agent.getAccountBalance() + "\n");
                    }
                }
                else
                {
                    if (taAgentOutput != null)
                    {
                        taAgentOutput.appendText("You don't have enough funds to withdraw " + response.getBidAmount() + "\n");
                    }
                }
            }

        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
    }


    /**
     * updateListOfAHs()
     * <p>
     * Opens a socket to the AC and updates the agent's list of auction house's
     */
    public void updateListOfAHs()
    {
        try
        {
            if (acConnected)
            {
                auctionCentralSocket = new Socket(acHostname, Main.auctionCentralPort);
                out = new ObjectOutputStream(auctionCentralSocket.getOutputStream());
                in = new ObjectInputStream(auctionCentralSocket.getInputStream());
                out.writeObject(new Message(MessageType.UPDATE_AHS, new ArrayList<AuctionHouse>()));

                Message response = (Message) in.readObject();

                if (response.getType() == MessageType.UPDATE_AHS)
                {
                    agent.setAuctionHouses(response.getListOfAHs());
                }
                else
                {
                    // TODO: What if messages somehow get interleaved? Haven't tested this.
                    System.out.print("Whoops, received a message other than update AHs");
                }
            }

        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * setBankHostname()
     * <p>
     * Sets the hostname for the banksocket and then connects to the bank. If this Client is an agent, the agent
     * will register itself with the bank.
     *
     * @param bankHostname The hostname given to us to connect to
     */
    public void setBankHostname(String bankHostname)
    {
        this.bankHostname = bankHostname;

        try
        {
            if (isAgent)
            {
                // Only the agent needs a connection to the bank.
                if(!bankConnected)
                {
                    bankSocket = new Socket(bankHostname, Main.bankPort);
                    registerAgentWithBank();
                    taAgentOutput.appendText("Connecting and registering with bank at: " + bankHostname + ":" + Main.bankPort + "\n");
                }
            }

        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * setAcHostname()
     * <p>
     * Sets the hostname for the auctionCentralSocket and then connect to the Auction Central.
     * Then the AH or Agent registers with the Auction Central.
     *
     * @param acHostname
     */
    public void setAcHostname(String acHostname)
    {
        this.acHostname = acHostname;

        try
        {
            if(!acConnected)
            {

                if (isAgent && bankConnected)
                {
                    auctionCentralSocket = new Socket(acHostname, Main.auctionCentralPort);
                    registerAgentWithAC();
                    taAgentOutput.appendText("Connecting and registering with AC at: " + acHostname + ":" + Main.auctionCentralPort + "\n");
                }
                else if (isAgent && !bankConnected)
                {
                    taAgentOutput.appendText("Cannot connect to AC. Register with bank first to get your bank key.\n");
                }
                else
                {
                    auctionCentralSocket = new Socket(acHostname, Main.auctionCentralPort);
                    registerAHWithAC();
                    System.out.println("Connecting and registering with AC at: " + acHostname + ":" + Main.auctionCentralPort + "\n");
                }
            }


        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
    }

    // TODO: Do we care if the bank is connected?
    public boolean getBankConnected()
    {
        return bankConnected;
    }

    /**
     * getAcConnected()
     *
     * @return Boolean representing whether this client has connected with the AC already.
     */
    public boolean getAcConnected()
    {
        return acConnected;
    }

    /**
     * connectLocalhost()
     * <p>
     * Sets both of the bank and ac hostname's to localhost. This is very useful for working on the command line and
     * in most situations where all the nodes are on the same machine.
     */
    public void connectLocalhost()
    {
        setBankHostname("127.0.0.1");
        setAcHostname("127.0.0.1");
    }
    //Sends mssage to ac from ah to say item was sold.
//    public void itemSold(Item item, Double totalBid, Agent agent) {
//        try {
//            auctionCentralSocket = new Socket("127.0.0.1", 5555);
//            out = new ObjectOutputStream(auctionCentralSocket.getOutputStream());
//            in = new ObjectInputStream(auctionCentralSocket.getInputStream());
//            out.writeObject(new Message(MessageType.ITEM_SOLD, item.getItemID(), auctionHouse, agent.getBiddingKey(), totalBid));
//
//            Message response = (Message) in.readObject();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            e.getLocalizedMessage();
//            e.getMessage();
//        }
//    }

    /**
     * main()
     * <p>
     * Strictly for spinning up Clients on the command line. Mostly used for debugging.
     *
     * @param args First arg decides if you want an AH or Agent, second is the name
     */
    public static void main(String[] args)
    {
        if (args[0].equals("AH") && !args[1].equals(null))
        {
            Client client = new Client(false, args[1]);
        }
        else if (args[0].equals("Agent") && !args[1].equals(null))
        {
            Client client = new Client(true, args[1]);
        }
    }


}
