package sound.chill.com.mychilloutplayer;


public class DataModel {


    String name;
    int image;

    public DataModel(String name, int image) {
        this.name = name;
        this.image=image;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

}