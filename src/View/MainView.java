package View;

import Domain.Feed;
import Utility.RssUtility;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import java.io.*;


public class MainView{

    private Stage primaryStage;

    //to be modified when load function is implemented
    private ObservableList<Feed> feeds = FXCollections.observableArrayList();

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

    TableView<Feed> getFeedTable(){
        return feedTable;
    }

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public void setFeeds(ObservableList<Feed> feeds){
        this.feeds = feeds;
        feedTable.setItems(feeds);
    }

    ObservableList<Feed> getFeeds(){
        return feeds;
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


        feedTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) ->
                {
                    try{
                        if(newValue != null){
                            getItemsFromFeed(newValue);
                        }
                    }catch(IOException | JDOMException ex){
                        itemTable.setItems(null);
                        showItemDetails(null);
                        ex.printStackTrace();
                    }
                });

        itemTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showItemDetails(newValue));
    }


    void getItemsFromFeed(Feed feed)throws IOException, JDOMException{

            itemTable.setItems(RssUtility.parseFeed(feed));
            showItemDetails(null);

    }

    private void showItemDetails(Element item){
        if(item != null){
            descriptionWeb.getEngine().loadContent(item.getChild("description").getText());
            linkArea.setText(item.getChild("link").getValue());
        }else {
            descriptionWeb.getEngine().loadContent("");
            linkArea.setText("");
        }
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
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("NewFeedDialog.fxml"));

            Stage dialogStage = new Stage();
            dialogStage.initOwner(primaryStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(fxmlLoader.load()));

            NewFeedDialog newFeedDialog = fxmlLoader.getController();
            newFeedDialog.setMainView(this);
            newFeedDialog.setDialogStage(dialogStage);

            dialogStage.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleExportXML(){
        Feed feed = feedTable.getSelectionModel().selectedItemProperty().get();
        if(feed != null){
            try{
                RssUtility.exportXML(feed);
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void handleImportXML(){
        try{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("XML", "*.xml"),
                    new FileChooser.ExtensionFilter("RSS", "*.rss"));

            File source = fileChooser.showOpenDialog(primaryStage);
            if(source != null){
                Feed newFeed = new Feed(source.toURI().toString());
                getItemsFromFeed(newFeed);
                getFeeds().add(newFeed);
                getFeedTable().getSelectionModel().select(newFeed);
            }
        }catch(IOException | JDOMException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteFeed(){
        Feed feed = feedTable.getSelectionModel().selectedItemProperty().get();
        if(feed != null){
            feeds.remove(feed);
            feedTable.getSelectionModel().select(null);
            itemTable.setItems(null);
            showItemDetails(null);
        }
    }
}
