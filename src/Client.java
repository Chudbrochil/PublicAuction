import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
    private Agent agent;
    private AuctionHouse auctionHouse;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket bankSocket;

    public Client(boolean isAgent, String name, Scanner scanner)
    {
        if (isAgent)
        {
            agent = new Agent(name);
            System.out.println("You've chosen " + agent.getName() + " as your username");
            try
            {
                Socket bankSocket = new Socket("127.0.0.1", 4444);
                Socket centralSocket = new Socket("127.0.0.1", 5555);

                out = new ObjectOutputStream(bankSocket.getOutputStream());
                in = new ObjectInputStream(bankSocket.getInputStream());

                registerAgent(out, in, agent, centralSocket);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                e.getMessage();
                e.getLocalizedMessage();
            }
        }

        // TODO: Purely for spinning up agents on command line. Will be (likely) removed after GUI is hardened.
        if (scanner != null)
        {
            System.out.println("Would you like to bid? Y/N");
            String answer = scanner.nextLine();
            if (answer.equals("Y"))
            {
                //put auction houses listings here
            }
            else if (answer.equals("N"))
            {

            }
        }

        if (isAgent == false)
        {
            auctionHouse = new AuctionHouse("AuctionHouse1");
            System.out.println("You've created a new Auction House");

            try
            {
                Socket auctionCentralSocket = new Socket("127.0.0.1", 5555);

                out = new ObjectOutputStream(auctionCentralSocket.getOutputStream());
                in = new ObjectInputStream(auctionCentralSocket.getInputStream());
                registerAH(out, in, auctionHouse);
            }
            catch (Exception e)
            {
                e.getLocalizedMessage();
                e.getMessage();
                e.printStackTrace();
            }
        }

    }


    public void registerAgent(ObjectOutputStream out, ObjectInputStream in, Agent newUser, Socket centralSocket)
    {
        try
        {
            out.writeObject(newUser);

            newUser = (Agent) in.readObject();

            System.out.println("Account Amount = " + newUser.getAccountBalance());
            System.out.println("Account Number = " + newUser.getAccountNum());

            out = new ObjectOutputStream(centralSocket.getOutputStream());
            in = new ObjectInputStream(centralSocket.getInputStream());

            out.writeObject(newUser);

            newUser = (Agent) in.readObject();

            System.out.println("Bidding Key = " + newUser.getBiddingKey());
            agent = newUser;
        }
        catch (Exception e)
        {
            e.getLocalizedMessage();
            e.getMessage();
            e.printStackTrace();
        }
    }

    public void registerAH(ObjectOutputStream out, ObjectInputStream in, AuctionHouse ah)
    {
        try
        {
            out.writeObject(ah);
            ah = (AuctionHouse) in.readObject();

        }
        catch (Exception e)
        {
            e.getMessage();
            e.getLocalizedMessage();
            e.printStackTrace();
        }
    }

    public void registerAuctionHouse(AuctionHouse ah)
    {
        //registerAH //todo: Make i/o streams class variables?
    }

    public Agent getAgent()
    {
        return agent; //TODO: CHECK FOR NULL SOMEWHERE IN HERE
    }

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        if (args[0].equals("AuctionHouse"))
        {
            Client client = new Client(false, args[0], null);
        }
        else if (args[0].equals("Agent") && !args[1].equals(null))
        {
            Client client = new Client(true, args[1], scanner);
        }
    }


    public void placeBid(double bidAmt, Agent agent)
    {
        try
        {
            bankSocket = new Socket("127.0.0.1", 4444);

            out = new ObjectOutputStream(bankSocket.getOutputStream());
            in = new ObjectInputStream(bankSocket.getInputStream());

            out.writeObject(agent);

            // TODO: We need to get a response from the bank that the bid went through
            agent.setAccountBalance(agent.getAccountBalance() - bidAmt);

        }
        catch (Exception e)
        {
            e.getMessage();
            e.getLocalizedMessage();
            e.printStackTrace();
        }
    }


}
