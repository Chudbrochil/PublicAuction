import java.util.ArrayList;

public class AuctionCentral
{
    Bank bank;
    ArrayList<Agent> registeredAgents;

    // Returns true if it successfully registers an Agent and gives it a biddingKey
    public boolean registerAgent(String name, int bankKey)
    {
        return true;
    }
    
    // Returns true if it successfully registers an AuctionHouse and gives it a public IDs.ID
    public boolean registerAuctionHouse(String name)
    {
        return true;
    }

    // Talks to bank to place hold for a particular agent with a particular bidding key
    // Returns true if the placeHold was successful (i.e. the agent has the $ and is a valid person)
    public boolean placeHold(int biddingKey, float amount)
    {
        return true;
    }





}
