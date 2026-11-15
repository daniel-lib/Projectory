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
    name = "system_access_tracker",
    indexes = {
      @Index(name = "idx_system_access_tracker_accessed_at", columnList = "accessed_at"),
      @Index(
          name = "idx_system_access_tracker_actor_identifier",
          columnList = "actor_identifier")
    })
public class SystemAccessTracker {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "access_id")
  private long accessId;

  @Column(name = "ip_address", nullable = false, length = 45)
  private String ipAddress;

  @Column(name = "user_agent", length = 1024)
  private String userAgent;

  @Column(name = "browser_type", length = 64)
  private String browserType;

  @Column(name = "operating_system", length = 64)
  private String operatingSystem;

  @Column(name = "guest_access", nullable = false)
  private boolean guestAccess;

  @Column(name = "actor_identifier", length = 100)
  private String actorIdentifier;

  @Column(name = "accessed_at", nullable = false)
  private Instant accessedAt;

  public long getAccessId() {
    return accessId;
  }

  public void setAccessId(long accessId) {
    this.accessId = accessId;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public String getBrowserType() {
    return browserType;
  }

  public void setBrowserType(String browserType) {
    this.browserType = browserType;
  }

  public String getOperatingSystem() {
    return operatingSystem;
  }

  public void setOperatingSystem(String operatingSystem) {
    this.operatingSystem = operatingSystem;
  }

  public boolean isGuestAccess() {
    return guestAccess;
  }

  public void setGuestAccess(boolean guestAccess) {
    this.guestAccess = guestAccess;
  }

  public String getActorIdentifier() {
    return actorIdentifier;
  }

  public void setActorIdentifier(String actorIdentifier) {
    this.actorIdentifier = actorIdentifier;
  }

  public Instant getAccessedAt() {
    return accessedAt;
  }

  public void setAccessedAt(Instant accessedAt) {
    this.accessedAt = accessedAt;
  }
}
