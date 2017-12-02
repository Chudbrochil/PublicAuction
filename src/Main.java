import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;

public class Main extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        typeSelect(primaryStage);
    }

    private void typeSelect(Stage primaryStage) throws Exception
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
            primaryStage.setTitle("PublicAuction Agent");
            primaryStage.setScene(new Scene(root, 600, 400));
        }
        else if(result.get().equals("Auction House"))
        {
            Parent root = FXMLLoader.load(getClass().getResource("AuctionHouseUI.fxml"));
            primaryStage.setTitle("PublicAuction AH");
            primaryStage.setScene(new Scene(root, 600, 400));
        }
        else if(result.get().equals("Bank"))
        {
            Parent root = FXMLLoader.load(getClass().getResource("BankUI.fxml"));
            primaryStage.setTitle("PublicAuction Bank");
            primaryStage.setScene(new Scene(root, 300, 600));
        }
        else if(result.get().equals("Auction Central"))
        {
            Parent root = FXMLLoader.load(getClass().getResource("AuctionCentralUI.fxml"));
            primaryStage.setTitle("PublicAuction Auction Central");
            primaryStage.setScene(new Scene(root, 300, 600));
        }

        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> System.exit(0));
    }




    // TODO: Make a UI for AuctionHouse?


    public static void main(String[] args)
    {
        launch(args);
    }

}
