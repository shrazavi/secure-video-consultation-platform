package com.shrazavi.dadmehr.DataClass;

public class Experience {
    String name;
    int drawble;

    public Experience(String name, int drawble) {
        this.drawble = drawble;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawble() {
        return drawble;
    }

    public void setDrawble(int drawble) {
        this.drawble = drawble;
    }
}
