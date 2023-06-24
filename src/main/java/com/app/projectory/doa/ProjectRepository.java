package com.app.projectory.doa;

import org.springframework.data.repository.CrudRepository;

import com.app.projectory.entity.Project;

public interface ProjectRepository extends CrudRepository<Project, Long>{
	/* List<Project> */
}
