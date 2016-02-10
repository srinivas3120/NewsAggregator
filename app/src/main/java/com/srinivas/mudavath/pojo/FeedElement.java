package com.srinivas.mudavath.pojo;

/**
 * Created by Mudavath Srinivas on 08-02-2016.
 */
public class FeedElement {

    private String title;
    private String author;
    private String category;
    private String link;
    private String thumbnail;
    private String description;
    private String pubDate;


    public FeedElement(){

    }

    public FeedElement(String title, String author, String category, String link,String thumbnail, String description, String pubDate) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.link = link;
        this.thumbnail=thumbnail;
        this.description = description;
        this.pubDate = pubDate;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
