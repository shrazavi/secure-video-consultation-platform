package com.shrazavi.dadmehr.DataClass;


public class Ticket {
    String _id;
    String user;
    String support;
    String title;
    String type;
    int readS;
    int readU;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getReadS() {
        return readS;
    }

    public void setReadS(int readS) {
        this.readS = readS;
    }

    public int getReadU() {
        return readU;
    }

    public void setReadU(int readU) {
        this.readU = readU;
    }
}
