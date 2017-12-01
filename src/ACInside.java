import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Vincent on 11/29/2017.
 */
public class ACInside
{
  Bank bank;
  ArrayList<Agent> registeredAgents = new ArrayList<>();
  ArrayList<AuctionHouse> registeredHouse = new ArrayList<>();
  HashMap<String, Integer> bid2Bank = new HashMap<String, Integer>();
  HashMap<Integer, String> bank2Bid = new HashMap<Integer, String>();
  HashMap<String, String> id2AH = new HashMap<String, String>();
  String bidKey = "0";
  String houseId = "0";
  String secretKey = "0";
  // Returns true if it successfully registers an Agent and gives it a biddingKey
  public boolean registerAgent(Agent agent)
  {
    if(!(registeredAgents.contains(agent)))
    {
      agent.setBiddingKey(bidKey);
      bid2Bank.put(bidKey, agent.getAccountNum());
      bank2Bid.put(agent.getAccountNum(), bidKey);
      return true;
    }
    return false;
  }

  // Returns true if it successfully registers an AuctionHouse and gives it a public IDs.ID
  public void registerAuctionHouse(AuctionHouse house)
  {
    house.setIDs(houseId, secretKey);
    id2AH.put(houseId, secretKey);
  }

  // Talks to bank to place hold for a particular agent with a particular bidding key
  // Returns true if the placeHold was successful (i.e. the agent has the $ and is a valid person)
  public boolean placeHold(Agent agent, Double amount)
  {
    //if(agent has money and is valid)
    //return true
    return false;
  }

  public String getBidKey(Agent agent)
  {
   return bank2Bid.get(agent.getAccountNum());
  }
  public int getBankKey(Agent agent)
  {
    return bid2Bank.get(agent.getBiddingKey());
  }

  public void sendMessage(Message message)
  {

  }

  public void receiveMessage(Message message)
  {

  }
}
