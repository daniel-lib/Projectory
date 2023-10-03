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
	@Autowired
	private userAccountService userServ;
	
	
	public List<Project> getProjectForCurrentUser(Authentication auth) {
		List<Project> fetchedProjects = projDao.findProjectListByUser(userServ.getUserId(auth));
			return fetchedProjects;
	}

}
