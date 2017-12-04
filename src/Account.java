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


    // TODO: Implement "hold" mechanism


}
