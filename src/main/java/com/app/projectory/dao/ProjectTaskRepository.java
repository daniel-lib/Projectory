package com.app.projectory.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.app.projectory.entity.ProjectTasks;

public interface ProjectTaskRepository extends CrudRepository<ProjectTasks, Long>{
	/* public Project findById(long projId); */
	List<ProjectTasks> findAll();
}
