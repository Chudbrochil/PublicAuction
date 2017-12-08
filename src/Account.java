// This is a class for the bank to hold in a map i.e. HashMap<BankKey, Account>

import java.io.Serializable;

public class Account implements Serializable
{
    private int accountNum;
    private Double accountBalance;
    private Double temporaryHold;
    private String bankKey;
    private String name;

    public Account(String name)
    {
        this.setName(name);
    }


    /**
     * Account initial constructor
     *
     * @param accountNum
     * @param accountBalance
     */
    public Account(int accountNum, Double accountBalance, String bankKey, String name)
    {
        this.accountNum = accountNum;
        this.accountBalance = accountBalance;
        this.bankKey = bankKey;
        this.setName(name);
    }

    public synchronized boolean deductAccountBalance(Double amountToDeduct)
    {
        if (accountBalance - amountToDeduct < 0)
        {
            return false;
        }
        else
        {
            accountBalance -= amountToDeduct;
            return true;
        }
    }
    
    /**
     * deductFromHold()
     * Use this method when deducting from bids that have been won.
     * This pulls from the amount on hold rather than the account amount.
     * @param amountToDeduct Amount of the successful, winning bid.
     * @return true of amount was successfully deducted.
     *         false if there were not enough funds.
     */
    public synchronized boolean deductFromHold(Double amountToDeduct)
    {
        if(temporaryHold >= amountToDeduct)
        {
            temporaryHold -= amountToDeduct;
            return true;
        }
        else return false;
    }

    public synchronized Double getAccountBalance() {
        return accountBalance;
    }

    public synchronized void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public synchronized void setAccountNum(int accountNum)
    {
        this.accountNum = accountNum;
    }

    public synchronized int getAccountNum()
    {
        return accountNum;
    }

    public synchronized void setBankKey(String bankKey) { this.bankKey = bankKey; }

    public synchronized String getBankKey() { return bankKey; }

    public synchronized String getName()
    {
        return name;
    }

    public synchronized void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "Name: " + name + " acct#: " + accountNum + " Balance: " + accountBalance + " HoldAmount: "+ temporaryHold;
    }
    
    /**
     * placeHold()
     * Call when a bid is valid at the AH.
     * @param amntToHold Amount to be placed in temporaryHold.
     * @return true if there was enough in accountBalance and the amount was moved to temporaryHold
     *         false if there was not enough in accountBalance and no money was moved.
     */
    public synchronized boolean placeHold(double amntToHold)
    {
        if(accountBalance >= amntToHold)
        {
            accountBalance -= amntToHold;
            temporaryHold += amntToHold;
            return true;
        }
        else return false;
        
    }
    
    /**
     * releaseHold()
     * Used if PASS is sent to AC. //todo implement PASS
     * @param amntToRelease Amount to be released from temporaryHold
     * @return true if there was enough in temporaryHold and the amount was moved to accountBalance
     *         false if there was not enough in temporaryHold and no money was moved.
     */
    public synchronized boolean releaseHold(double amntToRelease)
    {
        if(temporaryHold >= amntToRelease)
        {
            temporaryHold -= amntToRelease;
            accountBalance += amntToRelease;
            return true;
        }
        else return false;
    }

}
