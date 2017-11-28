import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    public Client(String name){
        Agent newUser = new Agent(name);
        System.out.println("You've chosen " + newUser.getName() + " as your username" );
        try {
            Socket socket = new Socket("127.0.0.1", 4444);
            Socket centralSocket = new Socket("127.0.0.1", 5555);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(newUser);

            newUser = (Agent)in.readObject();

            System.out.println("Account Amount = " + newUser.getAccountBalance());
            System.out.println("Account Number = " + newUser.getAccountNum());

            out = new ObjectOutputStream(centralSocket.getOutputStream());
            in = new ObjectInputStream(centralSocket.getInputStream());

            out.writeObject(newUser);

            newUser = (Agent) in.readObject();
            System.out.println("Bidding Key = " + newUser.getBiddingKey());

        } catch (Exception e) {

        }
    }
    public static void main(String[] args){

       Client client = new Client(args[0]);
    }
}
