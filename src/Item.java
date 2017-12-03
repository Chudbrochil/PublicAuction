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
    private int ahID;      //IDs.ID of the AuctionHouse that holds this item
    public final Double MINIMUM_BID;         //Minimum bid to start at
    private Double currentBid;               //CurrentBid on this item--the highest of all the bids. Will be 0 until first bid.
    public final String ITEM_NAME;          //Name of the item
    private String currentHighestBidderID;    //BIDDING_ID of Agent who holds the currentBid, which is winning. Will be null until first bid.
    private String imgPath;
    private int itemID;            //Unique item ID

    // Copy constructor
    public Item(Item anotherItem)
    {
        this.ahID = anotherItem.ahID;
        this.MINIMUM_BID = anotherItem.MINIMUM_BID;
        this.currentBid = anotherItem.currentBid;
        this.ITEM_NAME = anotherItem.ITEM_NAME;
        this.currentHighestBidderID = anotherItem.currentHighestBidderID;
        this.imgPath = anotherItem.imgPath;
        this.itemID = anotherItem.itemID;
    }

    public Item(String itemName, String imgPath, double minimumBid)
    {
        ITEM_NAME = itemName;
        this.imgPath = imgPath;
        MINIMUM_BID = minimumBid;
        currentBid = 0.0;
        currentHighestBidderID = null;
    }

    public Item(String itemName, String imgPath, Double minimumBid, int itemID)
    {
        this(itemName, imgPath, minimumBid);
        this.itemID = itemID;
    }

    public void setItemID(int itemID) { this.itemID = itemID; }

    public int getItemID() { return itemID; }

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
     * @param newBiddingKey Agent who is making the bid of AMOUNT newBid
     */
    public synchronized void setCurrentBidAndBidder(Double newBid, String newBiddingKey)
    {
        currentBid = newBid;
        currentHighestBidderID = newBiddingKey;
    }


    public int getAhID()
    {
        return ahID;
    }

    public void setAhID(int ahID)
    {
        this.ahID = ahID;
    }

    @Override
    public String toString()
    {
        return ITEM_NAME + " itemID: " + itemID + " ahID: " + ahID;
    }
}
