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

    public void registerAgent(Agent agent)
    {
        System.out.println("\nRegistering a new user...");
        System.out.println("Name: " + agent.getName());

        // TODO: If you try to register an agent with AC before bank, this might throw an error
        String biddingKey = Bank.getKey(agent.getBankKey() + agent.getName());
        agent.setBiddingKey(biddingKey);
        biddingKeyToBankKey.put(biddingKey, agent.getBankKey());
        System.out.println("Bidding Key: " + agent.getBiddingKey() + "\n");
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
            System.out.println(listOfAHs.get(i).getName() + " - ID:" + listOfAHs.get(i).getPublicID());
        }
    }

    public ArrayList<AuctionHouse> getListOfAHs() { return listOfAHs; }

}
