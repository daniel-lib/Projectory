package com.app.projectory.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class ProjectTasks {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String taskId;
	private String taskName;
	private String taskDescription;
	private String status;
	private String deadline;
	
	
	
		
	public ProjectTasks() {
		super();
	}

	public ProjectTasks(String taskId, String taskName, String taskDescription) {
		super();
		this.taskId = taskId;
		this.taskName = taskName;
		this.taskDescription = taskDescription;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	
	
	

}
