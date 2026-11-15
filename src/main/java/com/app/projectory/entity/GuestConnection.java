package com.app.projectory.entity;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(
    name = "guest_connection",
    indexes = {
      @Index(
          name = "uq_guest_connection_direction",
          columnList = "workspace_id, sender_id, receiver_id",
          unique = true)
    })
public class GuestConnection {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "connection_id")
  private long connectionId;

  @Column(name = "workspace_id", nullable = false, length = 36)
  private String workspaceId;

  @Column(name = "sender_id", nullable = false, length = 36)
  private String senderId;

  @Column(name = "receiver_id", nullable = false, length = 36)
  private String receiverId;

  @Column(nullable = false, length = 16)
  private String status;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  public long getConnectionId() {
    return connectionId;
  }

  public void setConnectionId(long connectionId) {
    this.connectionId = connectionId;
  }

  public String getWorkspaceId() {
    return workspaceId;
  }

  public void setWorkspaceId(String workspaceId) {
    this.workspaceId = workspaceId;
  }

  public String getSenderId() {
    return senderId;
  }

  public void setSenderId(String senderId) {
    this.senderId = senderId;
  }

  public String getReceiverId() {
    return receiverId;
  }

  public void setReceiverId(String receiverId) {
    this.receiverId = receiverId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
