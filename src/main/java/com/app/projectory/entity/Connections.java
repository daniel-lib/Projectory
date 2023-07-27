package com.app.projectory.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Connections {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private long connectionId;
	private String connectionDate;
	
//	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JoinColumn(name = "user_id")
//	Users user;

	public Connections() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Connections(String connectionDate, Users user) {
		super();
		this.connectionDate = connectionDate;
//		this.user = user;
	}

	public long getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(long connectionId) {
		this.connectionId = connectionId;
	}

	public String getConnectionDate() {
		return connectionDate;
	}

	public void setConnectionDate(String connectionDate) {
		this.connectionDate = connectionDate;
	}

//	public Users getUser() {
//		return user;
//	}
//
//	public void setUser(Users user) {
//		this.user = user;
//	}	
	
	

}
