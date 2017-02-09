package com.srinivas.mudavath.network.network;

/**
 * Created by Mudavath Srinivas on 09/02/17.
 */

public class HinduEditorials {

  public String url;
  public String publishedTime;
  public String modifiedTime;
  public String title;
  public String content;
  public String prevStoryUrl;
  public String nextStoryUrl;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getPublishedTime() {
    return publishedTime;
  }

  public void setPublishedTime(String publishedTime) {
    this.publishedTime = publishedTime;
  }

  public String getModifiedTime() {
    return modifiedTime;
  }

  public void setModifiedTime(String modifiedTime) {
    this.modifiedTime = modifiedTime;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getPrevStoryUrl() {
    return prevStoryUrl;
  }

  public void setPrevStoryUrl(String prevStoryUrl) {
    this.prevStoryUrl = prevStoryUrl;
  }

  public String getNextStoryUrl() {
    return nextStoryUrl;
  }

  public void setNextStoryUrl(String nextStoryUrl) {
    this.nextStoryUrl = nextStoryUrl;
  }

  @Override public String toString() {
    return "HinduEditorials{" +
        "url='" + url + '\'' +
        ", publishedTime='" + publishedTime + '\'' +
        ", modifiedTime='" + modifiedTime + '\'' +
        ", title='" + title + '\'' +
        ", content='" + content + '\'' +
        ", prevStoryUrl='" + prevStoryUrl + '\'' +
        ", nextStoryUrl='" + nextStoryUrl + '\'' +
        '}';
  }
}
