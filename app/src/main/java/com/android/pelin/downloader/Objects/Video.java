package com.android.pelin.downloader.Objects;

/**
 * Created by pelin on 27.04.2016.
 */
public class Video {
    private  String imageUrl;
    private String url="";
    private String name="";
    private String path = "";
    private String imagePath = "";
    private int percentage = 0;

    private boolean downloaded = false ;

    public Video(String url , String imageUrl )
    {
        this.url = url;
        this.imageUrl = imageUrl;
        this.name = url.substring(url.lastIndexOf("/")+1);
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPercentage(int percentage)
    {
        this.percentage = percentage;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public int getPercentage() {
        return percentage;
    }

    @Override
    public String toString() {
        return name;

    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
