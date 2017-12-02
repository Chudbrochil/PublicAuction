import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server
{

    public Server(boolean isBank)
    {
        try
        {
            if(isBank)
            {
                bankInit();
            }
            else
            {
                auctionCentralInit();
            }
        }
        catch (Exception e)
        {
            e.getLocalizedMessage();
            e.getMessage();
            e.printStackTrace();
        }
    }

    private void bankInit() throws Exception
    {
        Bank bank = new Bank();
        ServerSocket bankSocket = new ServerSocket(4444);
        while (true)
        {

            Socket pipeConnection = bankSocket.accept();
            ObjectOutputStream bankOut = new ObjectOutputStream(pipeConnection.getOutputStream());
            ObjectInputStream bankIn = new ObjectInputStream(pipeConnection.getInputStream());
            System.out.println("Bank Online");

            Object object = bankIn.readObject();


            if (object instanceof Agent)
            {
                Agent agent = (Agent) object;
                // If this is the first time we are getting an agent, register it
                if (!agent.isRegistered())
                {
                    bank.registerAgent(agent);
                    agent.setRegistered(true);
                    bankOut.writeObject(agent);
                }
                // If we are getting an agent again, we must be doing a withdrawl //TODO: Remove this after we aren't doing withdrawl's
                else
                {
                    // TODO: This 100 is hard-coded for now. We'll need to gather this value from a message.
                    bank.getMap().get(agent.getAccountNum()).setAmount(bank.getMap().get(agent.getAccountNum()).getAmount() - 100);
                    agent.setAccountBalance(agent.getAccountBalance() - 100.00);
                    System.out.println(bank.getMap().get(agent.getAccountNum()).getAmount());
                }
            }
        }
    }

    private void auctionCentralInit() throws Exception
    {
        AuctionCentral ac = new AuctionCentral();
        ServerSocket auctionCentralSocket = new ServerSocket(5555);


        while (true)
        {

            Socket otherPipeConnection = auctionCentralSocket.accept();
            ObjectOutputStream centralOut = new ObjectOutputStream(otherPipeConnection.getOutputStream());
            ObjectInputStream centralIn = new ObjectInputStream(otherPipeConnection.getInputStream());

            Object object = centralIn.readObject();

            if (object instanceof Agent)
            {

//                    otherPipeConnection = auctionCentralSocket.accept();
//                    centralOut = new ObjectOutputStream(otherPipeConnection.getOutputStream());
//                     centralIn = new ObjectInputStream(otherPipeConnection.getInputStream());

                Agent agent = (Agent) object;
                System.out.println("Auction Central Online");

                ac.registerAgent(agent);
                centralOut.writeObject(agent);
            }

//                otherPipeConnection = auctionCentralSocket.accept();
//                centralOut = new ObjectOutputStream(otherPipeConnection.getOutputStream());
//                centralIn = new ObjectInputStream(otherPipeConnection.getInputStream());
            else if (object instanceof AuctionHouse)
            {
                AuctionHouse ah;
                ah = (AuctionHouse) object;
                ac.registerAuctionHouse(ah);
                ac.getListOfAHs().add(ah);
                centralOut.writeObject(ah);
            }

            else {


                System.out.println(ac.getListOfAHs().size());
                centralOut.writeObject(ac.getListOfAHs());
            }
//                centralOut.writeObject(ac.getMap());

        }
    }

    public static void main(String[] args)
    {
        if (args[0].equals("Bank"))
        {
            Server s = new Server(true);
        }
        else if (args[0].equals("AC"))
        {
            Server s = new Server(false);
        }
    }


}