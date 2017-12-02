// This is a class for the bank to hold in a map i.e. HashMap<BankKey, Account>

public class Account
{
    private long accountNum;
    private Double balance;
    private Double temporaryHold;

    /**
     * Account initial constructor
     *
     * @param accountNum
     * @param balance
     */
    public Account(long accountNum, Double balance)
    {
        this.accountNum = accountNum;
        this.balance = balance;
    }

    public boolean deductAmount(Double amountToDeduct)
    {
        if (balance - amountToDeduct < 0)
        {
            return false;
        }
        else
        {
            balance -= amountToDeduct;
            return true;
        }
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    // TODO: Implement "hold" mechanism


}
