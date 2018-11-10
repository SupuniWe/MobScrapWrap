package com.db.cdap.scrapwrap.community;

public class Posts
{
    private String uid;
    private String time;
    private String date;
    private String description;
    private String post;
    private String fullName;
    private String postUrl;


    public Posts()
    {


    }

    public Posts(String uid, String time, String date, String description, String fullName, String postUrl) {
        this.uid = uid;
        this.time = time;
        this.date = date;
        this.description = description;
        //this.post = post;
        this.fullName = fullName;
        this.setPostUrl(postUrl);
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }
}
