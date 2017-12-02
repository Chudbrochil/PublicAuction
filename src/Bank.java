import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Random;

public class Bank
{
    private HashMap<Integer, Account> accountNumberToAccount = new HashMap<>();
    private Random rand = new Random();

    public Bank()
    {

    }

    public HashMap<Integer, Account> getAccountNumberToAccountMap()
    {
        return accountNumberToAccount;
    }


    /**
     * registerAgent()
     * <p>
     * Initial registering of an agent by the bank.
     * The agent is given a starting balance, account number and bank key by the bank.
     *
     * @param blankAccount The account we are opening for the agent
     */
    public void registerAgent(String agentName, Account blankAccount)
    {
        int accountNum = rand.nextInt(10000 + 1);
        double startingBalance = 10000.00;
        System.out.println("Name: " + agentName);
        String bankKey = Bank.getKey(agentName);

        blankAccount.setAccountNum(accountNum);
        blankAccount.setAccountBalance(startingBalance);
        blankAccount.setBankKey(bankKey);

        System.out.println("\nRegistering a new user.....");
        System.out.println("Account Name: " + agentName);

        System.out.println("Starting Balance: " + blankAccount.getAccountBalance());
        System.out.println("Account Number: " + blankAccount.getAccountNum());
        System.out.println("Agent Bank Key: " + blankAccount.getBankKey());

        accountNumberToAccount.put(accountNum, blankAccount);

    }


    /**
     * getKey()
     * <p>
     * This is the method for creating a secret key. The bank holds this as it is the "most secure" resource.
     *
     * @param name This is the name passed to the method to generate the secret key
     * @return A "secret key". These will correspond to:
     * Agent gets a Bank key from the Bank
     * Agent gets a bidding key from the AuctionCentral
     * AuctionHouse gets an Auction key from the AuctionCentral
     */
    public static String getKey(String name)
    {
        String output = "";
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(name.getBytes(StandardCharsets.UTF_8));
            output = hash.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            System.out.println(e.getMessage());
        }
        return output;
    }


}
