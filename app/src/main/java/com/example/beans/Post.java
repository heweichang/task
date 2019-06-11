package com.example.beans;

/**
 * Created by 何伟昌 on 2018/5/22.
 */
public class Post {

    private String postid;
    private String posttitle;
    private String postcontent;
    private String posttime;
    private String userid;
    private String username;
    private String userimage;
    private String postimage;

    public Post(String postid, String posttitle, String postcontent, String posttime, String userid, String username, String userimage, String postimage) {
        this.postid = postid;
        this.posttitle = posttitle;
        this.postcontent = postcontent;
        this.posttime = posttime;
        this.userid = userid;
        this.username = username;
        this.userimage = userimage;
        this.postimage = postimage;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPosttitle() {
        return posttitle;
    }

    public void setPosttitle(String posttitle) {
        this.posttitle = posttitle;
    }

    public String getPostcontent() {
        return postcontent;
    }

    public void setPostcontent(String postcontent) {
        this.postcontent = postcontent;
    }

    public String getPosttime() {
        return posttime;
    }

    public void setPosttime(String posttime) {
        this.posttime = posttime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postid='" + postid + '\'' +
                ", posttitle='" + posttitle + '\'' +
                ", postcontent='" + postcontent + '\'' +
                ", posttime='" + posttime + '\'' +
                ", userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                ", userimage='" + userimage + '\'' +
                ", postimage='" + postimage + '\'' +
                '}';
    }
}
