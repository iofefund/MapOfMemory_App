package org.mapofmemory.entities;

/**
 * Created by The Tronuo on 05.02.2018.
 */

public class PersonInfo {
    private String image, name;
    private int num;

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
