package com.app.projectory.entity;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(
    name = "guest_workspace",
    indexes = {@Index(name = "idx_guest_workspace_expiry", columnList = "expires_at")})
public class GuestWorkspace {
  @Id
  @Column(name = "workspace_id", length = 36)
  private String workspaceId;

  @Column(name = "invite_code_hash", nullable = false, unique = true, length = 64)
  private String inviteCodeHash;

  @Column(name = "invite_redeemed", nullable = false)
  private boolean inviteRedeemed;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "expires_at", nullable = false)
  private Instant expiresAt;

  public String getWorkspaceId() {
    return workspaceId;
  }

  public void setWorkspaceId(String workspaceId) {
    this.workspaceId = workspaceId;
  }

  public String getInviteCodeHash() {
    return inviteCodeHash;
  }

  public void setInviteCodeHash(String inviteCodeHash) {
    this.inviteCodeHash = inviteCodeHash;
  }

  public boolean isInviteRedeemed() {
    return inviteRedeemed;
  }

  public void setInviteRedeemed(boolean inviteRedeemed) {
    this.inviteRedeemed = inviteRedeemed;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(Instant expiresAt) {
    this.expiresAt = expiresAt;
  }
}
