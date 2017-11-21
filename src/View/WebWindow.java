package View;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;

public class WebWindow{
    @FXML
    private WebView itemWeb;

    public void initialize(){

    }

    void setItemWeb(String url){
        itemWeb.getEngine().load(url);
    }

}
