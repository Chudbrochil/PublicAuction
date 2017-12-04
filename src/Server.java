import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private boolean isListening;

    public Server(boolean isBank)
    {
        // TODO: isListening is a stand-in for having a Bank/AC being spun down and up. This may be an extra feature...
        isListening = false;
        try
        {
            if(isBank)
            {
                bankLaunch();
            }
            else
            {
                auctionCentralLaunch();
            }
        }
        catch (Exception e)
        {
            e.getLocalizedMessage();
            e.getMessage();
            e.printStackTrace();
        }
    }

    private void bankLaunch() throws Exception
    {
        Bank bank = new Bank();
        ServerSocket bankSocket = new ServerSocket(Main.bankPort);
        isListening = true;

        System.out.println("Bank online.");
        System.out.println(Main.returnNetworkInfo());
        System.out.println("Port: " + Main.bankPort);

        while (true)
        {
            Socket pipeConnection = bankSocket.accept();
            ObjectOutputStream bankOut = new ObjectOutputStream(pipeConnection.getOutputStream());
            ObjectInputStream bankIn = new ObjectInputStream(pipeConnection.getInputStream());
            Object object = bankIn.readObject();

            if(object instanceof Message)
            {
                Message incomingMessage = (Message)object;
                // Performing a withdrawl for an agent
                if(incomingMessage.getType() == MessageType.WITHDRAW)
                {
                    Account account = bank.getBankKeyToAccount().get(incomingMessage.getBankKey());
                    // If we were able to deduct the bidding amount, then take it out, send a success back.
                    if(account.deductAccountBalance(incomingMessage.BIDDING_AMOUNT))
                    {
                        incomingMessage.setBidResponse(BidResponse.ACCEPT);
                        System.out.println("Bank accepted withdrawl of " + incomingMessage.BIDDING_AMOUNT + " from:\n");
                    }
                    // If there wasn't enough money, send a rejection back.
                    else
                    {
                        incomingMessage.setBidResponse(BidResponse.REJECT);
                        System.out.println("Bank refused withdrawl of " + incomingMessage.BIDDING_AMOUNT + " from:\n");
                    }
                    System.out.println("Acct#: " + account.getAccountNum() + " BankKey: " + account.getBankKey() + " New Balance: " + account.getAccountBalance());
                    bankOut.writeObject(incomingMessage);
                }
                // Initializing an agent with an account (account#, balance, bankkey)
                else if(incomingMessage.getType() == MessageType.REGISTER_AGENT)
                {
                    System.out.println("Got a message register_agent from " + incomingMessage.getName());
                    bank.registerAgent(incomingMessage.getName(), incomingMessage.getAccount());
                    bankOut.writeObject(incomingMessage);
                }
            }
            else
            {
                System.out.println("Bank received unrecognized message.");
            }


        }
    }

    private void auctionCentralLaunch() throws Exception
    {
        AuctionCentral ac = new AuctionCentral();
        ServerSocket auctionCentralSocket = new ServerSocket(Main.auctionCentralPort);

        isListening = true;
        System.out.println("Auction Central online.");
        System.out.println(Main.returnNetworkInfo());
        System.out.println("Port: " + Main.auctionCentralPort);

        while (true)
        {
            Socket otherPipeConnection = auctionCentralSocket.accept();
            ObjectOutputStream centralOut = new ObjectOutputStream(otherPipeConnection.getOutputStream());
            ObjectInputStream centralIn = new ObjectInputStream(otherPipeConnection.getInputStream());

            Object object = centralIn.readObject();

            if(object instanceof Message)
            {
                Message incomingMessage = (Message)object;
                // Updating the list of AHs to the agent
                if(incomingMessage.getType() == MessageType.UPDATE_AHS)
                {
                    incomingMessage.setListOfAHs(ac.getListOfAHs());
                }
                // Registering a new agent with AC
                else if(incomingMessage.getType() == MessageType.REGISTER_AGENT)
                {
                    String biddingKey = ac.registerAgent(incomingMessage.getName(), incomingMessage.getBankKey());
                    incomingMessage.setBiddingKey(biddingKey);
                }
                // Registering a new AH with AC
                else if(incomingMessage.getType() == MessageType.REGISTER_AH)
                {
                    ac.registerAuctionHouse(incomingMessage.getAuctionHouse());
                }
                centralOut.writeObject(incomingMessage);
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