package com.app.projectory.controller;

import java.security.Principal;
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
import com.app.projectory.dao.UsersRepository;
import com.app.projectory.dto.ProjectDto;
import com.app.projectory.dto.ProjectMembersDto;
import com.app.projectory.dto.ProjectTasksDto;
import com.app.projectory.entity.Project;
import com.app.projectory.entity.ProjectTasks;
import com.app.projectory.entity.Users;
import com.app.projectory.service.userAccountService;

@Controller
@RequestMapping("/project")
public class ProjectController {
	
	@Autowired
	private ProjectRepository projDao;
	@Autowired
	private ProjectTaskRepository projTaskDoa;
	@Autowired
	private userAccountService userServ;
	@Autowired private UsersRepository userRepo;
	
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
	
	

	@GetMapping("/getProjectTasks")
	public @ResponseBody List<ProjectTasksDto> serveProjectTaskById(Authentication auth, @RequestParam("project") long projectId) {
		//get user id from user account service
		long userId = userServ.getUserId(auth);
		//get project tasks for specific user			
		return projDao.findProjectTasksByProject(projectId, userId);
		//return null;
	}
	
	@GetMapping("/members")
	public @ResponseBody List<ProjectMembersDto> serveProjectMembersById(Authentication auth, @RequestParam("project") long projectId) {
		//get user id from user account service
		long userId = userServ.getUserId(auth);
		//get project member for specific project			
		return projDao.findProjectMembersByProjectId(projectId, userId);
		//return null;
	}
	
	//Project count
	@GetMapping("/getProjectCount")
	public @ResponseBody long serveProjectCount(Authentication auth) {
		//get user id from user account service
		long userId = userServ.getUserId(auth);
		//get projects for specific user			
		return projDao.countProjectForUser(userId);
	}
	
	@GetMapping("/getOwnProjectsCount")
	public @ResponseBody long serveOwnProjectCount(Authentication auth) {
		//get user id from user account service
		long userId = userServ.getUserId(auth);
		//get projects for specific user			
		return projDao.countProjectsCreatedByUser(userId);
	}
	
	@GetMapping("/getJoinedProjectsCount")
	public @ResponseBody long serveJoinedProjectCount(Authentication auth) {
		//get user id from user account service
		long userId = userServ.getUserId(auth);
		//get joined projects for specific user			
		return projDao.countJoinedProjects(userId);
	}
	//Project count ended
	
	@GetMapping("/findByUsername")
	public @ResponseBody Users findByUsername(@RequestParam("user") String username) {
		return userRepo.findByUsername(username);
	}
	
	
	//add project member
	//returns 1 if successful or 0 if not
	@GetMapping("/addProjectMember")
	public @ResponseBody int addProjectMember(@RequestParam("project") long projectId, 
			@RequestParam("user") String username) {
		if(userRepo.findByUsername(username) != null) {
		long userId = userRepo.findByUsername(username).getUserId();
		return projDao.addProjectMember(projectId, userId);	
		}
		return 0;
	}
	
	
	//add project member
		//returns 1 if successful or 0 if not
		@GetMapping("/removeProjectMember")
		public @ResponseBody int removeProjectMember(@RequestParam("project") long projectId, 
				@RequestParam("user") String username) {
			if(userRepo.findByUsername(username) != null) {
			long userId = userRepo.findByUsername(username).getUserId();
			return projDao.removeProjectMember(projectId, userId);	
			}
			return 0;
		}
		
		
	
	@GetMapping("/task/update/status")
	public @ResponseBody int updateProjectTaskStatus(@RequestParam("status") String statusUpdate, 
			@RequestParam("task") long taskId, Authentication auth) {
		long userId = userServ.getUserId(auth);
		return projTaskDoa.updateProjectStatus(taskId, statusUpdate, userId);

	}
	
	@GetMapping("/task/update/assignee")
	public @ResponseBody int addProjectTaskAssignee(@RequestParam("user") long assigneeUserId, 
			@RequestParam("task") long taskId, Authentication auth) {
		long authUserId = userServ.getUserId(auth);
		return projTaskDoa.updateProjectTaskAssignee(taskId, assigneeUserId, authUserId);

	}

}
