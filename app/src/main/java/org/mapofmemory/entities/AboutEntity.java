package org.mapofmemory.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteCreator;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by The Tronuo on 04.02.2018.
 */
@StorIOSQLiteType(table = "about")
public class AboutEntity {
    @StorIOSQLiteColumn(name = "id", key = true)
    @SerializedName("id")
    @Expose
    private String id;
    @StorIOSQLiteColumn(name = "place_id")
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @StorIOSQLiteColumn(name = "descr_short")
    @SerializedName("descr_short")
    @Expose
    private String descrShort;
    @StorIOSQLiteColumn(name = "descr_detailed")
    @SerializedName("descr_detailed")
    @Expose
    private String descrDetailed;
    @StorIOSQLiteColumn(name = "image")
    @SerializedName("image")
    @Expose
    private String image;

    @StorIOSQLiteCreator
    public AboutEntity(String id, String placeId, String descrShort, String descrDetailed, String image){
        this.id = id;
        this.placeId = placeId;
        this.descrShort = descrShort;
        this.descrDetailed = descrDetailed;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getDescrShort() {
        return descrShort;
    }

    public void setDescrShort(String descrShort) {
        this.descrShort = descrShort;
    }

    public String getDescrDetailed() {
        return descrDetailed;
    }

    public void setDescrDetailed(String descrDetailed) {
        this.descrDetailed = descrDetailed;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
