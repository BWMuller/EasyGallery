package za.co.bwmuller.easygallery;

/**
 * Created by Bernhard Müller on 8/23/2017.
 */

public class Config {
    public String allImages = "All images";
    public String allVideos = "All videos";
    public int albumGridColumnCount = 2;
    public int mediaGridColumnCount = 3;
    public boolean autoRotateImages = false;
    public Scope loaderScope;
    public boolean enableCameraCapture = false;


    public enum Scope {
        ALL,
        IMAGES,
        VIDEOS
    }
}
