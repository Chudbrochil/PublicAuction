import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Agent implements Serializable
{
    private Bank bank;
    private AuctionCentral central;
    private ArrayList<AuctionHouse> auctionHouses;

    private int accountNum;
    private String bankKey, name;
    private String biddingKey;
    private double accountBalance;


    public Agent(String name)
    {
        this.name = name;
    }

    // Returns true if bid is successful
    public boolean placeBid()
    {

        return true;
    }

    // Is ran upon initialization of an agent, registers with bank
    // and gets back accountNum, bankKey, accountBalance
    private void getBankKey()
    {

    }

    public void setBankKey(String bankKey){
        this.bankKey = bankKey;
    }

    // This gets called on auction central to get the latest list of auctionhouses;
    private void getUpdatedAHList()
    {

    }

    // Loops over the auctionHouses
    private void getAHItems()
    {

    }

    public void setBiddingKey(String newKey){
        biddingKey = newKey;
    }

    public String getBiddingKey(){
        return biddingKey;
    }

    public String getName(){
        return this.name;
    }

    public void setAccountNum(int accountNum){
        this.accountNum = accountNum;
    }

    public int getAccountNum(){
        return accountNum;
    }


    public void setAccountBalance(int num){
        this.accountBalance = num;
    }

    public double getAccountBalance()
    {
        return accountBalance;
    }


}