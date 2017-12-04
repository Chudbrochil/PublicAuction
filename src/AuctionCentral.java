import java.util.ArrayList;
import java.util.HashMap;

public class AuctionCentral
{

    private HashMap<String, String> biddingKeyToBankKey;
    private HashMap<String, String> bankKeyToBiddingKey;

    private ArrayList<AuctionHouse> listOfAHs;
    private static int auctionHouseID = 0;

    /**
     * auctionCentral()
     *
     * Standard constructor for auction central.
     */
    public AuctionCentral()
    {
        biddingKeyToBankKey = new HashMap<>();
        bankKeyToBiddingKey = new HashMap<>();
        listOfAHs = new ArrayList<>();

    }

    /**
     * registerAgent()
     *
     * Registers an agent with the auction central. This gives the agent back a bidding key.
     * The intention is that the agent has already registered with the bank as it needs to give the
     * auction central it's bank key to get back a bidding key.
     *
     * @param agentName Name of the agent being registered with auction central.
     * @param bankKey Bank key of th agent being registered. Gets mapped to a bidding key.
     * @return The bidding key assigned to the agent.
     */
    public String registerAgent(String agentName, String bankKey)
    {
        System.out.println("Agent " + agentName + " registered.");
        String biddingKey = Bank.getKey(agentName + bankKey);
        biddingKeyToBankKey.put(biddingKey, bankKey);
        bankKeyToBiddingKey.put(bankKey, biddingKey);
        return biddingKey;
    }

    /**
     * registerAuctionHouse()
     *
     * Registers an auction house with auction central. Auction central holds a list of AHs that an agent will
     * want to retrieve. This is also where the auction house receives it's public id and auction house key
     *
     * @param auctionHouse The auction house that needs to register with the auction central.
     */
    public void registerAuctionHouse(AuctionHouse auctionHouse)
    {
        String auctionHouseKey = Bank.getKey(auctionHouse.getName());
        auctionHouse.setIDs(auctionHouseID, auctionHouseKey);

        listOfAHs.add(auctionHouse);
        System.out.println("Auction House " + auctionHouse.getName() + " registered.");
        auctionHouseID++;
    }

    /**
     * getListOfAHs()
     *
     * Retrieves the list of auction houses registered with auction central.
     *
     * @return List of auction houses.
     */
    public ArrayList<AuctionHouse> getListOfAHs() { return listOfAHs; }

    public String getListOfAHsAsString()
    {
        String output = "";
        for(int i = 0; i < listOfAHs.size(); ++i)
        {
            output += listOfAHs.toString() + "\n";
        }
        return output;
    }

    public HashMap<String, String> getBiddingKeyToBankKey() {
        return biddingKeyToBankKey;
    }

    public HashMap<String, String> getBankKeyToBiddingKey() {
        return bankKeyToBiddingKey;
    }
}

