import java.io.Serializable;
import java.util.ArrayList;

public class Agent implements Serializable
{
    private Bank bank;
    private AuctionCentral central;
    private ArrayList<AuctionHouse> auctionHouses;
    private boolean registered = false;
    private int accountNum;
    private String bankKey, name;
    private String biddingKey;
    private double accountBalance;


    public Agent(String name)
    {
        this.name = name;
    }

    // Returns true if bid is successful
    public boolean placeBid(double bidAmt)
    {
        // TODO: For now just doing a withdrawal from bank
        accountBalance -= bidAmt;
        return true;
    }


    // This gets called on auction central to get the latest list of auctionhouses;
    private void getUpdatedAHList()
    {

    }

    // Loops over the auctionHouses
    private void getAHItems()
    {

    }

    public void setBankKey(String bankKey)
    {
        this.bankKey = bankKey;
    }

    public String getBankKey() { return bankKey; }

    public void setBiddingKey(String newKey)
    {
        biddingKey = newKey;
    }

    public String getBiddingKey()
    {
        return biddingKey;
    }

    public void setAccountNum(int accountNum)
    {
        this.accountNum = accountNum;
    }

    public int getAccountNum()
    {
        return accountNum;
    }

    public void setAccountBalance(int num)
    {
        this.accountBalance = num;
    }

    public double getAccountBalance() { return accountBalance; }

    public String getName()
    {
        return this.name;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

}
