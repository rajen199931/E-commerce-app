package com.cgi.userprofile.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity 
// defines a class can be mapped to a table and identifies a class as an entity.
public class Register {
	
	@Id
	@GeneratedValue(strategy =GenerationType.AUTO)
	private String userId;
	private String userName;
	private Long contactNubmber;
	private String email;
	public Register() {
		super();
		
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getContactNubmber() {
		return contactNubmber;
	}
	public void setContactNubmber(Long contactNubmber) {
		this.contactNubmber = contactNubmber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	

}
