import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{

    //constructor for  bank
    public Server(int num)
    {

        Bank bank = new Bank();

        //do bank registration for agent
        try
        {

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
                    // If we are getting an agent again,
                    else
                    {
                        bank.getMap().get(agent.getAccountNum()).setAmount(bank.getMap().get(agent.getAccountNum()).getAmount() - 100);
                        agent.setAccountBalance(agent.getAccountBalance() - 100.00);
                        System.out.println(bank.getMap().get(agent.getAccountNum()).getAmount());
                    }
                }


            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getMessage();
            e.getLocalizedMessage();
        }


    }

    public Server()
    {
        AuctionCentral ac = new AuctionCentral();


        try
        {
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
                    centralOut.writeObject(ah);
                }
//                centralOut.writeObject(ac.getMap());

            }


        }
        catch (Exception e)
        {
            e.getLocalizedMessage();
            e.getMessage();
            e.printStackTrace();
        }

    }


    public static void main(String[] args)
    {
        if (args[0].equals("Bank"))
        {
            Server s = new Server(4444);
        }
        else if (args[0].equals("AuctionCentral"))
        {
            Server s = new Server();
        }
    }


}