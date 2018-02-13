package org.mapofmemory.entities;

import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteCreator;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType;

import java.util.ArrayList;
import java.util.List;

@StorIOSQLiteType(table = "persons")
public class PersonEntity{
	@StorIOSQLiteColumn(name = "img")
	@SerializedName("img")
	private String img;

	@StorIOSQLiteColumn(name = "num", key = true)
	@SerializedName("num")
	private int num;

	@StorIOSQLiteColumn(name = "name")
	@SerializedName("name")
	private String name = "";

	@StorIOSQLiteColumn(name = "id")
	@SerializedName("id")
	private String id;

	@StorIOSQLiteColumn(name = "place_id")
	@SerializedName("place_id")
	private String placeId;

	private List<MonumentEntity> monuments = new ArrayList<>();
	@StorIOSQLiteCreator
	PersonEntity(String id, String placeId, String name, int num, String img){
		this.id = id;
		this.placeId = placeId;
		this.name = name;
		this.num = num;
		this.img = img;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setImg(String img){
		this.img = img;
	}

	public String getImg(){
		return img;
	}

	public void setNum(int num){
		this.num = num;
	}

	public int getNum(){
		return num;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setMonuments(List<MonumentEntity> monuments) {
		this.monuments = monuments;
	}

	public List<MonumentEntity> getMonuments() {
		return monuments;
	}

	@Override
 	public String toString(){
		return 
			"PersonsItem{" + 
			"img = '" + img + '\'' + 
			",num = '" + num + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}