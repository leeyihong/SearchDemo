package com.leeyihong.search;

public class Itinerary {

	//Variables
    int _id, rating;
	String poi, category, sub_category, image;
    
    //Constructors
    public Itinerary(){}
    
    public Itinerary(int id, String poi, int rating, String category, String sub_category, String image){
    	this._id = id;
    	this.poi = poi;
    	this.rating = rating;
    	this.category = category;
    	this.sub_category = sub_category;
    	this.image = image;
    }
    
    public Itinerary(String poi, int rating, String category, String sub_category, String image){
        this.poi = poi;
        this.rating = rating;
        this.category = category;
        this.sub_category = sub_category;
        this.image = image;
    }

    public int getId() {
		return this._id;
	}

	public void setId(int id) {
		this._id = id;
	}

	public int getRating() {
		return this.rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getPoi() {
		return this.poi;
	}

	public void setPoi(String poi) {
		this.poi = poi;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return this.sub_category;
	}

	public void setSubCategory(String sub_category) {
		this.sub_category = sub_category;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

     
}
