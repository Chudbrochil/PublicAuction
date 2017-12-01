import java.util.HashMap;
import java.util.Random;

public class Bank
{


    HashMap<Integer, Account> map = new HashMap<Integer, Account>();
    Random rand = new Random();


    public Bank()
    {

    }

    public HashMap<Integer, Account> getMap() {
        return map;
    }


    public void registerAgent(Agent agent)
    {

        System.out.println("Registering a new user.....");
        System.out.println("Account name: " + agent.getName());
        agent.setAccountBalance(500);
        System.out.println("Account Balance: " + agent.getAccountBalance());
        agent.setAccountNum(rand.nextInt(1000) + 1);
        System.out.println("Account num: " + agent.getAccountNum() + "\n");

        Account account = new Account(agent.getAccountNum(), agent.getAccountBalance());
        map.put(agent.getAccountNum(), account);

    }
}
