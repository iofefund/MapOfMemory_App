package org.mapofmemory.entities;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MonumentResponse{

	@SerializedName("persons")
	private List<PersonEntity> persons;

	@SerializedName("monuments")
	private List<MonumentEntity> monuments;

	public void setPersons(List<PersonEntity> persons){
		this.persons = persons;
	}

	public List<PersonEntity> getPersons(){
		return persons;
	}

	public void setMonuments(List<MonumentEntity> monuments){
		this.monuments = monuments;
	}

	public List<MonumentEntity> getMonuments(){
		return monuments;
	}

}