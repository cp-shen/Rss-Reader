package Utility;

import Domain.Feed;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RssUtility{

    public static ObservableList<Element> parseFeed(Feed feed)throws IOException, JDOMException{
        SAXBuilder saxBuilder = new SAXBuilder();
        Document jdomDoc = saxBuilder.build(feed.getUri());
        Element rss = jdomDoc.getRootElement();
        Element channel = rss.getChild("channel");

        feed.setName(channel.getChild("title").getValue() + " " + channel.getChild("link").getValue());

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

    public static void exportXML(Feed feed)throws IOException{
        URL source = new URL(feed.getUri());
        File copy = new File(feed.getName().split(" ")[0]+
                "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd-kk-mm-ss")) + ".xml");

        if(copy.createNewFile()){
            InputStream in = source.openStream();
            FileOutputStream out = new FileOutputStream(copy);
            int temp;
            while( (temp = in.read()) != -1){
                out.write(temp);
            }
        }
    }
}
