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
    name = "guest_todo_collection",
    indexes = {@Index(name = "idx_guest_todo_collection_workspace", columnList = "workspace_id")})
public class GuestTodoCollection {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "collection_id")
  private long collectionId;

  @Column(name = "workspace_id", nullable = false, length = 36)
  private String workspaceId;

  @Column(name = "created_by", nullable = false, length = 36)
  private String createdBy;

  @Column(nullable = false, length = 40)
  private String title;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  public long getCollectionId() {
    return collectionId;
  }

  public void setCollectionId(long collectionId) {
    this.collectionId = collectionId;
  }

  public String getWorkspaceId() {
    return workspaceId;
  }

  public void setWorkspaceId(String workspaceId) {
    this.workspaceId = workspaceId;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
