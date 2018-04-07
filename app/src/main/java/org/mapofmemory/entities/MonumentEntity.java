package org.mapofmemory.entities;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteCreator;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by The Tronuo on 25.01.2018.
 */
@StorIOSQLiteType(table = "monuments")
public class MonumentEntity {
    @StorIOSQLiteColumn(name = "num", key = true)
    @SerializedName("num")
    @Expose
    private int num;
    @StorIOSQLiteColumn(name = "id")
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("id_relation")
    @Expose
    @StorIOSQLiteColumn(name = "id_relation")
    private String idRelation;
    @SerializedName("place_id")
    @Expose
    @StorIOSQLiteColumn(name = "place_id")
    private int placeId;
    @SerializedName("name")
    @Expose
    @StorIOSQLiteColumn(name = "name")
    private String name;
    @SerializedName("desc")
    @Expose
    @StorIOSQLiteColumn(name = "descr")
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
    private List<MonumentImgEntity> imgs = new ArrayList<>();
    @StorIOSQLiteColumn(name = "imgs_json")
    private String imgs_json;
    @StorIOSQLiteColumn(name = "keywords")
    @SerializedName("keywords")
    @Expose
    private String keywords;
    @StorIOSQLiteColumn(name = "biography_ids_json")
    @Expose
    private String biographyIdsJson;
    @SerializedName("biography_ids")
    @Expose
    private List<String> biographyIds = new ArrayList<>();

    @StorIOSQLiteCreator
    MonumentEntity(int num, String id, String biographyIdsJson, String keywords, int placeId, String idRelation, String name, String desc, String type, String lat, String lng, String type2, String imgs_json){
        this.num = num;
        this.id = id;
        this.keywords = keywords;
        this.biographyIdsJson = biographyIdsJson;
        this.placeId = placeId;
        this.idRelation = idRelation;
        this.name = name;
        this.desc = desc;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
        this.type2 = type2;
        this.imgs_json = imgs_json;
        Gson gson = new Gson();
        Type monumentImgType = new TypeToken<List<MonumentImgEntity>>(){}.getType();
        this.imgs = gson.fromJson(imgs_json, monumentImgType);
        Type biographyIdsType = new TypeToken<List<String>>(){}.getType();
        this.biographyIds = gson.fromJson(biographyIdsJson, biographyIdsType);
    }

    public MonumentEntity(Cursor c){
        this.num = c.getInt(c.getColumnIndex(MonumentEntityTable.NUM_COLUMN));
        this.id = c.getString(c.getColumnIndex(MonumentEntityTable.ID_COLUMN));
        this.placeId = c.getInt(c.getColumnIndex(MonumentEntityTable.PLACE_ID_COLUMN));
        this.keywords = c.getString(c.getColumnIndex(MonumentEntityTable.KEYWORDS_COLUMN));
        this.biographyIdsJson = c.getString(c.getColumnIndex(MonumentEntityTable.BIOGRAPHY_IDS_JSON_COLUMN));;
        this.idRelation = c.getString(c.getColumnIndex(MonumentEntityTable.ID_RELATION_COLUMN));;
        this.name = c.getString(c.getColumnIndex(MonumentEntityTable.NAME_COLUMN));
        this.desc = c.getString(c.getColumnIndex(MonumentEntityTable.DESC_COLUMN));
        this.type = c.getString(c.getColumnIndex(MonumentEntityTable.TYPE_COLUMN));
        this.lat = c.getString(c.getColumnIndex(MonumentEntityTable.LAT_COLUMN));
        this.lng = c.getString(c.getColumnIndex(MonumentEntityTable.LNG_COLUMN));
        this.imgs_json = c.getString(c.getColumnIndex(MonumentEntityTable.IMGSJSON_COLUMN));;
        Gson gson = new Gson();
        Type monumentImgType = new TypeToken<List<MonumentImgEntity>>(){}.getType();
        this.imgs = gson.fromJson(imgs_json, monumentImgType);
        Type biographyIdsType = new TypeToken<List<String>>(){}.getType();
        this.biographyIds = gson.fromJson(biographyIdsJson, biographyIdsType);
    }


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
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
        //Gson gson = new GsonBuilder().setPrettyPrinting().create();
        this.imgs = imgs;
        //imgs_json = gson.toJson(imgs);
    }

    public String getImgs_json() {
        return imgs_json;
    }

    public void setImgs_json(String imgs_json) {
        this.imgs_json = imgs_json;
        //Gson gson = new Gson();
        //Type teacherListType = new TypeToken<List<MonumentImgEntity>>(){}.getType();
        //this.imgs = gson.fromJson(imgs_json, teacherListType);
    }

    public List<String> getBiographyIds() {
        return biographyIds;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setBiographyIds(List<String> biographyIds) {
        this.biographyIds = biographyIds;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setBiographyIdsJson(String biographyIdsJson) {
        this.biographyIdsJson = biographyIdsJson;
    }

    public String getBiographyIdsJson() {
        return biographyIdsJson;
    }
    public String getRealName(){
        return "";
    }
}
