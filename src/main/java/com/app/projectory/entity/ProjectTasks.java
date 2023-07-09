package com.app.projectory.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ProjectTasks {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long taskId;
	private String taskName;
	private String taskDescription;
	private String status;
	private String deadline;
	
	
	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project containerProject;


		
	public ProjectTasks() {
		super();
	}

	public ProjectTasks(String taskName, String taskDescription) {
		super();
		this.taskName = taskName;
		this.taskDescription = taskDescription;
	}

	
	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
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
