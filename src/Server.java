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

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private int portNumber = 20000;

    private static boolean isBank;
    private static String staticAcHostname = "127.0.0.1";
    private static String staticBankHostname = "127.0.0.1";
    private static boolean bankConnectedToAC;
    private static boolean acConnectedToBank;


    /**
     * Server()
     * <p>
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
        if (lblClientsList != null) updateClientsLabel();

        // TODO: isListening is a stand-in for having a Bank/AC being spun down and up. This may be an extra feature...
        isListening = false;
        try
        {
            if (isBank)
            {
                bankLaunch();
            }
            else
            {
                auctionCentralLaunch();
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }


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
                    if (isBank)
                    {
                        lblClientsList.setText(bank.getAgentsAsString());
                    }
                    else
                    {
                        lblClientsList.setText(auctionCentral.getListOfAHsAsString());
                    }


                });
            }
        }, 0, 150, TimeUnit.MILLISECONDS);
    }

    /**
     * bankLaunch()
     * <p>
     * Launchs a bank object and opens a socket for listening for messages
     *
     * @throws IOException            Can be thrown from bad input/output in the streams
     * @throws ClassNotFoundException Can be thrown from bad cast from readObject()
     */
    private void bankLaunch() throws IOException, ClassNotFoundException
    {
        bank = new Bank();
        ServerSocket bankSocket = new ServerSocket(Main.bankPort);
        isListening = true;

        Platform.runLater(() -> lblConnectionInfo.setText(Main.returnNetworkInfo() + " Port: " + Main.bankPort));

        System.out.println("Bank online.");

        while (true)
        {
            Socket pipeConnection = bankSocket.accept();
            ObjectOutputStream bankOut = new ObjectOutputStream(pipeConnection.getOutputStream());
            ObjectInputStream bankIn = new ObjectInputStream(pipeConnection.getInputStream());
            Object object = bankIn.readObject();

            boolean needsReturnMessage = true;

            if (object instanceof Message)
            {
                Message incomingMessage = (Message) object;
                // Performing a withdrawl for an agent
                if (incomingMessage.getType() == MessageType.WITHDRAW)
                {
                    Account account = bank.getBankKeyToAccount().get(incomingMessage.getBankKey());
                    System.out.println("MSG RECV: WITHDRAW - FROM: " + account.getName());
                    // If we were able to deduct the bidding amount, then take it out, send a success back.
                    if (account.deductAccountBalance(incomingMessage.getBidAmount()))
                    {
                        incomingMessage.setBidResponse(BidResponse.ACCEPT);
                        System.out.println("Bank accepted withdrawl of " + incomingMessage.getBidAmount() + " on account:");
                    }
                    // If there wasn't enough money, send a rejection back.
                    else
                    {
                        incomingMessage.setBidResponse(BidResponse.REJECT);
                        System.out.println("Bank refused withdrawl of " + incomingMessage.getBidAmount() + " on account:");
                    }
                    System.out.println(account.toString());
                }
                //When we place a bid
                else if (incomingMessage.getType() == MessageType.PLACE_BID)
                {
                    Account account = bank.getBankKeyToAccount().get(incomingMessage.getBankKey());
                    System.out.println("\nRCV_MSG: PLACE_BID - FROM: " + account.getName());
                    // If we were able to deduct the bidding amount, then take it out, send a success back.
                    if (account.deductAccountBalance(incomingMessage.getBidAmount()))
                    {
                        incomingMessage.setBidResponse(BidResponse.ACCEPT);
                        incomingMessage.setType(MessageType.PLACE_HOLD);
                        bankOut.writeObject(incomingMessage);
                        System.out.println("Bank has placed a hold on account:");
                    }
                    // If there wasn't enough money, send a rejection back.
                    else
                    {
                        incomingMessage.setBidResponse(BidResponse.REJECT);
                        incomingMessage.setType(MessageType.PLACE_HOLD);
                        bankOut.writeObject(incomingMessage);
                        System.out.println("Bank has refused a hold on account:");
                    }
                    System.out.println(account.toString());
                }
                // Initializing an agent with an account (name, account#, balance, bankkey)
                else if (incomingMessage.getType() == MessageType.REGISTER_AGENT)
                {
                    System.out.println("\nRCV_MSG: REGISTER_AGENT - FROM: " + incomingMessage.getAccount().getName());
                    bank.registerAgent(incomingMessage.getAccount());
                }
                // If an agent goes offline it will unsubscribe itself from the bank.
                else if (incomingMessage.getType() == MessageType.UNREGISTER)
                {
                    ;
                    System.out.println("\nRCV_MSG: UNREGISTER - FROM: " + incomingMessage.getName());
                    bank.unregisterAgent(incomingMessage.getClientKey());
                    System.out.println("Agent " + incomingMessage.getName() + " un-registered.");
                    needsReturnMessage = false;
                }
                // removes hold from bank.
                else if (incomingMessage.getType() == MessageType.ITEM_SOLD)
                {
                    // ToDo Remove hold from bank account

                }
                else if (incomingMessage.getType() == MessageType.OUT_BID)
                {
                    // ToDo Remove hold from bank and put hold amount back in account

                }

                if (needsReturnMessage)
                {
                    bankOut.writeObject(incomingMessage);
                }

            }
            else
            {
                System.out.println("Bank received unrecognized message. Doing nothing"); // TODO: Will this hold the socket open?
            }


        }
    }

    /**
     * auctionCentralLaunch()
     * <p>
     * Launchs an Auction Central and opens a socket for listening to messages.
     *
     * @throws IOException            Can be thrown from bad input/output in the streams
     * @throws ClassNotFoundException Can be thrown from bad cast from readObject()
     */
    private void auctionCentralLaunch() throws IOException, ClassNotFoundException
    {
        auctionCentral = new AuctionCentral();
        ServerSocket auctionCentralSocket = new ServerSocket(Main.auctionCentralPort);

        isListening = true;
        Platform.runLater(() -> lblConnectionInfo.setText(Main.returnNetworkInfo() + " Port: " + Main.auctionCentralPort));
        System.out.println("Auction Central online.");

        while (true)
        {
            Socket otherPipeConnection = auctionCentralSocket.accept();
            ObjectOutputStream centralOut = new ObjectOutputStream(otherPipeConnection.getOutputStream());
            ObjectInputStream centralIn = new ObjectInputStream(otherPipeConnection.getInputStream());

            Object object = centralIn.readObject();
            if (object instanceof Message)
            {
                Message incomingMessage = (Message) object;

                boolean needsReturnMessage = true;

                // Updating the list of AHs to the agent
                if (incomingMessage.getType() == MessageType.UPDATE_AHS)
                {
                    incomingMessage.setListOfAHs(auctionCentral.getListOfAHs());
                }
                // Registering a new agent with AC
                else if (incomingMessage.getType() == MessageType.REGISTER_AGENT)
                {
                    System.out.println("\nRCV_MSG: REGISTER_AGENT - FROM: " + incomingMessage.getName());
                    String biddingKey = auctionCentral.registerAgent(incomingMessage.getName(), incomingMessage.getBankKey());
                    incomingMessage.setPortNumber(portNumber++);

                    incomingMessage.setBiddingKey(biddingKey);
                }
                // Registering a new AH with AC
                else if (incomingMessage.getType() == MessageType.REGISTER_AH)
                {
                    System.out.println("\nRCV_MSG: REGISTER_AH - FROM: " + incomingMessage.getAuctionHouse().getName());
                    auctionCentral.registerAuctionHouse(incomingMessage.getAuctionHouse());
                    System.out.println(incomingMessage.getAuctionHouse().getPublicID() + "is the id");

                }
                //if place bid is null then its from agent, anything else is from auctionhouse
                else if (incomingMessage.getType() == MessageType.PLACE_BID)
                {
                    if (incomingMessage.getBidResponse() == null)
                    {
                        System.out.println("\nRCV_MSG: PLACE_BID - FROM: bidKey-" + incomingMessage.getBiddingKey());
                        System.out.println(incomingMessage.getItem().getAhID());
                        Socket auctionHouseSocket = new Socket(staticAcHostname, 6000); // TODO: CHANGE THE LOCAL HOST?

                        out = new ObjectOutputStream(auctionHouseSocket.getOutputStream());
                        out.flush();
                        in = new ObjectInputStream(auctionHouseSocket.getInputStream());

                        System.out.println("SEND_MSG: PLACE_BID - TO: " + incomingMessage.getItem().getAhID());
                        out.writeObject(incomingMessage);


                        incomingMessage = (Message) in.readObject();
                        System.out.println("message came back from AH");

                        if (incomingMessage.getBidResponse() == BidResponse.REJECT)
                        {
                            centralOut.writeObject(incomingMessage);
                            System.out.println("AC says you didn't have enough");

                            Socket clientSocket = new Socket("127.0.0.1", 20000);
                            out = new ObjectOutputStream(clientSocket.getOutputStream());
                            in = new ObjectInputStream(clientSocket.getInputStream());

                            out.writeObject(incomingMessage);

                        }
                        else if (incomingMessage.getBidResponse() == BidResponse.ACCEPT)
                        {
                            System.out.println("\nRCV_MSG: PLACE_BID - FROM: bidKey-" + incomingMessage.getBiddingKey());
                            Socket bankSocket = new Socket(staticBankHostname, Main.bankPort);
                            ObjectOutputStream outToBank = new ObjectOutputStream(bankSocket.getOutputStream());
                            ObjectInputStream inFromBank = new ObjectInputStream(bankSocket.getInputStream());

                            incomingMessage.setBankKey(auctionCentral.getBiddingKeyToBankKey().get(incomingMessage.getBiddingKey()));
                            outToBank.writeObject(incomingMessage);
                            System.out.println("\nSEND_MSG: PLACE_BID - TO: Bank");


                            Message bankResponse = (Message) inFromBank.readObject();
                            bankResponse.setBiddingKey(auctionCentral.getBankKeyToBiddingKey().get(incomingMessage.getBankKey()));

                            if (bankResponse.getBidResponse() == BidResponse.ACCEPT)
                            {
                                //go to auction house
                                out.writeObject(bankResponse);
                                centralOut.writeObject(bankResponse);
                                System.out.println("successful bid that needs to go to auction house");
                            }
                            else if (bankResponse.getBidResponse() == BidResponse.REJECT)
                            {
                                //do nothing
                                out.writeObject(bankResponse);
                                centralOut.writeObject(bankResponse);
                                System.out.println("you didn't have enough money");
                            }
                            centralOut.writeObject(bankResponse);

                            // TODO: centralIn, centralOut agent->AC. in, out ah->AC

                        }

                    }


//                    needsReturnMessage = false;


                }
                // If an agent or AH goes down it will unsubscribe from the auction central
                else if (incomingMessage.getType() == MessageType.UNREGISTER)
                {
                    System.out.println("\nRCV_MSG: UNREGISTER - FROM: " + incomingMessage.getName());
                    if (!incomingMessage.isAgent())
                    {
                        auctionCentral.unregisterAuctionHouse(incomingMessage.getClientKey());
                        System.out.println("Auction House " + incomingMessage.getName() + " un-registered.");
                    }
                    else
                    {
                        System.out.println("Agent " + incomingMessage.getName() + " un-registered.");
                    }

                    needsReturnMessage = false;
                }
                // sends a message of ITEM_SOLD to bank and agent.
                else if (incomingMessage.getType() == MessageType.ITEM_SOLD)
                {
                    Socket bankSocket = new Socket(staticBankHostname, Main.bankPort);

                    out = new ObjectOutputStream(bankSocket.getOutputStream());
                    in = new ObjectInputStream(bankSocket.getInputStream());
                    String bankKey = auctionCentral.getBiddingKeyToBankKey().get(incomingMessage.getBiddingKey());
                    // Sending a message of type Item_Sold.
                    out.writeObject(new Message(MessageType.ITEM_SOLD, incomingMessage.getItemID(), incomingMessage.getAuctionHousePublicID(), bankKey, incomingMessage.getBidAmount()));
                    // ToDO make ac talk to agent.
                    needsReturnMessage = false;
                }
                else if (incomingMessage.getType() == MessageType.OUT_BID)
                {
                    Socket bankSocket = new Socket(staticBankHostname, Main.bankPort);

                    out = new ObjectOutputStream(bankSocket.getOutputStream());
                    in = new ObjectInputStream(bankSocket.getInputStream());

                    String bankKey = auctionCentral.getBiddingKeyToBankKey().get(incomingMessage.getBiddingKey());
                    // Sending a message of type OUT_BID.
                    out.writeObject(new Message(MessageType.OUT_BID, incomingMessage.getAuctionHousePublicID(), bankKey, incomingMessage.getBidAmount()));
                    // ToDO make ac talk to agent.
                    needsReturnMessage = false;
                }

                if (needsReturnMessage)
                {
                    centralOut.writeObject(incomingMessage);
                }


            }

        }
    }

    private int getPortNumber()
    {
        ++portNumber;
        return portNumber;
    }

    /**
     * setPeerConnection()
     * <p>
     * Connects a bank to an AC or an AC to a bank.
     *
     * @param peerHostname Hostname we are connecting to.
     */
    public static void setPeerConnection(String peerHostname)
    {
        if (isBank)
        {
            staticAcHostname = peerHostname;
            bankConnectedToAC = true;
            System.out.println("Initializing Auction Central to: " + staticAcHostname + ":" + Main.auctionCentralPort);
        }
        else
        {
            staticBankHostname = peerHostname;
            acConnectedToBank = true;
            System.out.println("Initializing Bank connection to: " + staticBankHostname + ":" + Main.bankPort);
        }
    }

    /**
     * main()
     * <p>
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