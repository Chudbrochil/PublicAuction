import java.util.HashMap;
import java.util.Random;

public class AuctionCentral
{


    HashMap<AuctionHouse, String> map = new HashMap<AuctionHouse, String>();
    Random random = new Random();

    public AuctionCentral()
    {

    }

    public void registerAgent(Agent agent)
    {
        System.out.println("Registering a new user...");
        System.out.println("User name: " + agent.getName());

        // TODO: If you try to register an agent with AC before bank, this might do something weird.
        agent.setBiddingKey(Bank.getKey(agent.getBankKey() + agent.getName()));
        System.out.println("Bidding key: " + agent.getBiddingKey() + "\n");


    }


    public void registerAuctionHouse(AuctionHouse ah)
    {
        map.put(ah, ah.getName());
        ah.setIDs("public Id", "Secret key");
        System.out.println("Auction house is registered...");
        //todo: implement. See Bank.registerAgent(Agent agent)
    }

    // Talks to bank to place hold for a particular agent with a particular bidding key
    // Returns true if the placeHold was successful (i.e. the agent has the $ and is a valid person)
    public boolean placeHold(int biddingKey, Double amount)
    {
        return true;
    }


    public HashMap<AuctionHouse, String> getMap()
    {
        return map;
    }

}
