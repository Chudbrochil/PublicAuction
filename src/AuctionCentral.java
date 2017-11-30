import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AuctionCentral
{
    Bank bank;

    public AuctionCentral(){
        Agent agent = null;
        Random rand = new Random();
        HashMap<String, Integer> registeredUsers = new HashMap<>();
        try{
            ServerSocket socket = new ServerSocket(5555);
          System.out.println("Auction Central is online");


            while(true){
                Socket pipeConnection = socket.accept();

                ObjectOutputStream out = new ObjectOutputStream(pipeConnection.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(pipeConnection.getInputStream());

                agent = (Agent) in.readObject();
                registeredUsers.put(agent.getName(), agent.getAccountNum());
                agent.setBiddingKey(rand.nextInt(10000) + 1);

                out.writeObject(agent);
                //next lines were to test out the communicateBank function
//                Socket bankSocket  = new Socket("127.0.0.1", 4444);
//                communicateBank(agent, bankSocket, out);

//
            }
        }catch(Exception e){
            e.printStackTrace();
            e.getLocalizedMessage();
            e.getMessage();
        }
    }

    public void communicateBank(Agent agent,Socket bankSocket, ObjectOutputStream out){
       try {

           out = new ObjectOutputStream(bankSocket.getOutputStream());
           System.out.println("Sending request for Bank info");
           out.writeObject(agent);
       }
       catch(Exception e){
           e.printStackTrace();
           e.getLocalizedMessage();
           e.getMessage();
       }
    }

    // Returns true if it successfully registers an Agent and gives it a biddingKey
    public boolean registerAgent(String name, int bankKey)
    {
        return true;
    }

    
    public void registerAuctionHouse(AuctionHouse ah)
    {
        //todo: implement. See Bank.registerAgent(Agent agent)
    }

    // Talks to bank to place hold for a particular agent with a particular bidding key
    // Returns true if the placeHold was successful (i.e. the agent has the $ and is a valid person)
    public boolean placeHold(int biddingKey, float amount)
    {
        return true;
    }


public static void main(String[] args){
        AuctionCentral ac = new AuctionCentral();
}


}
