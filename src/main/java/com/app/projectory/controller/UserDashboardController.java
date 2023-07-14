package com.app.projectory.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.projectory.doa.ProjectRepository;
import com.app.projectory.doa.ProjectTaskRepository;
import com.app.projectory.doa.TodoListRepository;
import com.app.projectory.entity.Project;
import com.app.projectory.entity.ProjectTasks;
import com.app.projectory.entity.Todo;
import com.app.projectory.service.UserLoginService;

@Controller
@RequestMapping("/user")
public class UserDashboardController {
	
	@Autowired
	TodoListRepository todoData;
	@Autowired
	ProjectRepository projDoa;
	@Autowired
	ProjectTaskRepository projTaskDoa;
	UserLoginService userLgServ = new UserLoginService();
	
	
	
	@GetMapping("/dashboard")
	public String displayUserDashboard(Model model, Todo todo) {
//		return "/user/user-dashboard?indicator = pass"; 
		List<Todo> fetchedItems = todoData.findAll();
		List<Project> projectsList = projDoa.findAll();
		List<ProjectTasks> projectTasks = projTaskDoa.findAll();
		
		/* Users LoggedInUser = userLgServ.getByUsername(user.getUsername()); */
		
		model.addAttribute("itemCount", todoData.count());	
		model.addAttribute("items", fetchedItems);
		model.addAttribute("user", model.getAttribute("userA"));
		model.addAttribute("projectCount", projDoa.count());
		model.addAttribute("project", projectsList);
		model.addAttribute("todoModel", todo);
		model.addAttribute("projectTasks", projectTasks);
		
		/* model.addAttribute("userL", LoggedInUser); */
		 return "/user/user-dashboard"; 
		
	}
	
	
	@GetMapping("add-project-js")
	@ResponseBody	
	public String addTodoUsingJs(@RequestParam String title, @RequestParam String description, @RequestParam String status) {
		try {
			Project project = new Project();
			project.setTitle(title);
			project.setDescription(description);
			project.setStatus(status);
			projDoa.save(project);			
		}
		catch(Exception error) {
			return "project creation - error";
		}
		return "project creation - success";
		
	}
	
	
	@GetMapping("/add-project-task")
	@ResponseBody
	public int addProjectTask(@RequestParam String title, @RequestParam String description, @RequestParam long projId) {
					
		if(title != "" && description != "" && projId > 0 && projId > 0) {
			ProjectTasks task = new ProjectTasks();
			Optional<Project> project = projDoa.findById(projId);
			project.ifPresent(value -> task.setContainerProject(value));
			/* project.orElse(defaultApplicationType); */
			task.setTaskName(title);
			task.setTaskDescription(description);
			projTaskDoa.save(task);
			return 1;			
		}
		return 0;		
	}

}
