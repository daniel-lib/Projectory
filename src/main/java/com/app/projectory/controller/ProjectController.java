package com.app.projectory.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.projectory.doa.ProjectRepository;
import com.app.projectory.doa.ProjectTaskRepository;
import com.app.projectory.entity.Project;
import com.app.projectory.entity.ProjectTasks;

@Controller
@RequestMapping("project")
public class ProjectController {
	
	@Autowired
	ProjectRepository projDoa;
	@Autowired
	ProjectTaskRepository projTaskDoa;
	
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
	public int addProjectTask(@RequestParam String title, @RequestParam String description, @RequestParam long projId, @RequestParam String status) {
					
		if(title != "" && description != "" && projId > 0 && projId > 0) {
			ProjectTasks task = new ProjectTasks();
			Optional<Project> project = projDoa.findById(projId);
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

}
