import IDs.AuctionHouseID;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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
        this();
        AUCTION_CENTRAL = AC;
        //AUCTION_CENTRAL.registerAuctionHouse(name);
        //get publicID from AuctionHouse and
    }
    public AuctionHouse()
    {
        for(int i = 0; i < 3; ++i)
        {
            System.out.println(ItemDB.getRandomItem().ITEM_NAME);
        }
    }
    
    public void placeHold(String biddingKey, float amount)
    {
    
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
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputFile, "UTF-8" ));
                String line;
                while((line = reader.readLine()) != null)
                {
                    // This allows for commenting in the ItemList
                    if(!line.startsWith("//"))
                    {
                        // TODO: This has no error checking against bad files, don't mess with the file :-)
                        // TODO: Will error on input not like: string,string,float and extra blank lines at end of file
                        String[] elements = line.split(",");
                        String itemName = elements[0];
                        String imgPath = elements[1];
                        Float minimumBid = Float.valueOf(elements[2]);
                        items.add(new Item(itemName, imgPath, minimumBid));
                    }
                }
                inputFile.close();
            }
            catch(IOException e) { System.out.println(e.getMessage()); }
        }

        private static Item getRandomItem()
        {
            return items.get(ThreadLocalRandom.current().nextInt(0, items.size()));
        }


    }






}
