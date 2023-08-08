package com.app.projectory.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.app.projectory.dto.ProjectStatusCount;
import com.app.projectory.entity.Project;

public interface ProjectRepository extends CrudRepository<Project, Long>{
	/* List<Project> */
	@Override
	List<Project> findAll();
	
	@Query(nativeQuery=true, value="SELECT STATUS as statusLabel, COUNT(STATUS) as labelCount FROM PROJECT "
			+ "GROUP BY STATUS ORDER BY statusLabel")
	List<ProjectStatusCount> countProjectStatus();
	
}
