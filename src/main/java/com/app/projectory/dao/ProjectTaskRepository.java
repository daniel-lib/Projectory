package com.app.projectory.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.projectory.entity.ProjectTasks;

@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTasks, Long>{
	/* public Project findById(long projId); */
	List<ProjectTasks> findAll();
}
