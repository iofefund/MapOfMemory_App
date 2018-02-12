package org.mapofmemory.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The Tronuo on 05.02.2018.
 */

public class PersonInfo{
    private String image, name;
    private int num;
    private String type;
    private List<PersonInfo> inners = new ArrayList<>();

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

    public void addInner(PersonInfo personInfo){
        inners.add(personInfo);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setInners(List<PersonInfo> inners) {
        this.inners = inners;
    }

    public List<PersonInfo> getInners() {
        return inners;
    }
}
