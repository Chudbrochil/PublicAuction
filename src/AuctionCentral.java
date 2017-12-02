import java.util.ArrayList;
import java.util.HashMap;

public class AuctionCentral
{
    private HashMap<String, String> biddingKeyToBankKey;
    private ArrayList<AuctionHouse> listOfAHs;
    private static int auctionHouseID = 0;

    public AuctionCentral()
    {
        biddingKeyToBankKey = new HashMap<>();
        listOfAHs = new ArrayList<>();

    }

    public String registerAgent(String agentName, String bankKey)
    {
        System.out.println("\nRegistering a new user...");
        System.out.println("Name: " + agentName);

        // TODO: If you try to register an agent with AC before bank, this might throw an error
        String biddingKey = Bank.getKey(agentName + bankKey);
        biddingKeyToBankKey.put(biddingKey, bankKey);
        System.out.println("Bidding Key: " + biddingKey + "\n");
        return biddingKey;
    }


    public void registerAuctionHouse(AuctionHouse auctionHouse)
    {
        auctionHouseID++;
        String auctionHouseKey = Bank.getKey(auctionHouse.getName());
        auctionHouse.setIDs(auctionHouseID, auctionHouseKey);
        listOfAHs.add(auctionHouse);
        System.out.println("Auction house is registered...");
        System.out.println("ID: " + auctionHouseID + " AH Key: " + auctionHouseKey);


        System.out.println("Current list of Auction Houses:");
        for(int i = 0; i < listOfAHs.size(); ++i)
        {
            System.out.println("AH Name: " + listOfAHs.get(i).getName() + " - ID:" + listOfAHs.get(i).getPublicID());
        }
    }

    public ArrayList<AuctionHouse> getListOfAHs() { return listOfAHs; }

}
