package com.knimbus.elib;

public class CommonListing {

    private String title,author,type,url;
    private String size;

    CommonListing(String title, String author, String type, String url, String size){
        this.title = title;
        this.author = author;
        this.type = type;
        this.url = url;
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSize() {

        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
