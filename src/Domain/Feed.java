package Domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

public class Feed implements Serializable{
    private StringProperty uri;
    private StringProperty name;


    public String getUri(){
        return uri.get();
    }

    public String getName(){
        return name.get();
    }

    public Feed(String uri, String name){
        this.uri = new SimpleStringProperty(uri);
        this.name = new SimpleStringProperty(name);
    }
}
