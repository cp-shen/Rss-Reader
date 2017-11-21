package View;

import Domain.Feed;
import Utility.RssUtility;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import java.io.IOException;

public class MainView{

    private Stage primaryStage;
    private ObservableList<Feed> feeds;

    @FXML
    private TableView<Element> itemTable;
    @FXML
    private TableView<Feed> feedTable;
    @FXML
    private TableColumn<Element, String> itemColumn,pubDateColumn;
    @FXML
    private TableColumn<Feed, String> feedNameColumn;
    @FXML
    private WebView descriptionWeb;
    @FXML
    private TextArea linkArea;

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public void initialize(){
        feedNameColumn.setCellValueFactory(
                param -> new SimpleStringProperty(param.getValue().getName()));

        itemColumn.setCellValueFactory(param -> {
            Element title = param.getValue().getChild("title");
            return new SimpleStringProperty(title.getValue());
        });
        pubDateColumn.setCellValueFactory(param ->{
            Element pubDate = param.getValue().getChild("pubDate");
            return new SimpleStringProperty(pubDate.getValue());
        });


        feedTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try{
                itemTable.setItems(RssUtility.parseRss(newValue.getUri()));
            }catch(JDOMException | IOException ex){
                ex.printStackTrace();
            }
        });

        itemTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showItemDetails(newValue));
    }

    public void setFeeds(ObservableList<Feed> feeds){
        this.feeds = feeds;
        feedTable.setItems(feeds);
    }

    private void showItemDetails(Element item){
        descriptionWeb.getEngine().loadContent(item.getChild("description").getText());
        linkArea.setText(item.getChild("link").getValue());
    }

    @FXML
    private void handleGO(){
        Element item = itemTable.getSelectionModel().getSelectedItem();
        if(item != null){
            try{
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("WebWindow.fxml"));

                Stage webStage = new Stage();
                webStage.setScene(new Scene(fxmlLoader.load()));

                WebWindow webWindowController = fxmlLoader.getController();
                webWindowController.setItemWeb(item.getChild("link").getValue());

                webStage.show();
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void handleSaveFeeds(){
        RssUtility.saveFeeds(feeds,"Feeds");
    }

    @FXML
    private void handleNewFeed(){

    }
}
