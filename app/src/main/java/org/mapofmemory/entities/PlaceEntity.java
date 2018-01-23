package org.mapofmemory.entities;

import com.google.gson.annotations.SerializedName;

public class PlaceEntity{

	@SerializedName("img")
	private String img;

	@SerializedName("img_root")
	private String imgRoot;

	@SerializedName("about")
	private String about;

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private String title;

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