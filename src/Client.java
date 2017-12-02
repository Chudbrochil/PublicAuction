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

    public Client(boolean isAgent, String name)
    {
        if(name == null) { name = "NONAME CLIENT"; }

        try
        {
            auctionCentralSocket = new Socket("127.0.0.1", 5555);

            if(isAgent)
            {
                agentInit(name);
            }
            else
            {
                auctionHouseInit(name);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            e.getMessage();
            e.getLocalizedMessage();
        }

    }

    private void agentInit(String name) throws Exception
    {
            agent = new Agent(name);
            System.out.println("You've chosen " + agent.getName() + " as your username");
            bankSocket = new Socket("127.0.0.1", 4444);
            out = new ObjectOutputStream(bankSocket.getOutputStream());
            in = new ObjectInputStream(bankSocket.getInputStream());
            registerAgent(out, in, agent);

    }

    private void auctionHouseInit(String name) throws Exception
    {
        auctionHouse =  new AuctionHouse(name);
        System.out.println("You've chosen " + auctionHouse.getName() + " as your auction house.");
        out = new ObjectOutputStream(auctionCentralSocket.getOutputStream());
        in = new ObjectInputStream(auctionCentralSocket.getInputStream());
        registerAH(out, in, auctionHouse);
    }


    private void registerAgent(ObjectOutputStream out, ObjectInputStream in, Agent newUser)
    {
        try
        {
            out.writeObject(newUser);

            newUser = (Agent) in.readObject();

            System.out.println("Account Balance = " + newUser.getAccountBalance());
            System.out.println("Account Number = " + newUser.getAccountNum());

            out = new ObjectOutputStream(auctionCentralSocket.getOutputStream());
            in = new ObjectInputStream(auctionCentralSocket.getInputStream());

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

    private void registerAH(ObjectOutputStream out, ObjectInputStream in, AuctionHouse ah)
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

    public Agent getAgent()
    {
        return agent; //TODO: CHECK FOR NULL SOMEWHERE IN HERE
    }


    /**
     *  TODO: This is where method's for AuctionHouse and Agent will live. We need messaging here.
     */


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


    public ArrayList<AuctionHouse> getListAH(ArrayList list){
        try{
            auctionCentralSocket = new  Socket("127.0.0.1", 5555);
            out = new ObjectOutputStream(auctionCentralSocket.getOutputStream());
            in = new ObjectInputStream(auctionCentralSocket.getInputStream());


            out.writeObject(list);
           list = (ArrayList<AuctionHouse>) in.readObject();


            return list;


        }catch(Exception e){
            e.printStackTrace();
            e.getLocalizedMessage();
            e.getMessage();
        }
        return null;
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
