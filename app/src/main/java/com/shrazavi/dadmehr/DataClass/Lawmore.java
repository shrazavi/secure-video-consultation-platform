package com.shrazavi.dadmehr.DataClass;

public class Lawmore {

    private String text;
    private int select;
    private String type;

    public Lawmore(String text, int select, String type) {
        this.text = text;
        this.select = select;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
