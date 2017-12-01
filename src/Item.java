import java.io.Serializable;

/**
 * This class is a data class that holds the information related to a particular auction item.
 * <p>
 * AUCTION_HOUSE_ID, MINIMUM_BID, and ITEM_NAME are never changed once the item is created, so these fields are final and public.
 * currentBid is set to 0 when the object is created and will be set when the first valid bid is made.
 * currentHighestBidderID is set to null when the object is first created and will be set when the first valid bid is made.
 */

public class Item implements Serializable
{
    public int AUCTION_HOUSE_ID;      //IDs.ID of the AuctionHouse that holds this item
    public final Double MINIMUM_BID;         //Minimum bid to start at
    private Double currentBid;               //CurrentBid on this item--the highest of all the bids. Will be 0 until first bid.
    public final String ITEM_NAME;          //Name of the item
    private String currentHighestBidderID;    //biddingID of Agent who holds the currentBid, which is winning. Will be null until first bid.
    private String imgPath;
    public final String ITEM_ID;            //Unique item ID
    private static int staticIDCounter = 1;


    public Item(String itemName, String imgPath, Double minimumBid)
    {
        ITEM_NAME = itemName;
        this.imgPath = imgPath;
        MINIMUM_BID = minimumBid;
        currentBid = 0.0;
        currentHighestBidderID = null;
        ITEM_ID = staticIDCounter * 42 + ""; //actually make it look like a real ID, sorta.
        staticIDCounter++;
    }

    public synchronized Double getCurrentBid()
    {
        return currentBid;
    }

    public synchronized String getCurrentHighestBidderID()
    {
        return currentHighestBidderID;
    }

    /**
     * setCurrentBidAndBidder()
     *
     * @param newBid
     * @param newBiddingKey Agent who is making the bid of amount newBid
     */
    public synchronized void setCurrentBidAndBidder(Double newBid, String newBiddingKey)
    {
        currentBid = newBid;
        currentHighestBidderID = newBiddingKey;
    }


}
