package com.example.beans;

/**
 * Created by 何伟昌 on 2019/4/10.
 */
public class Reply {

    private String replyid;
    private String replycontent;
    private String replyctime;
    private String postid;
    private String username;
    private String userid;

    public Reply(String replyid, String replycontent, String replyctime, String postid, String username, String userid) {
        this.replyid = replyid;
        this.replycontent = replycontent;
        this.replyctime = replyctime;
        this.postid = postid;
        this.username = username;
        this.userid = userid;
    }

    public Reply(String username, String replycontent) {
        this.username = username;
        this.replycontent = replycontent;
    }

    public String getReplyid() {
        return replyid;
    }

    public void setReplyid(String replyid) {
        this.replyid = replyid;
    }

    public String getReplycontent() {
        return replycontent;
    }

    public void setReplycontent(String replycontent) {
        this.replycontent = replycontent;
    }

    public String getReplyctime() {
        return replyctime;
    }

    public void setReplyctime(String replyctime) {
        this.replyctime = replyctime;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "replyid='" + replyid + '\'' +
                ", replycontent='" + replycontent + '\'' +
                ", replyctime='" + replyctime + '\'' +
                ", postid='" + postid + '\'' +
                ", username='" + username + '\'' +
                ", userid='" + userid + '\'' +
                '}';
    }
}
