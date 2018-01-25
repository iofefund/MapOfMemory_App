package org.mapofmemory.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by The Tronuo on 25.01.2018.
 */

public class MonumentImgEntity {

    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("desc")
    @Expose
    private String desc;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
