package Utility;

import Domain.Feed;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.*;
import java.util.List;

public class RssUtility{

    public static ObservableList<Element> parseRss(String xmlSource)throws IOException, JDOMException{
        SAXBuilder saxBuilder = new SAXBuilder();
        Document jdomDoc = saxBuilder.build(xmlSource);
        Element rss = jdomDoc.getRootElement();
        Element channel = rss.getChild("channel");
        List<Element> items = channel.getChildren("item");
        return FXCollections.observableArrayList(items);
    }

    public static ObservableList<Feed> loadFeeds(String fileUrl){
        try{
            ObservableList<Feed> feeds = FXCollections.observableArrayList();

            File file = new File(fileUrl);
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));

            int size = (Integer)in.readObject();

            for(int i=0; i<size; i++){
                feeds.add((Feed)in.readObject());
            }

            return feeds;
        }catch(IOException | ClassNotFoundException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static void saveFeeds(List<Feed> feeds, String fileUrl){
        try{
            File file = new File(fileUrl);
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));

            out.writeObject(feeds.size());

            for(Feed feed : feeds){
                out.writeObject(feed);
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
