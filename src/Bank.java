import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Bank{
    ArrayList<Agent> registeredAgents = new ArrayList<>();


public static void main(String[] args){
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
        }
//        out.close();
//        in.close();
    }catch(Exception e){

    }

}







}
