import java.util.HashMap;
import java.util.Random;

public class Bank {

    HashMap<Account,Integer > map = new HashMap<Account, Integer>();
    Random rand = new Random();

    public Bank(){

    }


    public void registerAgent(Agent agent){
        System.out.println("Accout name: " + agent.getName());
        agent.setAccountBalance(500);
        System.out.println("Account Balance: " + agent.getAccountBalance());
        agent.setAccountNum(rand.nextInt(1000) + 1);
        System.out.println("Account num: " + agent.getAccountNum());

        Account account = new Account(agent.getAccountNum(), (float) agent.getAccountBalance());
        map.put(account, agent.getAccountNum());
        
    }
}
