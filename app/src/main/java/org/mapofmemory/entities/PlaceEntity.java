package org.mapofmemory.entities;

import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteCreator;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = "places")
public class PlaceEntity{

	@SerializedName("lat")
	@StorIOSQLiteColumn(name = "lat")
	private double lat;

	@SerializedName("lng")
	@StorIOSQLiteColumn(name = "lng")
	private double lng;

	@SerializedName("img")
	@StorIOSQLiteColumn(name = "img")
	private String img;

	@SerializedName("img_root")
	@StorIOSQLiteColumn(name = "img_root")
	private String imgRoot;

	@SerializedName("about")
	@StorIOSQLiteColumn(name = "about")
	private String about;

	@SerializedName("id")
	@StorIOSQLiteColumn(name = "id", key = true)
	private int id;

	@SerializedName("title")
	@StorIOSQLiteColumn(name = "title")
	private String title;

	PlaceEntity(){

	}
	@StorIOSQLiteCreator
	PlaceEntity(int id, double lat, double lng, String title, String img, String imgRoot, String about){
		this.id = id;
		this.lat = lat;
		this.lng = lng;
		this.title = title;
		this.img = img;
		this.imgRoot = imgRoot;
		this.about = about;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLat() {
		return lat;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLng() {
		return lng;
	}

	public void setImg(String img){
		this.img = img;
	}

	public String getImg(){
		return img;
	}

	public void setImgRoot(String imgRoot){
		this.imgRoot = imgRoot;
	}

	public String getImgRoot(){
		return imgRoot;
	}

	public void setAbout(String about){
		this.about = about;
	}

	public String getAbout(){
		return about;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}
}