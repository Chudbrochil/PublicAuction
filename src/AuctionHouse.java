import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
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
 * for an amount equal to the bid in question
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
    private String publicID;
    private String secretKey; // Requested and received from Auction Central
    private HashMap<String, Item> myItems; //Item ID as key for the item.
    //private HashMap<String, Time>
    //private HashMap<>
    private Client myClient;
    //private static int AuctionHouseID;

    /**
     * AuctionHouse()
     * Creates an AuctionHouse that has three random items for sale.
     *
     * @param name   Name for this AuctionHouse

     */
    public AuctionHouse(String name)
    {
        NAME = name;
//        myClient = client;
        myItems = new HashMap<>();

        //Give me items!
        for (int i = 0; i < 3; ++i)
        {
            Item item = ItemDB.getRandomItem();
            myItems.put(item.ITEM_ID, item);
            System.out.println(item.ITEM_NAME);
        }
    }

    /**
     * Called by an IAuctionCentral to set the PublicID and SecretKey
     */
    public void setIDs(String publicid, String secretkey)
    {
        publicID = publicid;
        secretKey = secretkey;
    }

    public void placeHold(String biddingKey, Double amount)
    {

    }

    public String getName()
    {
        return NAME;
    }

    /**
     * placeBid()
     * Called by an Agent to place a bid (or by a Client acting on behalf of an Agent)
     *
     * @param biddingID      biddingID of the Agent who wishes to place a bid
     * @param amount         Amount the bidder wishes to bid.
     * @param itemID         ID of the item the bidder wishes to bid on
     * @param auctionHouseID the ID of this auction house (needed by Client)
     */
    public void placeBid(String biddingID, double amount, String itemID, String auctionHouseID)
    {
        Item item = myItems.get(itemID);
        if (item == null)
        {
            //That item isn't for sale here USER OUTPUT
            System.err.println("Bidding ID " + biddingID + " tried to bid on " + itemID + ", which is not an item in " + NAME + ".");
            return;
        }

        //If it's a valid bid amount
        if (amount >= item.MINIMUM_BID && amount > item.getCurrentBid())
        {
            //Agent didn't bid enough USER OUTPUT
            //if(placeHold())
        }
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
                    if (!line.startsWith("//"))
                    {
                        // TODO: This has no error checking against bad files, don't mess with the file :-)
                        // TODO: Will error on input not like: string,string,Double and extra blank lines at end of file
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
            return items.get(ThreadLocalRandom.current().nextInt(0, items.size()));
        }
    }

}
