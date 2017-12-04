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
    public final double BIDDING_AMOUNT;            // Amount the bidder wishes to bid.
    public final String ITEM_ID;           //ID of the item the bidder wishes to bid on
    private String auctionHousePublicID;

    //for type UPDATE_AHS
    private ArrayList<AuctionHouse> listOfAHs;

    //for type REGISTER_AGENT
    private Account account;
    private String name;
    private String bankKey;
    private String biddingKey; // Also for place_bid

    //for type REGISTER_AH
    private AuctionHouse auctionHouse;

    
    //for type PLACE_HOLD
    //Everything for PLACE_BID and
    //private BidResponse response;
    
    //Constructor for a PLACE_BID message
    public Message(MessageType t, String biddingKey, double amount, String itemID, String auctionHousePublicID)
    {
        type = t;
        this.biddingKey = biddingKey;
        BIDDING_AMOUNT = amount;
        ITEM_ID = itemID;
        auctionHousePublicID = auctionHousePublicID;

    }
    
    //Constructor for a WITHDRAW message
    public Message(MessageType t, String bankKey, double amount)
    {
        type = t;
        this.bankKey = bankKey;
        BIDDING_AMOUNT = amount;
    
        //Unused with this Message type
        ITEM_ID = null;
    }

    // Constructor for a REGISTER_AGENT message (to Bank)
    public Message(MessageType t, String name, Account account)
    {
        type = t;
        this.name = name;
        this.account = account;

        // not used
        BIDDING_AMOUNT = 0;
        ITEM_ID = null;
    }

    //Constructor for a REGISTER_AGENT message (to AC)
    public Message(MessageType t, String name, String bankKey, String biddingKey)
    {
        type = t;

        this.name = name;
        this.bankKey = bankKey;
        this.biddingKey = biddingKey;

        // not used
        ITEM_ID = null;
        BIDDING_AMOUNT = 0;
    }

    //Constructor for a REGISTER_AH message (to AC)
    public Message(MessageType t, AuctionHouse auctionHouse)
    {
        type = t;
        this.setAuctionHouse(auctionHouse);

        // not used
        BIDDING_AMOUNT = 0;
        ITEM_ID = null;
    }

    //Constructor for UPDATE_AHS message
    public Message(MessageType t, ArrayList<AuctionHouse> listOfAHs)
    {
        type = t;
        listOfAHs = new ArrayList<>();

        // not used
        BIDDING_AMOUNT = 0.0;
        ITEM_ID = null;
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


    /**
     * Getters/setters for private members encapsulated in messages
     */

    public ArrayList<AuctionHouse> getListOfAHs() { return listOfAHs; }

    public void setListOfAHs(ArrayList<AuctionHouse> listOfAHs) { this.listOfAHs = listOfAHs; }

    public Account getAccount() { return account; }

    public void setAccount(Account account) { this.account = account; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getBankKey() { return bankKey; }

    public void setBankKey(String bankKey) { this.bankKey = bankKey; }

    public String getBiddingKey() { return biddingKey; }

    public void setBiddingKey(String biddingKey) { this.biddingKey = biddingKey; }

    public AuctionHouse getAuctionHouse() { return auctionHouse; }

    public void setAuctionHouse(AuctionHouse auctionHouse) { this.auctionHouse = auctionHouse; }
}
