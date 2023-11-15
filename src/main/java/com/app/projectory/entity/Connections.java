package com.app.projectory.entity;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Connections {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private long connectionId;
	@UpdateTimestamp
	private LocalDate connectionDate;
	private String connectionStatus; //pending, accepted, blocked, 
	private Long requestReceiverUser;
	
	@JsonIgnore	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "request_sender_user")
	Users userWithConnection;

	
	

	public Connections() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Connections(LocalDate connectionDate, Users user) {
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

	public LocalDate getConnectionDate() {
		return connectionDate;
	}

	public void setConnectionDate(LocalDate connectionDate) {
		this.connectionDate = connectionDate;
	}

	public String getConnectionStatus() {
		return connectionStatus;
	}

	public void setConnectionStatus(String connectionStatus) {
		this.connectionStatus = connectionStatus;
	}

	public Long getConnectedUser() {
		return requestReceiverUser;
	}

	public void setConnectedUser(Long requestReceiverUser) {
		this.requestReceiverUser = requestReceiverUser;
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
