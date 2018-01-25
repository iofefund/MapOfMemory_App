package org.mapofmemory.entities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteCreator;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType;

import java.util.List;

/**
 * Created by The Tronuo on 25.01.2018.
 */
@StorIOSQLiteType(table = "monuments")
public class MonumentEntity {

    @Nullable
    @StorIOSQLiteColumn(name = "id", key = true)
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("id_relation")
    @Expose
    @StorIOSQLiteColumn(name = "id_relation")
    private String idRelation;
    @NonNull
    @SerializedName("name")
    @Expose
    @StorIOSQLiteColumn(name = "name")
    private String name;
    @SerializedName("desc")
    @Expose
    @StorIOSQLiteColumn(name = "desc")
    private String desc;
    @SerializedName("type")
    @Expose
    @StorIOSQLiteColumn(name = "type")
    private String type;
    @SerializedName("lat")
    @Expose
    @StorIOSQLiteColumn(name = "lat")
    private String lat;
    @SerializedName("lng")
    @Expose
    @StorIOSQLiteColumn(name = "lng")
    private String lng;
    @SerializedName("type_2")
    @Expose
    @StorIOSQLiteColumn(name = "type_2")
    private String type2;
    @SerializedName("imgs")
    @Expose
    private List<MonumentImgEntity> imgs = null;


    @StorIOSQLiteCreator
    MonumentEntity(String id, String idRelation, String name, String desc, String type, String lat, String lng, String type2){
        this.id = id;
        this.idRelation = idRelation;
        this.name = name;
        this.desc = desc;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
        this.type2 = type2;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdRelation() {
        return idRelation;
    }

    public void setIdRelation(String idRelation) {
        this.idRelation = idRelation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public List<MonumentImgEntity> getImgs() {
        return imgs;
    }

    public void setImgs(List<MonumentImgEntity> imgs) {
        this.imgs = imgs;
    }

}
