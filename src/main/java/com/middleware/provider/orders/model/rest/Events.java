package com.middleware.provider.orders.model.rest;

public class Events {
	private String city;     	
	private String state;     
	private String description;     
	private String date;     
	

    public Events() {
    }
    
    public Events( String city,String state,String description,String date) {
        this.city = city;
        this.state = state;
        this.description = description;
        this.date = date;
    }

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}


    
}
