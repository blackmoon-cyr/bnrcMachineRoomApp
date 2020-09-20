package com.example.photorecognition.uploadphoto.ui.dashboard;

public class message {
    private String title;

    private String time;

    public message(String title,String time){
        this.title=title;
        this.time=time;
    }

    public String getTitle(){
        return title;
    }

    public String getTime(){
        return time;
    }

}
