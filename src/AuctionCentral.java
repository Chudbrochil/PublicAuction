import java.util.HashMap;
import java.util.Random;

public class AuctionCentral {


    HashMap<AuctionHouse, String> map = new HashMap<AuctionHouse, String>();
    Random random = new Random();
    public AuctionCentral(){

    }
    public void registerAgent(Agent agent)
    {
        System.out.println("Registering a new user...");
        System.out.println("User name: " + agent.getName());
        agent.setBiddingKey(random.nextInt(1000) + 1);
        System.out.println("Bidding key: " + agent.getBiddingKey() + "\n");


    }


    public void registerAuctionHouse(AuctionHouse ah)
    {
        map.put(ah, ah.getName());
        ah.setIDs("public Id", "Secret key");
        //todo: implement. See Bank.registerAgent(Agent agent)
    }

    // Talks to bank to place hold for a particular agent with a particular bidding key
    // Returns true if the placeHold was successful (i.e. the agent has the $ and is a valid person)
    public boolean placeHold(int biddingKey, float amount)
    {
        return true;
    }


    public HashMap<AuctionHouse, String> getMap() {
        return map;
    }

}
