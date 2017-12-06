package Domain;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    public void setName(String name){
        this.name.set(name);
    }

    public Feed(String uri){
        this.uri = new SimpleStringProperty(uri);
        this.name = new SimpleStringProperty("");
    }

    private void readObject(ObjectInputStream in)throws IOException, ClassNotFoundException{
        uri = new SimpleStringProperty((String)in.readObject());
        name = new SimpleStringProperty((String)in.readObject());
    }

    private void writeObject(ObjectOutputStream out)throws IOException{
        out.writeObject(uri.get());
        out.writeObject(name.get());
    }
}
