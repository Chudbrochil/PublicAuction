import java.io.Serializable;
import java.util.ArrayList;

public class Agent
{
    private ArrayList<AuctionHouse> auctionHouses;
    private int accountNum;
    private String bankKey, name;
    private String biddingKey;
    private double accountBalance;
    private int portNumber;


    public Agent(String name)
    {
        auctionHouses = new ArrayList<>();
        this.name = name;
    }

    public void setAccountInfo(Account account)
    {
        accountNum = account.getAccountNum();
        accountBalance = account.getAccountBalance();
        bankKey = account.getBankKey();

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

    public void setAccountBalance(Double num)
    {
        this.accountBalance = num;
    }

    public void deductAccountBalance(Double num) { accountBalance -= num; }

    public double getAccountBalance() { return accountBalance; }

    public String getName()
    {
        return this.name;
    }

    public ArrayList<AuctionHouse> getAuctionHouses() {
        return auctionHouses;
    }


    public void setAuctionHouses(ArrayList<AuctionHouse> auctionHouses) {
        this.auctionHouses = auctionHouses;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

}
