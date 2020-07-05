package com.example.myapplication.ui.notifications;

import android.graphics.drawable.Drawable;

public class NotiItem {

    private Drawable image;
    private String title;
    private String duration;
    private String dday;

    public NotiItem(Drawable image, String title, String duration, String dday){
        this.image = image;
        this.title = title;
        this.duration = duration;
        this.dday = dday;
    }

    public Drawable getImage(){
        return image;
    }

    public void setImage(Drawable image){
        this.image = image;
    }

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

