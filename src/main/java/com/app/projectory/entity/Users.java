package com.app.projectory.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long userId;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String username;
	private String password;
	
//	@OneToMany(mappedBy = "user")
//	List<Connections> connection;
	
	public Users() {
		
	}
	public Users(String username, String password, String firstName, String lastName, String emailAddress) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}	
	
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
//	public List<Connections> getConnection() {
//		return connection;
//	}
//	public void setConnection(List<Connections> connection) {
//		this.connection = connection;
//	}
	@Override
	public String toString() {
		return getFirstName() +" "+getLastName();
	}
	
		
}
