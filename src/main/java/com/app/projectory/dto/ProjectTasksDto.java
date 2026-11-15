package com.app.projectory.dto;

public interface ProjectTasksDto {

  public Long getTaskId();

  public String getTaskName();

  public String getTaskDescription();

  public String getStatus();

  public String getDeadline();

  public long getProjectId();

  public long getAssigneeId();

  // public String getProjectOwnerUsername();
  public String getAssigneeUsername();

  public String getProjectCreationDate();
}
