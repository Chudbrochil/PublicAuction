import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Bank{
    ArrayList<Agent> registeredAgents = new ArrayList<>();

    public Bank(){
        Agent agent = null;
       Random rand = new Random();
        try{
            ServerSocket socket = new ServerSocket(4444);
         System.out.println("Welcome to the Bank");

            while(true) {
                Socket pipeConnection = socket.accept();

                ObjectOutputStream out = new ObjectOutputStream(pipeConnection.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(pipeConnection.getInputStream());

                agent = (Agent) in.readObject();
                agent.setAccountBalance(500);
                agent.setAccountNum(rand.nextInt(1000) + 1);
//        agent.setBankKey();

                out.writeObject(agent);

//                next line test out the Bank's ability to listen for an AuctionCentral
//                listenforAC(socket, pipeConnection, in, out);

            }

        }catch(Exception e){
            e.printStackTrace();
            e.getLocalizedMessage();
            e.getMessage();
        }
    }


    public void listenforAC(ServerSocket socket, Socket pipeConnection, ObjectInputStream in, ObjectOutputStream out){
        try {
            pipeConnection = socket.accept();
            out = new ObjectOutputStream(pipeConnection.getOutputStream());
            in = new ObjectInputStream(pipeConnection.getInputStream());


           Agent agent = (Agent) in.readObject();
            System.out.println("Request coming in from Auction Central...");
            System.out.println("Account amount = " + agent.getAccountBalance());
        }
        catch(Exception e){
         e.getMessage();
         e.getLocalizedMessage();
         e.printStackTrace();
        }
        }

public static void main(String[] args){
        Bank b = new Bank();
}







}
