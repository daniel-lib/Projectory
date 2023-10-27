package com.app.projectory.dto;

import javax.persistence.Transient;

public interface ProjectDto {
	
	public Long getProjectId();
	
	public String getTitle();

	public String getDescription();

	public String getStatus();

	public String getCreationDate();
	
	public String getProjectOwnerUsername();
	
	
}
