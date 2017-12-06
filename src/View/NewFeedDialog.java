package View;

import Domain.Feed;
import Utility.RssUtility;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jdom2.JDOMException;

import java.io.IOException;

public class NewFeedDialog{
    @FXML
    private TextField urlField;
    @FXML
    private Label errorLabel;

    private Stage dialogStage;
    private MainView mainView;

    void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    void setMainView(MainView mainView){
        this.mainView = mainView;
    }



    @FXML
    public void initialize(){

    }

    @FXML
    private void handleOk(){
        try{
            Feed newFeed = new Feed(urlField.getText());

            mainView.getItemsFromFeed(newFeed);
            mainView.getFeeds().add(newFeed);
            mainView.getFeedTable().getSelectionModel().select(newFeed);

            dialogStage.close();
        }catch(IOException | JDOMException ex){
            ex.printStackTrace();
            errorLabel.setText("Fail to parse URL");
        }
    }
}
