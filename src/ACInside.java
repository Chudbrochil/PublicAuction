import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Vincent on 11/29/2017.
 */
public class ACInside
{
//  Bank bank;
//  ArrayList<Agent> registeredAgents = new LinkedList<>();
//  ArrayList<Agent> registeredHouse = new LinkedList<>();
//  HashMap<Integer, Integer> bank2Bid = new HashMap<Integer, Integer>();
//  HashMap<Integer, Integer> bid2Bank = new HashMap<Integer, Integer>();
//  HashMap<Integer, AuctionHouse> id2AH = new HashMap<Integer, AuctionHouse>();
//  int bidKey = 0;
//  int houseId = 0;
  // Returns true if it successfully registers an Agent and gives it a biddingKey
  public boolean registerAgent(Agent agent)
  {
//    if(!(registeredAgents.contains(agent)))
//    {
//      agent.setBiddingKey(bidKey);
//      bank2Bid.put(agent.getAccountNum(), bidKey);
//      bid2Bank.put(bidKey, agent.getAccountNum());
//      return true;
//    }
    return false;
  }

  // Returns true if it successfully registers an AuctionHouse and gives it a public IDs.ID
  public boolean registerAuctionHouse(AuctionHouse house)
  {
//    if(registeredHouse.contains(house))
//    {
//      house.setID(houseId);
//      id2AH.put(houseId,house);
//      return true;
//    }
    return false;
  }

  // Talks to bank to place hold for a particular agent with a particular bidding key
  // Returns true if the placeHold was successful (i.e. the agent has the $ and is a valid person)
  public boolean placeHold(Agent agent, float amount)
  {
    //if(agent has money and is valid)
    //return true
    return false;
  }

  public void sendMessage(Message message)
  {

  }

  public void receiveMessage(Message message)
  {

  }
}
