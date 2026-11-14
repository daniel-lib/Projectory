package com.app.projectory.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long notificationId;

	@Column(nullable = false)
	private String type;

	@Column(nullable = false)
	private String message;

	private String redirectLink;

	@Column(nullable = false)
	private boolean seen;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	private LocalDateTime modifiedAt;

	private LocalDateTime seenAt;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "receiver_user_id", nullable = false)
	private Users receiver;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_user_id")
	private Users sender;

	public long getNotificationId() {
		return notificationId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRedirectLink() {
		return redirectLink;
	}

	public void setRedirectLink(String redirectLink) {
		this.redirectLink = redirectLink;
	}

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getModifiedAt() {
		return modifiedAt;
	}

	public LocalDateTime getSeenAt() {
		return seenAt;
	}

	public void setSeenAt(LocalDateTime seenAt) {
		this.seenAt = seenAt;
	}

	public Users getReceiver() {
		return receiver;
	}

	public void setReceiver(Users receiver) {
		this.receiver = receiver;
	}

	public Users getSender() {
		return sender;
	}

	public void setSender(Users sender) {
		this.sender = sender;
	}
}
