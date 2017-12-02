import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is the object that can be sent between classes, storing information.
 * Check if a parameter is null. If it is and you're expecting to be able to use it, something has gone wrong.
 *
 */
public class Message implements Serializable
{
    //General needs
    private MessageType type;
    private BidResponse response;
    
    //for type PLACE_BID
    public final String BIDDING_ID;         //BIDDING_ID of the Agent who wishes to place a bid
    public final double BIDDING_AMOUNT;            // Amount the bidder wishes to bid.
    public final String ITEM_ID;           //ID of the item the bidder wishes to bid on
    public final String AUCTION_HOUSE_ID;   //the ID of this auction house (needed by Client)
    
    //for type WITHDRAW
    public final int ACCOUNT_NUM;

    //for type UPDATE_AHS
    private ArrayList<AuctionHouse> listOfAHs;

    //for type REGISTER_AGENT
    private Account account;
    private String name;

    //for type REGISTER_AH

    
    //for type PLACE_HOLD
    //Everything for PLACE_BID and
    //private BidResponse response;
    
    //Constructor for a PLACE_BID message
    public Message(MessageType t, String biddingID, double amount, String itemID, String auctionHouseID)
    {
        type = t;
        BIDDING_ID = biddingID;
        BIDDING_AMOUNT = amount;
        ITEM_ID = itemID;
        AUCTION_HOUSE_ID = auctionHouseID;

        //Unused with this Message type
        ACCOUNT_NUM = 0;
    }
    
    //Constructor for a WITHDRAW message
    public Message(MessageType t, int accountNum, double amount)
    {
        type = t;
        ACCOUNT_NUM = accountNum;
        BIDDING_AMOUNT = amount;
    
        //Unused with this Message type
        ITEM_ID = null;
        AUCTION_HOUSE_ID = null;
        BIDDING_ID = null;
    }

    // Constructor for a REGISTER_AGENT message
    public Message(MessageType t, String name, Account account)
    {
        type = t;

        // not used
        ACCOUNT_NUM = 0;
        BIDDING_AMOUNT = 0;
        BIDDING_ID = null;
        ITEM_ID = null;
        AUCTION_HOUSE_ID = null;
    }

    //Constructor for UPDATE_AHS message
    public Message(MessageType t, ArrayList<AuctionHouse> listOfAHs)
    {
        type = t;
        listOfAHs = new ArrayList<>();

        BIDDING_AMOUNT = 0.0;
        ITEM_ID = null;
        AUCTION_HOUSE_ID = null;
        BIDDING_ID = null;
        ACCOUNT_NUM = 0;
    }
    
    /**
     * setType()
     * @param t Type of Message.
     * For when you want to keep the same field values but need a different Message type.
     */
    public void setType(MessageType t)
    {
        type = t;
    }
    
    /**
     * getType()
     * @return Get the type of message so you can respond to it correctly
     */
    public MessageType getType()
    {
        return type;
    }
    
    public void setBidResponse(BidResponse r){response = r;}
    
    public BidResponse getBidResponse() { return response; }

    public ArrayList<AuctionHouse> getListOfAHs() { return listOfAHs; }

    public void setListOfAHs(ArrayList<AuctionHouse> listOfAHs) { this.listOfAHs = listOfAHs; }

    public Account getAccount() { return account; }

    public void setAccount(Account account) { this.account = account; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

}
