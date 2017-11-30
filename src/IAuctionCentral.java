/**
 * Methods AuctionCentral is expected to implement
 *
 * This can be an actual AuctionCentral or a Client object
 *           that 'pretends' to be an AuctionCentral and passes messages to a Server, which then passes them to the
 *           actual AuctionCentral and calls the appropriate method.
 */

public interface IAuctionCentral
{
    //Gives ah a Public ID and Secret Auction Key.
    //Also stores AuctionHouse in its register so that it can be given out to Agents who request a list of AH's later.
    public void registerAuctionHouse(AuctionHouse ah);
    
    
}
