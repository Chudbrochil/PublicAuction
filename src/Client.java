import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args){
        Agent newUser = new Agent(args[0]);
        System.out.println("You've chosen " + newUser.getName() + " as your username" );
        try {
           Socket socket = new Socket("127.0.0.1", 4444);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(newUser);

            newUser = (Agent)in.readObject();

            System.out.println("Account Amount = " + newUser.getAccountBalance());
            System.out.println("Account Number = " + newUser.getAccountNum());
            out.close();
            in.close();
        } catch (Exception e) {

        }
    }
}
