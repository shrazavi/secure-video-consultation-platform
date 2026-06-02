package com.shrazavi.dadmehr.DataClass;



public class ChatsText {
    private String id;
    private String text;
    private String from;
    private String time;
    public static final int MESSAGE_RIGHT = 0;
    public static final int MESSAGE_LEFT = 1;
    private long date;
    private boolean deleted;
    private boolean read;
    private String imgurl;
    private String vidurl;
    private String voise;
    private String audio;
    private String pdf;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getVidurl() {
        return vidurl;
    }

    public void setVidurl(String vidurl) {
        this.vidurl = vidurl;
    }

    public String getVoise() {
        return voise;
    }

    public void setVoise(String voise) {
        this.voise = voise;
    }

    public static int getMessageRight() {
        return MESSAGE_RIGHT;
    }

    public static int getMessageLeft() {
        return MESSAGE_LEFT;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }
}
