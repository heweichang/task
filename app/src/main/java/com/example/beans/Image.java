package com.example.beans;

/**
 * Created by 何伟昌 on 2019/3/7.
 */
public class Image {

    private String idimage;
    private String imagepath;
    private String imagecheck;
    private String taskid;


    public Image(String imagepath,String taskid) {
        super();
        // TODO Auto-generated constructor stub
        this.imagepath=imagepath;
        this.taskid=taskid;
    }
    public Image(String imagepath) {
        super();
        // TODO Auto-generated constructor stub
        this.imagepath=imagepath;
    }
    public String getImagepath() {
        return imagepath;
    }
    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }
    public String getImagecheck() {
        return imagecheck;
    }
    public void setImagecheck(String imagecheck) {
        this.imagecheck = imagecheck;
    }
    public String getTaskid() {
        return taskid;
    }
    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getIdimage() {
        return idimage;
    }
    public void setIdimage(String idimage) {
        this.idimage = idimage;
    }
    @Override
    public String toString() {
        return "Image_path [idimage=" + idimage + ", imagepath=" + imagepath + ", imagecheck=" + imagecheck
                + ", taskid=" + taskid + "]";
    }
}
