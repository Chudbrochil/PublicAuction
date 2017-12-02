/**
 * This is the object that can be sent between classes, storing information.
 * Check if a parameter is null. If it is and you're expecting to be able to use it, something has gone wrong.
 *
 */
public class Message
{
    MessageType type;
    
    //for type PLACE_BID
    public final String BIDDING_ID;         //BIDDING_ID of the Agent who wishes to place a bid
    public final double AMOUNT;            // Amount the bidder wishes to bid.
    public final String ITEM_ID;           //ID of the item the bidder wishes to bid on
    public final String AUCTION_HOUSE_ID;   //the ID of this auction house (needed by Client)
    
    //for type PLACE_HOLD
    //Everything for PLACE_BID and
    private BidResponse response;
    

    public Message(MessageType t, String biddingID, double amount, String itemID, String auctionHouseID)
    {
        type = t;
        BIDDING_ID = biddingID;
        AMOUNT = amount;
        ITEM_ID = itemID;
        AUCTION_HOUSE_ID = auctionHouseID;
    }
    
    /**
     * @param t Type of Message.
     * For when you want to keep the same items but need a different Message type.
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
    
}
