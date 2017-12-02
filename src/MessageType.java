/**
 * If you create a Message, assign it a type. Those who receive messages will respond based on the MessageType.
 */

public enum MessageType
{
    /**
     * Agent to AuctionHouse:
     *      "Hey, what items do you have?" (Establish socket communication.)
     *      "I want to place a bid on Item ____. This is my Bidding Key."
     *      "I am no longer interested in your items. Goodbye!" //closes.
     *
     * AuctionHouse to Agent:
     *      (Response) "These are my items and their minimum bids!"
     *      (Response) Accept, Reject (Item, amount)
     *      Pass, Winner (Item, amount)
     *      "Hey, I'm going out of business." //closes. CLOSE_SOCKET_REQUEST
     *
     * AuctionHouse to AuctionCentral
     *      "Please place a hold on BidderID account for amount" //PLACE_HOLD, null boolean, BidderID, amount
     *      "I'm shutting down!" CLOSE_SOCKET_REQUEST //Every time AuctionCentral gets one of these, it pings agents? to say "These houses closed!"
     *                                                  //but it doesn't have a reference... :(
     * AuctionCentral to Bank
     *      "Please place a hold on BankKey account for amount." //PLACE_HOLD, null boolean, BankKey, amount
     *
     * Bank to AuctionCentral
     *      "I placed a hold on their account." //PLACE_HOLD, true boolean, BankKey, amount
     *      "I cannot place said hold." //PLACE_HOLD, false boolean, BankKey, amount
     *
     * AuctionCentral to AuctionHouse
     *      "I placed a hold on their account." //PLACE_HOLD, true boolean, BidderID, amount
     *      "I cannot place said hold." //PLACE_HOLD, false boolean, BidderID, amount
     *
     */
}
