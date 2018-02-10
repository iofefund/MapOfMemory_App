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

	@SerializedName("url")
	@StorIOSQLiteColumn(name = "url")
	private String url;

	@SerializedName("img_root")
	@StorIOSQLiteColumn(name = "img_root")
	private String imgRoot;

	@SerializedName("about")
	@StorIOSQLiteColumn(name = "about")
	private String about;

	@SerializedName("about_title")
	@StorIOSQLiteColumn(name = "about_title")
	private String aboutTitle;

	@SerializedName("id")
	@StorIOSQLiteColumn(name = "id", key = true)
	private int id;

	@SerializedName("title")
	@StorIOSQLiteColumn(name = "title")
	private String title;

	@SerializedName("descr")
	@StorIOSQLiteColumn(name = "descr")
	private String descr;

	@SerializedName("dom_short")
	@StorIOSQLiteColumn(name = "dom_short")
	private String domShort;

	@SerializedName("dom_detailed")
	@StorIOSQLiteColumn(name = "dom_detailed")
	private String domDetailed;

	@SerializedName("dom_image")
	@StorIOSQLiteColumn(name = "dom_image")
	private String domImage;

	PlaceEntity(){

	}
	@StorIOSQLiteCreator
	PlaceEntity(int id, double lat, double lng, String title, String descr, String img, String url, String imgRoot, String about, String aboutTitle, String domShort, String domDetailed, String domImage){
		this.id = id;
		this.lat = lat;
		this.lng = lng;
		this.title = title;
		this.descr = descr;
		this.img = img;
		this.url = url;
		this.imgRoot = imgRoot;
		this.about = about;
		this.aboutTitle = aboutTitle;
		this.domDetailed = domDetailed;
		this.domImage = domImage;
		this.domShort = domShort;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
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

	public void setAboutTitle(String aboutTitle) {
		this.aboutTitle = aboutTitle;
	}

	public String getAboutTitle() {
		return aboutTitle;
	}

	public void setDomDetailed(String domDetailed) {
		this.domDetailed = domDetailed;
	}

	public String getDomDetailed() {
		return domDetailed;
	}

	public void setDomImage(String domImage) {
		this.domImage = domImage;
	}

	public String getDomImage() {
		return domImage;
	}

	public void setDomShort(String domShort) {
		this.domShort = domShort;
	}

	public String getDomShort() {
		return domShort;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getDescr() {
		return descr;
	}
}