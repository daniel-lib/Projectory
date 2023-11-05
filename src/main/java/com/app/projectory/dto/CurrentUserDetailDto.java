package com.app.projectory.dto;

public interface CurrentUserDetailDto {
	
	long getUserId();
	String getFirstName();
	String getLastName();
	String getEmailAddress();
	String getUsername();
	String getOnlineStatus();
	String getRole();
	boolean getEnabled();
	String getProfilePicture();
}
