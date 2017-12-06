import Utility.RssUtility;
import View.MainView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jdom2.JDOMException;

import java.io.IOException;

public class Main extends Application{

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("View/MainView.fxml"));

            primaryStage.setScene(new Scene(fxmlLoader.load()));
            MainView mainView = fxmlLoader.getController();
            mainView.setPrimaryStage(primaryStage);

            mainView.setFeeds(RssUtility.loadFeeds("Feeds"));

            primaryStage.setTitle("RSS Reader By BJTU16301130");
            primaryStage.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
