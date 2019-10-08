package com.example.blackeagle.joker;

public class Messages {

    private String message,type,from;
    private long time;

    public Messages(){

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setType(String type) {
        this.type = type;

    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Messages(String message, String type,String from, long time){
        this.message = message;
        this.type = type;
        this.time = time;
        this.from = from;

    }
}
