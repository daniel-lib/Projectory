package com.app.projectory.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long projectId;
	/* private Date startDate; */
	private String title;
	private String description;
	private String status;
	/* private Date finishDate; */

	@OneToMany(mappedBy = "theProject")
	private List<ProjectTasks> projectTasks; 

	public Project(String title, String description, String status) {
		super();
		this.title = title;
		this.description = description;
		this.status = status;
	}

	public Project() {

	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
