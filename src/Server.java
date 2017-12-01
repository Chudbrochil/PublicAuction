
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

                Agent agent;
                agent = (Agent) bankIn.readObject();

                bank.registerAgent(agent);
                bankOut.writeObject(agent);
//                    System.out.println("Where do we reside");

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

//                AuctionHouse ah;
//                ah = (AuctionHouse) centralIn.readObject();
//
//                ac.registerAuctionHouse(ah);
//
//                centralOut.writeObject(ah);
//                System.out.println("here we rare");
//
//
//                otherPipeConnection = auctionCentralSocket.accept();
//                centralOut = new ObjectOutputStream(otherPipeConnection.getOutputStream());
//                centralIn = new ObjectInputStream(otherPipeConnection.getInputStream());
                Agent agent = (Agent) centralIn.readObject();
                System.out.println("Auction Central Online");


                ac.registerAgent(agent);
                centralOut.writeObject(agent);

                centralOut.writeObject(ac.getMap());

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
