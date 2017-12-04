import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Anna Carey will head the effort to implement this object.
 * <p>
 * Instances of this class are dynamically created.
 * Registers at Auction Central by providing a name and by receiving
 * --public IDs.ID,
 * --secret auction key.
 * Has a fixed number of items to sell, and no more than three at a time.
 * (each item has an auction house IDs.ID, item IDs.ID, minimum bid, and current bid.)
 * It receives bids and acknowledges them. (Agents provide their
 * Successful bid: asks AuctionCentral to place/releaes a hold on the bidder's bank account
 * for an AMOUNT equal to the bid in question
 * A bid is successful and 'wins' if it is not overtaken in 30 seconds.
 * For a successful/winning bid, the AuctionHouse requests that AuctionCentral transfer the money to the AuctionHouse.
 * <p>
 * ***
 * To use this class, you MUST set the AuctionCentral via setAuctionCentral() method.
 * ***
 */

public class AuctionHouse implements Serializable
{
    private final String NAME;
    private int publicID;
    private String ahKey; // Requested and received from Auction Central
    private HashMap<Integer, Item> items; //Item ID as key for the item.
    private HashSet<PendingBidRequest> pendingHolds; //placeBid calls this AuctionHouse is waiting on holds for to respond to
                                             //the message.
    //private HashMap<String, Time> itemTimers;
    //private HashMap<>

    private int itemCounter = 0;

    /**
     * AuctionHouse()
     * Creates an AuctionHouse that has three random items for sale.
     *
     * @param name Name for this AuctionHouse
     */
    public AuctionHouse(String name)
    {
        NAME = name;
        items = new HashMap<>();
        pendingHolds = new HashSet();
        populateItems();
    }

    /**
     * populateItems()
     *
     * This method fills the auctionHouse with 3 random items. This needs to be done after the auction house is
     * registered so that the item can have it's auction house id set. This gives the item is "uniqueness"
     *
     */
    public void populateItems()
    {
        System.out.println("Populating " + NAME + " with 3 random items.");
        for (int i = 0; i < 3; ++i)
        {
            Item item = ItemDB.getRandomItem();
            item.setItemID(itemCounter);
            items.put(itemCounter, item);
            System.out.println(item.ITEM_NAME + " - Min price: " + item.MINIMUM_BID + " itemID: " + item.getItemID());
            itemCounter++;
        }
    }

    public HashMap<Integer, Item> getItems()
    {
        return items;
    }

    /**
     * Called by an IAuctionCentral to set the PublicID and SecretKey
     */
    public void setIDs(int publicID, String ahKey)
    {
        this.publicID = publicID;
        this.ahKey = ahKey;
        ArrayList<Item> listOfItems = new ArrayList<Item>(items.values());
        for(int i = 0; i < listOfItems.size(); ++i)
        {
            listOfItems.get(i).setAhID(this.publicID);
        }
    }

    public int getPublicID()
    {
        return publicID;
    }

    public String getName()
    {
        return NAME;
    }

    /**
     * placeBid()
     * Called by an Agent to place a bid (or by a Client when a PLACE_BID Message is received)
     * @param biddingID      BIDDING_ID of the Agent who wishes to place a bid
     * @param amount         Amount the bidder wishes to bid.
     * @param itemID         ID of the item the bidder wishes to bid on
     * @param auctionHouseID the ID of this auction house (needed by Client)
     * @return true if Client should move ahead and request a hold to be placed.
     *         false if something went wrong (not this AuctionHouse's ID, the item doesn't exist here,
     *         the Agent bid too little,) in which case the Client can send a bidResponse REJECT Message
     *         right back to the Agent.
     *         //todo: above
     */
    public boolean placeBid(String biddingID, double amount, String itemID, String auctionHouseID)
    {
        //Safechecking
        if(!auctionHouseID.equals(publicID))
        {
            //Not the right AuctionHouse USER OUTPUT
            System.err.println(toString()+" received a placeBid request for auctionHouseID "+auctionHouseID+" which does" +
                "not match its public ID "+publicID+". Returning.");
            return false;
        }
        Item item = items.get(itemID);
        if (item == null)
        {
            //That item isn't for sale here USER OUTPUT
            System.err.println("Bidding ID " + biddingID + " tried to bid on " + itemID + ", which is not an item in " +
                NAME + ". Returning");
            return false;
        }

        //If it's a valid bid AMOUNT
        if (amount >= item.MINIMUM_BID && amount > item.getCurrentBid())
        {
            //Agent didn't bid enough USER OUTPUT
            pendingHolds.add(new PendingBidRequest(biddingID, amount, itemID));
            return true;
        }
        //todo: else, USER OUTPUT whoops, bid more.
        else return false;
    }
    
    /**
     * processHoldResponse()
     * Called by Client when a REQUEST_HOLD Message is received.
     * @param biddingID      BIDDING_ID of the Agent who wishes to place a bid
     * @param amount         Amount the bidder wishes to bid.
     * @param itemID         ID of the item the bidder wishes to bid on
     * @param response       BidResponse of
     * @return  null if response was REJECT.
     *          biddingID of the person whose bid was surpassed otherwise. Client should send a REQUEST_BID
     *          BidResponse PASS to the returned biddingID and a REQUEST_BID BidResponseMessage ACCEPT to
     *          *this* biddingID
     *          //todo: above
     */
    public String processHoldResponse(String biddingID, double amount, String itemID, BidResponse response)
    {
        //todo: Anna: Check if pendingHolds holds it.
        if(response==BidResponse.REJECT) return null;
        
        else if(response==BidResponse.ACCEPT)
        {
            Item item = items.get(itemID);
            String prevBidWinner = item.getCurrentHighestBidderID();
            item.setCurrentBidAndBidder(amount, biddingID);
            restartBidTime(itemID);
            return prevBidWinner;
        }
        else
        {
            System.err.println(toString()+" got a hold response of BidResponse "+ response+". Should be ACCEPT or REJECT.");
            return null;
        }
    }
    
    /**
     * @param itemID Item whose timer is being reset
     */
    private void restartBidTime(String itemID)
    {
        //Timer timer = itemTimers.get(itemID);
        //items.
        //todo
    }


    private class PendingBidRequest
    {
        public final String BIDDING_ID;
        public final double AMOUNT;
        public final String ITEM_ID;
        
        public PendingBidRequest(String bID, double amt, String iID)
        {
            BIDDING_ID = bID;
            AMOUNT = amt;
            ITEM_ID = iID;
        }
        
        @Override
        public boolean equals(Object obj)
        {
            if(obj instanceof PendingBidRequest)
            {
                PendingBidRequest br = (PendingBidRequest)obj;
                return this.BIDDING_ID == br.BIDDING_ID && this.AMOUNT == br.AMOUNT && this.ITEM_ID == br.ITEM_ID;
            }
            else return false;
        }
        
        //TODO: override hash to use in Set.
    }
    
    
    private static class ItemDB
    {
        private static ArrayList<Item> items;

        // Static initializer that always loads the filelist
        static
        {
            items = new ArrayList<>();

            try
            {
                InputStream inputFile = ItemDB.class.getResourceAsStream("ItemList.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputFile, "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null)
                {
                    // This allows for commenting in the ItemList
                    if (!line.startsWith("//") && !line.trim().isEmpty())
                    {
                        // TODO: ItemList.txt is fragile, don't give it bad input
                        String[] elements = line.split(",");
                        String itemName = elements[0];
                        String imgPath = elements[1];
                        Double minimumBid = Double.valueOf(elements[2]);
                        items.add(new Item(itemName, imgPath, minimumBid));
                    }
                }
                inputFile.close();
            }
            catch (IOException e)
            {
                System.out.println(e.getMessage());
            }
        }

        private static Item getRandomItem()
        {
            // I have to create a copy of the original object so that we don't end up with duplicate
            // objects if we random pick the same item.
            return new Item(items.get(ThreadLocalRandom.current().nextInt(0, items.size())));
        }
    }

}
