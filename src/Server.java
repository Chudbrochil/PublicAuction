
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server {


    //constructor for  bank
    public Server(int num) {


        //do bank registration for agent

        try {
            ServerSocket socket = new ServerSocket(num);

            while(true){
                Socket pipeConnection = socket.accept();
                ObjectOutputStream out = new ObjectOutputStream(pipeConnection.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(pipeConnection.getInputStream());

                if(num == 4444){
                    System.out.println("Bank Online");
                    Bank bank = new Bank();

                    Agent agent;
                    agent = (Agent)in.readObject();

                    bank.registerAgent(agent);
                    out.writeObject(agent);
//                    System.out.println("Where do we reside");

                }
                else if(num == 5555){
System.out.println("Auction Central Online");
                    //AuctionCentral ac = new AuctionCentral();
                    //AuctionHouse ah;
                    //ah = (AuctionHouse) in.readObject();
                    //register acution houses
                    //out.writeObject(ah)

                }




            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            e.getLocalizedMessage();
        }


    }



    public static void main(String[] args){
        if(args[0].equals("Bank")){
            Server s = new Server(4444);
        } else if(args[0].equals("AuctionCentral")){
            Server s = new Server(5555);
        }
    }


}
