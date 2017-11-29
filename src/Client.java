import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    public Client(String name) {
        Agent newUser = new Agent(name);
        System.out.println("You've chosen " + newUser.getName() + " as your username");
        try {
            Socket bankSocket = new Socket("127.0.0.1", 4444);
//            Socket centralSocket = new Socket("127.0.0.1", 5555);

            ObjectOutputStream out = new ObjectOutputStream(bankSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(bankSocket.getInputStream());

           registerAgent(out,in,newUser,bankSocket);

        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            e.getLocalizedMessage();
        }


        // TODO: A given Agent/AuctionHouse will call methods on it's "mailman" (client)
        // which will then create messages to send to other clients/servers (auction central, auction house)


    }

    public Client() {
        AuctionHouse ah = new AuctionHouse();
        System.out.println("You've created a new Auction House");

        try {
            Socket auctionCentralSocket = new Socket("127.0.0.1", 5555);

            ObjectOutputStream out = new ObjectOutputStream(auctionCentralSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(auctionCentralSocket.getInputStream());
            registerAH(out, in, ah);


        } catch (Exception e) {
            e.getLocalizedMessage();
            e.getMessage();
            e.printStackTrace();
        }
    }


    public void registerAgent(ObjectOutputStream out, ObjectInputStream in,Agent newUser, Socket centralSocket) {
        try {
            out.writeObject(newUser);

            newUser = (Agent) in.readObject();

            System.out.println("Account Amount = " + newUser.getAccountBalance());
            System.out.println("Account Number = " + newUser.getAccountNum());

            out = new ObjectOutputStream(centralSocket.getOutputStream());
            in = new ObjectInputStream(centralSocket.getInputStream());

            out.writeObject(newUser);

            newUser = (Agent) in.readObject();
            System.out.println("Bidding Key = " + newUser.getBiddingKey());

        } catch (Exception e) {
        e.getLocalizedMessage();
        e.getMessage();
        e.printStackTrace();
        }
    }

    public void registerAH(ObjectOutputStream out, ObjectInputStream in, AuctionHouse ah) {
        try {
            out.writeObject(ah);
            ah = (AuctionHouse) in.readObject();
        } catch (Exception e) {
            e.getMessage();
            e.getLocalizedMessage();
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        if (args[0].equals("Auction House")) {
            Client client = new Client();
        } else if (args[0].equals("Agent") && !args[1].equals(null)) {
            Client client = new Client(args[1]);
        }
    }
}
