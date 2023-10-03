package com.app.projectory.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.app.projectory.dao.ProjectRepository;
import com.app.projectory.entity.Project;

@Service
public class ProjectService {
	
	@Autowired
	ProjectRepository projDao;
	
	
	public List<Project> getProjectForCurrentUser(Long userId) {
		List<Project> fetchedProjects = projDao.findProjectListByUser(userId);
			return fetchedProjects;
	}

}
