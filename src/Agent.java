import java.util.ArrayList;

public class Agent
{


    Bank bank;
    AuctionCentral central;
    ArrayList<AuctionHouse> auctionHouses;

    private int accountNum;
    private String bankKey;
    private String biddingKey;
    private double accountBalance;


    // Returns true if bid is successful
    private boolean placeBid()
    {
        return true;
    }

    // Is ran upon initialization of an agent, registers with bank
    // and gets back accountNum, bankKey, accountBalance
    private void getBankKey()
    {

    }

    // This gets called on auction central to get the latest list of auctionhouses;
    private void getUpdatedAHList()
    {

    }

    // Loops over the auctionHouses
    private void getAHItems()
    {

    }

}
