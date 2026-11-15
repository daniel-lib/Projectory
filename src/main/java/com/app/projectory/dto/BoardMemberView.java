package com.app.projectory.dto;

public class BoardMemberView {
  private final String memberId;
  private final String username;
  private final String displayName;
  private final String profileUrl;

  public BoardMemberView(String memberId, String username, String displayName, String profileUrl) {
    this.memberId = memberId;
    this.username = username;
    this.displayName = displayName;
    this.profileUrl = profileUrl;
  }

  public String getMemberId() {
    return memberId;
  }

  public String getUsername() {
    return username;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getProfileUrl() {
    return profileUrl;
  }
}
