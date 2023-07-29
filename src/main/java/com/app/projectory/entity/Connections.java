package com.app.projectory.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Connections {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private long connectionId;
	private String connectionDate;
	private String connectionStatus; //pending, accepted, blocked, 
	private Long connectedUser;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	Users userWithConnection;

	
	

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

	public String getConnectionStatus() {
		return connectionStatus;
	}

	public void setConnectionStatus(String connectionStatus) {
		this.connectionStatus = connectionStatus;
	}

	public Long getConnectedUser() {
		return connectedUser;
	}

	public void setConnectedUser(Long connectedUser) {
		this.connectedUser = connectedUser;
	}

	public Users getUserWithConnection() {
		return userWithConnection;
	}

	public void setUserWithConnection(Users userWithConnection) {
		this.userWithConnection = userWithConnection;
	}
	

//	public Users getUser() {
//		return user;
//	}
//
//	public void setUser(Users user) {
//		this.user = user;
//	}	
	
	

}
