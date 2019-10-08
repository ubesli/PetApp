package com.example.blackeagle.joker;

public class ChatYaptıklarım {

    private String chatisim,chatimg,chatid;

    public ChatYaptıklarım(){

    }

    public ChatYaptıklarım(String chatisim, String chatid,String chatimg) {
        this.chatisim = chatisim;
        this.chatimg = chatimg;
        this.chatid = chatid;

    }

    public String getChatisim() {
        return chatisim;
    }

    public void setChatisim(String chatisim) {
        this.chatisim = chatisim;
    }

    public String getChatimg() {
        return chatimg;
    }

    public void setChatimg(String chatimg) {
        this.chatimg = chatimg;
    }

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }
}
