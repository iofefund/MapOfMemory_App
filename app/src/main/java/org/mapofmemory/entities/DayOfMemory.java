package org.mapofmemory.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteCreator;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by The Tronuo on 04.02.2018.
 */
@StorIOSQLiteType(table = "day_of_memories")
public class DayOfMemory {
    @StorIOSQLiteColumn(name = "id", key = true)
    @SerializedName("id")
    @Expose
    private String id;
    @StorIOSQLiteColumn(name = "place_id")
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @StorIOSQLiteColumn(name = "title")
    @SerializedName("title")
    @Expose
    private String title;
    @StorIOSQLiteColumn(name = "text")
    @SerializedName("text")
    @Expose
    private String text;
    @StorIOSQLiteColumn(name = "image")
    @SerializedName("image")
    @Expose
    private String image;
    @StorIOSQLiteColumn(name = "type")
    @SerializedName("type")
    @Expose
    private String type;
    @StorIOSQLiteColumn(name = "date")
    @SerializedName("date")
    @Expose
    private String date;

    @StorIOSQLiteCreator
    public DayOfMemory(String id, String placeId, String title, String text, String image, String type, String date){
        this.id = id;
        this.placeId = placeId;
        this.title = title;
        this.text = text;
        this.image = image;
        this.type = type;
        this.date = date;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
