package com.example.myapplication.ui.notifications;

public class NotiItem {

    private int imageId;
    private String title;
    private String duration;
    private String dday;

    public NotiItem(int imageId, String title, String duration, String dday){
        this.imageId = imageId;
        this.title = title;
        this.duration = duration;
        this.dday = dday;
    }

    public int getImageId() { return imageId; }

    public void setImageId(int imageId) { this.imageId = imageId; }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDuration(){
        return duration;
    }

    public void setDuration(String duration){
        this.duration = duration;
    }

    public String getDday(){
        return dday;
    }

    public void setDday(String dday){
        this.dday = dday;
    }
}

