import IDs.AuctionHouseID;

/**
 * This class is a data class that holds the information related to a particular auction item.
 *
 * AUCTION_HOUSE_ID, MINIMUM_BID, and ITEM_NAME are never changed once the item is created, so these fields are final and public.
 * currentBid is set to 0 when the object is created and will be set when the first valid bid is made.
 * currentHighestBidderID is set to null when the object is first created and will be set when the first valid bid is made.
 */

public class Item
{
    public final AuctionHouseID AUCTION_HOUSE_ID;      //IDs.ID of the AuctionHouse that holds this item
    public final float MINIMUM_BID;         //Minimum bid to start at
    private float currentBid;               //CurrentBid on this item--the highest of all the bids. Will be 0 until first bid.
    public final String ITEM_NAME;          //Name of the item
    private String currentHighestBidderID;    //biddingID of Agent who holds the currentBid, which is winning. Will be null until first bid.
    
    /**
     * Item()
     * @param auctionHouseID IDs.ID of the AuctionHouse that holds this item
     * @param minimumBid Minimum bid of this item
     * @param itemName Name of this item
     * Creates an Item with given parameters, a currentBid of 0, and a currentHighestBidderID of null.
     */
    public Item (AuctionHouseID auctionHouseID, float minimumBid, String itemName)
    {
        AUCTION_HOUSE_ID = auctionHouseID;
        MINIMUM_BID = minimumBid;
        ITEM_NAME = itemName;
        
        currentBid = 0;
        currentHighestBidderID = null;
    }
    
    public synchronized float getCurrentBid() { return currentBid; }
    
    public synchronized String getCurrentHighestBidderID() { return currentHighestBidderID; }
    
    /**
     * setCurrentBidAndBidder()
     * @param newBid
     * @param newBiddingKey Agent who is making the bid of amount newBid
     */
    public synchronized void setCurrentBidAndBidder(float newBid, String newBiddingKey)
    {
        currentBid = newBid;
        currentHighestBidderID = newBiddingKey;
    }


}
