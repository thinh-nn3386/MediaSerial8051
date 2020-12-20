package entity;

import java.util.ArrayList;
import java.util.List;

public class Media {
    public static final String IMAGE = "src/fxml/image/";
    public static final String MP3 = "src/fxml/mp3/";
    private String imagePath;
    private String mp3Path;
    private String name;

    public Media(String image, String mp3){
        this.imagePath = IMAGE + image;
        this.mp3Path = MP3 + mp3;
        this.name = String.valueOf(mp3.toCharArray(),0,mp3.indexOf('.'));
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getMp3Path() {
        return mp3Path;
    }

    public List getALLMedia(){
        ArrayList list = new ArrayList();
        Media media1 = new Media("bigcityboi.jpg","BigCityBoi.mp3");
        list.add(media1);
        Media media2 = new Media("danchaua.jpg","DanChauADaDiVoBar.mp3");
        list.add(media2);
        Media media3 = new Media("nanabumkit.jpg","NaNaBumChit.mp3");
        list.add(media3);
        return list;
    }
}
