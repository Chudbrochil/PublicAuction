import IDs.AuctionHouseID;

import java.util.ArrayList;

/**
 * Anna Carey will head the effort to implement this object.
 *
 * Instances of this class are dynamically created.
 * Registers at Auction Central by providing a name and by receiving
 *   --public IDs.ID,
 *   --secret auction key.
 * Has a fixed number of items to sell, and no more than three at a time.
 *   (each item has an auction house IDs.ID, item IDs.ID, minimum bid, and current bid.)
 * It receives bids and acknowledges them. (Agents provide their
 *   Successful bid: asks AuctionCentral to place/releaes a hold on the bidder's bank account
 *   for an amount equal to the bid in question
 * A bid is successful and 'wins' if it is not overtaken in 30 seconds.
 * For a successful/winning bid, the AuctionHouse requests that AuctionCentral transfer the money to the AuctionHouse.
 */

public class AuctionHouse
{
    private String name;
    private AuctionHouseID ID;
    private String secretKey; // Requested and received from Auction Central
    private ArrayList<Item> items;
    private AuctionCentral AUCTION_CENTRAL;
    
    public AuctionHouse(AuctionCentral AC)
    {
        AUCTION_CENTRAL = AC;
        AUCTION_CENTRAL.registerAuctionHouse(name);
        //get publicID from AuctionHouse and
    }
    
    public void placeHold(String biddingKey, float amount)
    {
    
    }






}
