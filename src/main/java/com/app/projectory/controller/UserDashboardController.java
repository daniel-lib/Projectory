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
import com.app.projectory.doa.TodoListCollectionRepository;
import com.app.projectory.doa.TodoListRepository;
import com.app.projectory.doa.UsersRepository;
import com.app.projectory.entity.Project;
import com.app.projectory.entity.ProjectTasks;
import com.app.projectory.entity.Todo;
import com.app.projectory.entity.TodoListCollection;
import com.app.projectory.entity.Users;

@Controller
@RequestMapping("/user")
public class UserDashboardController {
	
	@Autowired
	TodoListRepository todoData;
	@Autowired
	ProjectRepository projDao;
	@Autowired
	ProjectTaskRepository projTaskDao;
	@Autowired
	TodoListCollectionRepository collectionDao;
//	UserLoginService userLgServ = new UserLoginService();
	@Autowired
	UsersRepository userDao;
	
	
	
	
	
	@GetMapping("/dashboard")
	public String displayUserDashboard(Model model, Todo todo) {
//		return "/user/user-dashboard?indicator = pass"; 
		List<Todo> fetchedItems = todoData.findAll();
		List<Project> projectsList = projDao.findAll();
		List<Project> userProjects = projDao.findAll();
		List<ProjectTasks> projectTasks = projTaskDao.findAll();
		List<TodoListCollection> collection = collectionDao.findAll();
		
		/* Users LoggedInUser = userLgServ.getByUsername(user.getUsername()); */
		
		model.addAttribute("itemCount", todoData.count());
		/* model.addAttribute("items", fetchedItems); */
		model.addAttribute("user", model.getAttribute("userA"));
		model.addAttribute("projectCount", projDao.count());
		model.addAttribute("projectTaskCount", projTaskDao.count());
		model.addAttribute("project", projectsList);
		model.addAttribute("projectTasks", projectTasks);
		model.addAttribute("todoModel", todo);
		model.addAttribute("TodoCollections", collection);
		
		if(model.containsAttribute("loginIndicator")) {
			model.addAttribute("loginIndicator", model.getAttribute("loginIndicator"));
		}
		
		/* model.addAttribute("userL", LoggedInUser); */
		 return "/user/user-dashboard"; 
		
	}
	
	@GetMapping("/check-login-info")
	@ResponseBody
	public Users checkLoginInfo(@RequestParam Long userId, @RequestParam String proof) {
		
		Optional<Users> fetchedUser = userDao.findById(userId);
		if(fetchedUser.isPresent() && fetchedUser.get().getLoginIndicator()!=null) {
			if(fetchedUser.get().getLoginIndicator().equals(proof)) {
//				return fetchedUser.get();
				Users checkedUser = fetchedUser.get();
				return checkedUser;
			}
				
		}
//		fetchUser.ifPresent(value -> checkedUser = value);
		
//		return null;
		return null;
	}
	
	

}
