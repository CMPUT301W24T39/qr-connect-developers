package com.example.qrconnect;

/**
 * This class stores the name and url of an image
 */
public class ImageInfo {
    public String name;
    public String url;

    public ImageInfo() {
    }

    /**
     * The constructor of this class
     * @param name the file name of the image
     * @param url the url of the image
     */
    public ImageInfo(String name, String url) {
        this.name = name;
        this.url = url;
    }

    /**
     * Get the name of the image
     * @return the file name of the image
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the image
     * @param name the file name of the image
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the url of the image
     * @return the url of the image
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set the url of the image
     * @param url the url of the image
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
