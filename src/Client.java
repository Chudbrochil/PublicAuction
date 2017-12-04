import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client
{
    private Agent agent;
    private AuctionHouse auctionHouse;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket bankSocket;
    private Socket auctionCentralSocket;
    private TextArea taAgentOutput;
    private String bankHostname;
    private String acHostname;
    private boolean isAgent;
    private boolean acConnected;
    private boolean bankConnected;

    /**
     * Client()
     * Constructor useful for running on command line
     * @param isAgent
     * @param name
     */
    public Client(boolean isAgent, String name)
    {
        this(isAgent, name, null);
    }

    /**
     * Client()
     * Regular constructor for Client that gets called the Controller
     * @param isAgent
     * @param name
     * @param taAgentOutput
     */
    public Client(boolean isAgent, String name, TextArea taAgentOutput)
    {
        this.isAgent = isAgent;

        bankConnected = false;
        acConnected = false;


        if (name == null)
        {
            name = "NONAME CLIENT";
        }
        this.taAgentOutput = taAgentOutput;

        if (isAgent)
        {
            agent = new Agent(name);
            if(taAgentOutput != null) { taAgentOutput.appendText("You've chosen " + agent.getName() + " as your name.\n"); }
        }
        else
        {
            auctionHouse = new AuctionHouse(name);
            System.out.println("You've chosen " + auctionHouse.getName() + " as your auction house.");
        }

        // Console only supports localhost connections for now.
        // TODO: Add console connectivity?
        // TODO: Add AH individual connectivity on UI same as Agent
        if(taAgentOutput == null || isAgent == false)
        {
            connectLocalhost();
        }

    }

    private void registerAgentWithBank() throws IOException, ClassNotFoundException
    {
        // Registering with bank
        out = new ObjectOutputStream(bankSocket.getOutputStream());
        in = new ObjectInputStream(bankSocket.getInputStream());

        out.writeObject(new Message(MessageType.REGISTER_AGENT, agent.getName(), new Account()));
        Message response = (Message) in.readObject();
        agent.setAccountInfo(response.getAccount());

        if(taAgentOutput != null) { taAgentOutput.appendText("Starting Balance: " + agent.getAccountBalance() + "\n"); }
        if(taAgentOutput != null) { taAgentOutput.appendText("Account Number: " + agent.getAccountNum() + "\n"); }

    }

    private void registerAgentWithAC() throws IOException, ClassNotFoundException
    {
        // Registering with AC
        out = new ObjectOutputStream(auctionCentralSocket.getOutputStream());
        in = new ObjectInputStream(auctionCentralSocket.getInputStream());
        out.writeObject(new Message(MessageType.REGISTER_AGENT, agent.getName(), agent.getBankKey(), ""));
        Message response = (Message)in.readObject();
        agent.setBiddingKey(response.getBiddingKey());
        if(taAgentOutput != null) { taAgentOutput.appendText("Bidding Key: " + agent.getBiddingKey() + "\n"); }
    }

    private void registerAHWithAC() throws IOException, ClassNotFoundException
    {
        out = new ObjectOutputStream(auctionCentralSocket.getOutputStream());
        in = new ObjectInputStream(auctionCentralSocket.getInputStream());
        out.writeObject(new Message(MessageType.REGISTER_AH, auctionHouse));
        Message incomingMessage = (Message)in.readObject();
        auctionHouse = incomingMessage.getAuctionHouse();
    }

    public Agent getAgent() { return agent; }


    /**
     * AuctionHouse and Agent messaging methods live here
     */
    public void withdraw(double bidAmt, Agent agent)
    {
        try
        {
            if(bankConnected)
            {
                bankSocket = new Socket(bankHostname, Main.bankPort);
                out = new ObjectOutputStream(bankSocket.getOutputStream());
                in = new ObjectInputStream(bankSocket.getInputStream());

                // Sending a message of type Withdraw
                out.writeObject(new Message(MessageType.WITHDRAW, agent.getAccountNum(), bidAmt));
                Message response = (Message)in.readObject();

                if(response.getBidResponse() == BidResponse.ACCEPT)
                {
                    agent.deductAccountBalance(response.BIDDING_AMOUNT);
                }
                else
                {
                    if(taAgentOutput != null) { taAgentOutput.appendText("You don't have enough funds to withdraw " + response.BIDDING_AMOUNT + "\n"); }
                }
            }

        }
        catch(IOException e) { System.out.println(e.getMessage()); }
        catch(ClassNotFoundException e) { System.out.println(e.getMessage()); }
    }


    public void updateListOfAHs()
    {
        try
        {
            if(acConnected)
            {
                auctionCentralSocket = new Socket(acHostname, Main.auctionCentralPort);
                out = new ObjectOutputStream(auctionCentralSocket.getOutputStream());
                in = new ObjectInputStream(auctionCentralSocket.getInputStream());
                out.writeObject(new Message(MessageType.UPDATE_AHS, new ArrayList<AuctionHouse>()));

                Message response = (Message)in.readObject();

                if(response.getType() == MessageType.UPDATE_AHS)
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
        catch(IOException e) { System.out.println(e.getMessage()); }
        catch(ClassNotFoundException e) { System.out.println(e.getMessage()); }
    }

    public void setBankHostname(String bankHostname)
    {
        this.bankHostname = bankHostname;

        try
        {
            if(isAgent)
            {
                // Only the agent needs a connection to the bank.
                bankSocket = new Socket(bankHostname, Main.bankPort);
                bankConnected = true;
                registerAgentWithBank();
                taAgentOutput.appendText("Connecting and registering with bank at: " + bankHostname + ":" + Main.bankPort + "\n");
            }

        }
        catch(IOException e) { System.out.println(e.getMessage()); }
        catch(ClassNotFoundException e) { System.out.println(e.getMessage()); }
    }

    public void setAcHostname(String acHostname)
    {
        this.acHostname = acHostname;

        try
        {
            auctionCentralSocket = new Socket(acHostname, Main.auctionCentralPort);
            acConnected = true;
            if(isAgent && bankConnected)
            {
                registerAgentWithAC();
                taAgentOutput.appendText("Connecting and registering with AC at: " + acHostname + ":" + Main.auctionCentralPort + "\n");
            }
            else if(isAgent && !bankConnected)
            {
                taAgentOutput.appendText("Cannot connect to AC. Register with bank first to get your bank key.\n");
            }
            else { registerAHWithAC(); }
        }
        catch(IOException e) { System.out.println(e.getMessage()); }
        catch(ClassNotFoundException e) { System.out.println(e.getMessage()); }
    }

    public boolean getBankConnected() { return bankConnected; }
    public boolean getAcConnected() { return acConnected; }

    public void connectLocalhost()
    {
        setBankHostname("127.0.0.1");
        setAcHostname("127.0.0.1");
    }

    public static void main(String[] args)
    {
        if (args[0].equals("AH"))
        {
            Client client = new Client(false, args[1]);
        }
        else if (args[0].equals("Agent") && !args[1].equals(null))
        {
            Client client = new Client(true, args[1]);
        }
    }


}
