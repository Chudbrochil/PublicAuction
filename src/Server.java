import javafx.application.Platform;
import javafx.scene.control.Label;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server
{
    private boolean isListening;
    private Bank bank;
    private AuctionCentral auctionCentral;
    private Label lblClientsList, lblConnectionInfo;
    private boolean isBank;

    /**
     * Server()
     *
     * Server constructor
     *
     * @param isBank Boolean representing if this is a bank or not. True if bank, false if it's AC.
     */
    public Server(boolean isBank)
    {
        this(isBank, null, null);
    }

    // TODO: This design of passing in a label might not be the best way to do this.... (Getting registered agents/ah's)
    public Server(boolean isBank, Label lblClientsList, Label lblConnectionInfo)
    {
        this.isBank = isBank;
        this.lblClientsList = lblClientsList;
        this.lblConnectionInfo = lblConnectionInfo;

        // If we didn't originate from the command line then spin up a thread to update the clients label
        if(lblClientsList != null) updateClientsLabel();

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
        catch(IOException e) { System.out.println(e.getMessage()); }
        catch(ClassNotFoundException e) { System.out.println(e.getMessage()); }


    }

    private void updateClientsLabel()
    {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable()
        {
            @Override
            public void run()
            {
                Platform.runLater(() -> {
                    if(isBank && bank.getAgentsAsString() != null)
                    {
                        lblClientsList.setText(bank.getAgentsAsString());
                    }

                });
            }
        }, 0, 250, TimeUnit.MILLISECONDS);
    }

    /**
     * bankLaunch()
     *
     * Launchs a bank object and opens a socket for listening for messages
     *
     * @throws IOException Can be thrown from bad input/output in the streams
     * @throws ClassNotFoundException Can be thrown from bad cast from readObject()
     */
    private void bankLaunch() throws IOException, ClassNotFoundException
    {
        bank = new Bank();
        ServerSocket bankSocket = new ServerSocket(Main.bankPort);
        isListening = true;

        Platform.runLater(() ->lblConnectionInfo.setText(Main.returnNetworkInfo() + " Port: " + Main.bankPort));

        System.out.println("Bank online.\n");

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
                    System.out.println("MSG RECV: WITHDRAW - FROM: " + account.getName());
                    // If we were able to deduct the bidding amount, then take it out, send a success back.
                    if(account.deductAccountBalance(incomingMessage.getBidAmount()))
                    {
                        incomingMessage.setBidResponse(BidResponse.ACCEPT);
                        System.out.println("Bank accepted withdrawl of " + incomingMessage.getBidAmount() + " on account:\n");
                    }
                    // If there wasn't enough money, send a rejection back.
                    else
                    {
                        incomingMessage.setBidResponse(BidResponse.REJECT);
                        System.out.println("Bank refused withdrawl of " + incomingMessage.getBidAmount() + " on account:\n");
                    }
                    System.out.println(account.toString());
                    bankOut.writeObject(incomingMessage);
                }
                //When we place a bid
                 else if(incomingMessage.getType() == MessageType.PLACE_BID)
                {
                    Account account = bank.getBankKeyToAccount().get(incomingMessage.getBankKey());
                    System.out.println("MESSAGE: PLACE_BID - FROM: " + account.getName());
                    // If we were able to deduct the bidding amount, then take it out, send a success back.
                    if(account.deductAccountBalance(incomingMessage.getBidAmount()))
                    {
                        incomingMessage.setBidResponse(BidResponse.ACCEPT);
                        incomingMessage.setType(MessageType.PLACE_HOLD);
                        bankOut.writeObject(incomingMessage);
                        System.out.println("Bank has placed a hold on account:\n");
                    }
                    // If there wasn't enough money, send a rejection back.
                    else
                    {
                        incomingMessage.setBidResponse(BidResponse.REJECT);
                        incomingMessage.setType(MessageType.PLACE_HOLD);
                        bankOut.writeObject(incomingMessage);
                        System.out.println("Bank has refused a hold on account:\n");
                    }
                    System.out.println(account.toString());
                    bankOut.writeObject(incomingMessage);
                }
                // Initializing an agent with an account (name, account#, balance, bankkey)
                else if(incomingMessage.getType() == MessageType.REGISTER_AGENT)
                {
                    System.out.println("MESSAGE: REGISTER_AGENT - FROM: " + incomingMessage.getAccount().getName());
                    bank.registerAgent(incomingMessage.getAccount());
                    bankOut.writeObject(incomingMessage);
                }

            }
            else
            {
                System.out.println("Bank received unrecognized message. Doing nothing");
            }

        }
    }

    /**
     * auctionCentralLaunch()
     *
     * Launchs an Auction Central and opens a socket for listening to messages.
     *
     * @throws IOException Can be thrown from bad input/output in the streams
     * @throws ClassNotFoundException Can be thrown from bad cast from readObject()
     */
    private void auctionCentralLaunch() throws IOException, ClassNotFoundException
    {
        auctionCentral = new AuctionCentral();
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
                    incomingMessage.setListOfAHs(auctionCentral.getListOfAHs());
                }
                // Registering a new agent with AC
                else if(incomingMessage.getType() == MessageType.REGISTER_AGENT)
                {
                    String biddingKey = auctionCentral.registerAgent(incomingMessage.getName(), incomingMessage.getBankKey());
                    incomingMessage.setBiddingKey(biddingKey);
                }
                // Registering a new AH with AC
                else if(incomingMessage.getType() == MessageType.REGISTER_AH)
                {
                    auctionCentral.registerAuctionHouse(incomingMessage.getAuctionHouse());
                }
                else if(incomingMessage.getType() == MessageType.PLACE_BID)
                {
                    Socket bankSocket = new Socket("127.0.0.1", Main.bankPort);
                    ObjectOutputStream outToBank = new ObjectOutputStream(bankSocket.getOutputStream());
                    ObjectInputStream inFromBank = new ObjectInputStream(bankSocket.getInputStream());

                    incomingMessage.setBankKey(auctionCentral.getBiddingKeyToBankKey().get(incomingMessage.getBiddingKey()));
                    outToBank.writeObject(incomingMessage);


                    Message bankResponse = (Message) inFromBank.readObject();
                    bankResponse.setBiddingKey(auctionCentral.getBankKeyToBiddingKey().get(incomingMessage.getBankKey()));

                    centralOut.writeObject(bankResponse);


                }
                //                else if(incomingMessage.getType() == MessageType.ITEM_SOLD)
//                {
//                    Socket bankSocket = new Socket("127.0.0.1", 4444);
//
//                    out = new ObjectOutputStream(bankSocket.getOutputStream());
//                    in = new ObjectInputStream(bankSocket.getInputStream());
//
//                    // Sending a message of type Withdraw
//                    out.writeObject(new Message(MessageType.WITHDRAW, incomingMessage, bidAmt));
//                }

                centralOut.writeObject(incomingMessage);
            }

        }
    }

    public String getAgentsAsString()
    {
        return bank.getAgentsAsString();
    }

    /**
     * main()
     *
     * Strictly for spinning up Servers on the command line. Mostly used for debugging.
     */
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