package com.app.projectory.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.projectory.dao.ProjectRepository;
import com.app.projectory.dao.ProjectTaskRepository;
import com.app.projectory.dto.ProjectDto;
import com.app.projectory.entity.Project;
import com.app.projectory.entity.ProjectTasks;
import com.app.projectory.service.userAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/project")
public class ProjectController {
	
	@Autowired
	ProjectRepository projDao;
	@Autowired
	ProjectTaskRepository projTaskDoa;
	@Autowired
	private userAccountService userServ;
	
	@GetMapping("/board")
	public String serveBoard(Model model, Authentication auth){
		model.addAttribute("currentUserDetail", userServ.getCurrentUserDetail(auth));
		model.addAttribute("currentPage", "board");
		/* model.addAttribute("user", auth.getName()); */
		return "/user/user-content-container";
	}
	
	@GetMapping("add-project-js")
	@ResponseBody	
	public String addTodoUsingJs(@RequestParam String title, @RequestParam String description, @RequestParam String status) {
		try {
			Project project = new Project();
			project.setTitle(title);
			project.setDescription(description);
			project.setStatus(status);
			projDao.save(project);			
		}
		catch(Exception error) {
			return "project creation - error";
		}
		return "project creation - success";
		
	}
	
	
	@GetMapping("/add-project-task")
	@ResponseBody
	public int addProjectTask(@RequestParam String title, @RequestParam String description, @RequestParam long projId, @RequestParam String status) {
					
		if(title != "" && description != "" && projId > 0 && projId > 0) {
			ProjectTasks task = new ProjectTasks();
			Optional<Project> project = projDao.findById(projId);
			project.ifPresent(value -> task.setContainerProject(value));
			/* project.orElse(defaultApplicationType); */
			task.setTaskName(title);
			task.setTaskDescription(description);
			task.setStatus(status);
			projTaskDoa.save(task);
			return 1;			
		}
		return 0;		
	}
	
	@GetMapping("/getProjects")
	public @ResponseBody List<ProjectDto> serveProjects(Authentication auth, Principal p) {
		//get user id from user account service
		long userId = userServ.getUserId(auth);
		//get projects for specific user
			
		return projDao.findProjectListByUserIncUsername(userId);
		//return null;
	}
	
	@GetMapping("/getProjectCount")
	public @ResponseBody long serveProjectCount(Authentication auth) {
		//get user id from user account service
		long userId = userServ.getUserId(auth);
		//get projects for specific user
			
		return projDao.countProjectForUser(userId);
		//return null;
	}

}
