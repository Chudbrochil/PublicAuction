import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server
{
    private boolean isListening;

    private static int bankPort = 4444;
    private static int auctionCentralPort = 5555;

    public Server(boolean isBank)
    {
        // TODO: isListening is a stand-in for having a Bank/AC being spun down and up. This may be an extra feature...
        isListening = false;
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
        ServerSocket bankSocket = new ServerSocket(bankPort);
        isListening = true;

        System.out.println("Bank online.");
        System.out.println(Main.returnNetworkInfo());
        System.out.println("Port: " + bankPort);


        while (true)
        {
            Socket pipeConnection = bankSocket.accept();
            ObjectOutputStream bankOut = new ObjectOutputStream(pipeConnection.getOutputStream());
            ObjectInputStream bankIn = new ObjectInputStream(pipeConnection.getInputStream());
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
                    bank.getAccountNumberToAccountMap().get(agent.getAccountNum()).setAmount(bank.getAccountNumberToAccountMap().get(agent.getAccountNum()).getAmount() - 100);
                    agent.setAccountBalance(agent.getAccountBalance() - 100.00);
                    System.out.println(bank.getAccountNumberToAccountMap().get(agent.getAccountNum()).getAmount());
                }
            }
        }
    }

    private void auctionCentralInit() throws Exception
    {
        AuctionCentral ac = new AuctionCentral();
        ServerSocket auctionCentralSocket = new ServerSocket(auctionCentralPort);

        isListening = true;
        System.out.println("Auction Central online.");
        System.out.println(Main.returnNetworkInfo());
        System.out.println("Port: " + auctionCentralPort);

        while (true)
        {
            Socket otherPipeConnection = auctionCentralSocket.accept();
            ObjectOutputStream centralOut = new ObjectOutputStream(otherPipeConnection.getOutputStream());
            ObjectInputStream centralIn = new ObjectInputStream(otherPipeConnection.getInputStream());

            Object object = centralIn.readObject();

            if (object instanceof Agent)
            {
                Agent agent = (Agent) object;
                ac.registerAgent(agent);
                centralOut.writeObject(agent);
            }
            else if (object instanceof AuctionHouse)
            {
                AuctionHouse ah;
                ah = (AuctionHouse) object;
                ac.registerAuctionHouse(ah);
                centralOut.writeObject(ah);
            }

            else {

                centralOut.writeObject(ac.getListOfAHs());
            }

        }
    }

    // Useful for running things on command line only.
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