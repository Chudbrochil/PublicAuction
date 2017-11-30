
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


                    Bank bank = new Bank();

                    Agent agent;
                    agent = (Agent)in.readObject();

                    bank.registerAgent(agent);
                    out.writeObject(agent);
                }
                else if(num == 5555){

                    AuctionCentral ac = new AuctionCentral();
                    AuctionHouse ah;
                    ah = (AuctionHouse) in.readObject();
                    ac.registerAuctionHouse(ah);
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
