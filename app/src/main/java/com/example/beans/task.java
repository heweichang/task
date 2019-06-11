package com.example.beans;

/**
 * Created by 何伟昌 on 2018/4/8.
 */
public class task {

    private String task_id;
    private String task_name;
    private String task_date;
    private String task_addr;
    private String task_req;
    private String task_content;
    private String task_over;
    private String user_name;
    private String task_comment;
    private String task_latitude;
    private String task_longitude;

    public task(String task_id,String task_name,String task_date,String task_addr,String task_req,String task_content,String task_over,String user_name,String task_comment,String task_latitude,String task_longitude){
        this.task_id=task_id;
        this.task_name=task_name;
        this.task_date=task_date;
        this.task_addr=task_addr;
        this.task_req=task_req;
        this.task_content=task_content;
        this.task_over=task_over;
        this.user_name=user_name;
        this.task_comment=task_comment;
        this.task_latitude=task_latitude;
        this.task_longitude=task_longitude;
    }
    public task(String task_id,String task_name,String task_content,String task_date,String task_addr,String task_req){
        this.task_id=task_id;
        this.task_name=task_name;
        this.task_content=task_content;
        this.task_date=task_date;
        this.task_addr=task_addr;
        this.task_req=task_req;
    }

    public String getTask_latitude() {
        return task_latitude;
    }

    public void setTask_latitude(String task_latitude) {
        this.task_latitude = task_latitude;
    }

    public String getTask_longitude() {
        return task_longitude;
    }

    public void setTask_longitude(String task_longitude) {
        this.task_longitude = task_longitude;
    }

    public String getTask_comment() {
        return task_comment;
    }
    public void setTask_comment(String task_comment) {
        this.task_comment = task_comment;
    }
    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getTask_over() {
        return task_over;
    }

    public void setTask_over(String task_over) {
        this.task_over = task_over;
    }
    public String getTask_addr() {
        return task_addr;
    }

    public String getTask_content() {
        return task_content;
    }

    public void setTask_content(String task_content) {
        this.task_content = task_content;
    }

    public String getTask_req() {
        return task_req;
    }

    public void setTask_req(String task_req) {
        this.task_req = task_req;
    }

    public void setTask_addr(String task_addr) {
        this.task_addr = task_addr;
    }

    public String getTask_name() {
        return task_name;
    }

    public String getTask_date() {
        return task_date;
    }

    public void setTask_date(String task_date) {
        this.task_date = task_date;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

}
