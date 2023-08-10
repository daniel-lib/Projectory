package com.app.projectory.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long projectId;
	/* private Date startDate; */
	private String title;
	private String description;
	private String status;
	
	/* private Date finishDate; */
	
	//for project tasks
	@OneToMany(mappedBy = "containerProject")
	private List<ProjectTasks> projectTasks;
	
	//for indicating owner(creator) of the project
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "project_owner_user_id")
	private Users projectOwner;
	
	//for listing members of the project(people who joined the project)
	@ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinTable(name = "project_members", joinColumns = @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<Users> projectMembers;



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

	public List<ProjectTasks> getProjectTasks() {
		return projectTasks;
	}

	public void setProjectTasks(List<ProjectTasks> projectTasks) {
		this.projectTasks = projectTasks;
	}

	public Users getProjectOwner() {
		return projectOwner;
	}

	public void setProjectOwner(Users projectOwner) {
		this.projectOwner = projectOwner;
	}

	public List<Users> getProjectMembers() {
		return projectMembers;
	}

	public void setProjectMembers(List<Users> projectMembers) {
		this.projectMembers = projectMembers;
	}
	
	
	

}
