package com.gogi.eregatta;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Booking {
	@Id
	private int id;
	private String price;
	private String url;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String geturl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
