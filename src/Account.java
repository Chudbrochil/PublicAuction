import java.util.ArrayList;


// This is a class for the bank to hold in a map i.e. HashMap<BankKey, Account>

public class Account
{
    private long accountNum;
    private Double amount;
    private Double temporaryHold;

    /**
     * Account initial constructor
     * @param accountNum
     * @param amount
     */
    public Account(long accountNum, Double amount)
    {
        this.accountNum = accountNum;
        this.amount = amount;
    }

    public boolean deductAmount(Double amountToDeduct)
    {
        if(amount - amountToDeduct < 0)
        {
            return false;
        }
        else
        {
            amount -= amountToDeduct;
            return true;
        }
    }

    // TODO: Implement "hold" mechanism


}
