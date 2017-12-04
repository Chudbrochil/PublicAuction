import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Optional;

public class Main extends Application
{
    public static int bankPort = 4444;
    public static int auctionCentralPort = 5555;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        typeSelect(primaryStage);
    }

    /**
     * typeSelect()
     *
     * A dialog box appears that asks if you'd like an Agent, Auction House, Bank or Auction Central.
     * From there we open the corresponding FXML file that has the controller set inside it.
     *
     * @param primaryStage The stage that we are going to launch the GUI onto.
     * @throws IOException Throws if we fail to open the fxml file
     */
    private void typeSelect(Stage primaryStage) throws IOException
    {
        ArrayList<String> auctionDisplays = new ArrayList<>();
        auctionDisplays.add("Agent");
        auctionDisplays.add("Auction House");
        auctionDisplays.add("Bank");
        auctionDisplays.add("Auction Central");

        ChoiceDialog dialog = new ChoiceDialog(auctionDisplays.get(0), auctionDisplays);
        dialog.setTitle("Public Auction");
        dialog.setHeaderText("What auction display do you want?");
        Optional<String> result = dialog.showAndWait();

        // TODO: Doing string comparison here is probably bad...
        if(!result.isPresent() || result.get().equals("Agent"))
        {
            Parent root = FXMLLoader.load(getClass().getResource("AgentUI.fxml"));
            primaryStage.setTitle("Agent");
            primaryStage.setScene(new Scene(root, 700, 400));
        }
        else if(result.get().equals("Auction House"))
        {
            Parent root = FXMLLoader.load(getClass().getResource("AuctionHouseUI.fxml"));
            primaryStage.setTitle("Auction House");
            primaryStage.setScene(new Scene(root, 300, 600));
        }
        else if(result.get().equals("Bank"))
        {
            Parent root = FXMLLoader.load(getClass().getResource("BankUI.fxml"));
            primaryStage.setTitle("Bank");
            primaryStage.setScene(new Scene(root, 300, 600));
        }
        else if(result.get().equals("Auction Central"))
        {
            Parent root = FXMLLoader.load(getClass().getResource("AuctionCentralUI.fxml"));
            primaryStage.setTitle("Auction Central");
            primaryStage.setScene(new Scene(root, 300, 600));
        }

        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> System.exit(0));
    }

    /**
     * getStandardOutCapture()
     *
     * This returns an outputstream that will capture text and display it on a text area.
     * The text will generally be captured from standard out.
     *
     * @param taOutput TextArea we are writing to
     * @return OutputStream that gets fed into stdout method
     */
    public static OutputStream getStandardOutCapture(TextArea taOutput)
    {
        OutputStream out = new OutputStream()
        {
            @Override
            public void write(int b) throws IOException
            {
                appendText(String.valueOf((char)b));
            }

            public void appendText(String str)
            {
                Platform.runLater(() -> taOutput.appendText(str));
            }
        };
        return out;
    }

    /**
     * returnNetworkInfo()
     *
     * Gets hostname and IP for a process.
     *
     * @return A string corresponding to this process' Hostname and IP Address.
     */
    public static String returnNetworkInfo()
    {
        String output = "";
        try
        {
            InetAddress ipInfo = InetAddress.getLocalHost();
            output += "Hostname: " + ipInfo.getHostName() + "\n";
            output += "IP Address: " + ipInfo.getHostAddress();
        }
        catch(UnknownHostException e) { System.out.println(e.getMessage()); }
        return output;
    }

    /**
     * askName()
     *
     * Opens a dialog box that asks for a user's name. Then this method returns the entered name.
     * This is useful for both Auction House and Agent where a name will be used.
     *
     * @return The name entered in the dialog box.
     */
    public static String askName()
    {
        TextInputDialog dialog = new TextInputDialog("Name");
        dialog.setTitle("Enter your name");
        dialog.setContentText("Please enter your name.");
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent())
        {
            return result.get();
        }
        else
        {
            return "Nameless Object";
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }

}
