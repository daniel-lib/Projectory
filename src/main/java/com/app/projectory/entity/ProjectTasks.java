package com.app.projectory.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ProjectTasks {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long taskId;
	private String taskName;
	private String taskDescription;
	private String status;
	private String deadline;
	
	private long assignee;
	
	@JsonIgnore
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, 
			fetch = FetchType.LAZY)
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

	
	

	public Project getContainerProject() {
		return containerProject;
	}

	public void setContainerProject(Project containerProject) {
		this.containerProject = containerProject;
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
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	
	
}
