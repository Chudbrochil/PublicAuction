// This is a class for the bank to hold in a map i.e. HashMap<BankKey, Account>

import java.io.Serializable;

public class Account implements Serializable
{
    private int accountNum;
    private Double accountBalance = 0.0;
    private Double temporaryHold = 0.0;
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

    public boolean deductAccountBalance(Double amountToDeduct)
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
    public boolean deductFromHold(Double amountToDeduct)
    {
        if(temporaryHold >= amountToDeduct)
        {
            temporaryHold -= amountToDeduct;
            return true;
        }
        else return false;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void setAccountNum(int accountNum)
    {
        this.accountNum = accountNum;
    }

    public int getAccountNum()
    {
        return accountNum;
    }

    public void setBankKey(String bankKey) { this.bankKey = bankKey; }

    public String getBankKey() { return bankKey; }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "Name: " + name + " acct#: " + accountNum + " Balance: " + accountBalance + " Hold: "+ temporaryHold;
    }
    
    /**
     * placeHold()
     * Call when a bid is valid at the AH.
     * @param amntToHold Amount to be placed in temporaryHold.
     * @return true if there was enough in accountBalance and the amount was moved to temporaryHold
     *         false if there was not enough in accountBalance and no money was moved.
     */
    public boolean placeHold(double amntToHold)
    {
        if(accountBalance >= amntToHold)
        {
            System.out.println("In account, you had enough money.");
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
    public boolean releaseHold(double amntToRelease)
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
