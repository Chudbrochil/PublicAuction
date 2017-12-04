import javafx.scene.control.TextArea;

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
        if (name == null)
        {
            name = "NONAME CLIENT";
        }
        this.taAgentOutput = taAgentOutput;

        try
        {
            auctionCentralSocket = new Socket("127.0.0.1", 5555);

            if (isAgent)
            {
                agentInit(name);
            }
            else
            {
                auctionHouseInit(name);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getMessage();
            e.getLocalizedMessage();
        }

    }

    private void agentInit(String name) throws Exception
    {
        agent = new Agent(name);
        if(taAgentOutput != null) { taAgentOutput.appendText("You've chosen " + agent.getName() + " as your name.\n"); }
        bankSocket = new Socket("127.0.0.1", 4444);
        out = new ObjectOutputStream(bankSocket.getOutputStream());
        in = new ObjectInputStream(bankSocket.getInputStream());
        registerAgent(out, in);

    }

    private void auctionHouseInit(String name) throws Exception
    {
        auctionHouse = new AuctionHouse(name);
        System.out.println("You've chosen " + auctionHouse.getName() + " as your auction house.");
        out = new ObjectOutputStream(auctionCentralSocket.getOutputStream());
        in = new ObjectInputStream(auctionCentralSocket.getInputStream());
        registerAH(out, in);
    }


    //ToDO: Seperate register agent for auction central and bank so that we don't throw a connection reset error
    private void registerAgent(ObjectOutputStream out, ObjectInputStream in)
    {
        try
        {
            // Registering with bank
            out.writeObject(new Message(MessageType.REGISTER_AGENT, agent.getName(), new Account()));
            Message response = (Message) in.readObject();
            agent.setAccountInfo(response.getAccount());

            if(taAgentOutput != null) { taAgentOutput.appendText("Starting Balance: " + agent.getAccountBalance() + "\n"); }
            if(taAgentOutput != null) { taAgentOutput.appendText("Account Number: " + agent.getAccountNum() + "\n"); }

            // Registering with AC
            out = new ObjectOutputStream(auctionCentralSocket.getOutputStream());
            in = new ObjectInputStream(auctionCentralSocket.getInputStream());

            out.writeObject(new Message(MessageType.REGISTER_AGENT, agent.getName(), agent.getBankKey(), ""));

            response = (Message)in.readObject();
            agent.setBiddingKey(response.getBiddingKey());

            if(taAgentOutput != null) { taAgentOutput.appendText("Bidding Key: " + agent.getBiddingKey() + "\n"); }
        }
        catch (Exception e)
        {
            e.getLocalizedMessage();
            e.getMessage();
            e.printStackTrace();
        }
    }

    private void registerAH(ObjectOutputStream out, ObjectInputStream in)
    {
        try
        {
            out.writeObject(new Message(MessageType.REGISTER_AH, auctionHouse));
            Message incomingMessage = (Message)in.readObject();
            auctionHouse = incomingMessage.getAuctionHouse();
        }
        catch (Exception e)
        {
            e.getMessage();
            e.getLocalizedMessage();
            e.printStackTrace();
        }
    }

    public Agent getAgent()
    {
        return agent; //TODO: CHECK FOR NULL SOMEWHERE IN HERE
    }


    /**
     * TODO: This is where method's for AuctionHouse and Agent will live. We need messaging here.
     */


    public void withdraw(double bidAmt, Agent agent)
    {
        try
        {
            bankSocket = new Socket("127.0.0.1", 4444);

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
        catch (Exception e)
        {
            e.getMessage();
            e.getLocalizedMessage();
            e.printStackTrace();
        }
    }


    public void updateListOfAHs()
    {
        try
        {
            auctionCentralSocket = new Socket("127.0.0.1", 5555);
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
        catch (Exception e)
        {
            e.printStackTrace();
            e.getLocalizedMessage();
            e.getMessage();
        }

    }

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
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
