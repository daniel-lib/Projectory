package com.app.projectory.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.projectory.dao.ProjectRepository;
import com.app.projectory.dao.ProjectTaskRepository;
import com.app.projectory.dao.TodoListCollectionRepository;
import com.app.projectory.dao.TodoListRepository;
import com.app.projectory.dao.UsersRepository;
import com.app.projectory.dto.ProjectStatusCount;
import com.app.projectory.entity.Project;
import com.app.projectory.entity.ProjectTasks;
import com.app.projectory.entity.Todo;
import com.app.projectory.entity.TodoListCollection;
import com.app.projectory.entity.Users;
import com.app.projectory.service.ProjectService;
import com.app.projectory.service.TodoListCollectionService;
import com.app.projectory.service.userAccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/user")
public class UserDashboardController {
	@Autowired
	private userAccountService userServ;
	
	@Autowired
	private TodoListCollectionService todoCollServ;
	
	@Autowired
	TodoListRepository todoData;
	@Autowired
	ProjectRepository projDao;
	@Autowired
	ProjectTaskRepository projTaskDao;
	@Autowired
	TodoListCollectionRepository collectionDao;
//	UserAuthService userLgServ = new UserAuthService();
	@Autowired
	UsersRepository userDao;
	@Autowired
	BCryptPasswordEncoder encoder;	
	@Autowired
	ProjectService projServ;
	@Autowired
	TodoListCollectionService collectionServ;
	
	
	
	//For testing
	@GetMapping("/username")
    @ResponseBody
    public String currentUserName(Authentication auth) {	
		//return userServ.getCurrentUsername(auth);
		//return auth.getName();
		
		/*
		 * String projects = ""; // projDao.findProjectListByUser(1).toString();
		 * for(Project p : projServ.getProjectForCurrentUser(auth) ) { projects +=
		 * p.getTitle()+" <br/> ";
		 * 
		 * }
		 * 
		 * return projects;
		 */
		
		return null;
		 
    }
	
	@GetMapping("/dashboard")
	public String displayUserDashboard(Model model, Todo todo, Principal principal, Authentication auth) throws JsonProcessingException {
		long userId = userServ.getUserId(auth);
		List<Project> projectsList = projServ.getProjectForCurrentUser(userId);		
		//List<Project> userProjects = projDao.findAll();
		List<ProjectTasks> projectTasks = projTaskDao.findAll();
		
		List<TodoListCollection> collection = collectionServ.getTodoCollectionForCurrentUser(userId);
		
		List<ProjectStatusCount> StatusCountList = projDao.countProjectStatus(userId);
		Map<String, ProjectStatusCount> StatusCntMap = new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();
		for(ProjectStatusCount obj : StatusCountList)
		StatusCntMap.put(obj.getStatusLabel() , obj);
		
		String jsonString = objectMapper.writeValueAsString(StatusCountList);
		model.addAttribute("ProjectStatusCount",jsonString);
		
		model.addAttribute("itemCount", todoData.count());
		model.addAttribute("user", principal.getName());
		//model.addAttribute("projectCount", projDao.count());   //only works on the repo?
		
		model.addAttribute("projectCount", projDao.count());
		model.addAttribute("projectMembersCount", projDao.count());
		model.addAttribute("projectTaskCount", projTaskDao.count());
		model.addAttribute("project", projectsList);
		model.addAttribute("projectTasks", projectTasks);
		model.addAttribute("todoModel", todo);
		model.addAttribute("TodoCollections", collection);
		
		//activity
		
	
		
		if(model.containsAttribute("loginIndicator")) {
			model.addAttribute("loginIndicator", model.getAttribute("loginIndicator"));
		}
		
		model.addAttribute("currentUserDetail", userServ.getCurrentUserDetail(auth));
		model.addAttribute("currentPage", "dashboard");
		 return "/user/user-content-container"; 
		
	}
	
	@GetMapping("/api/todo-list/{collectionId}")
	@ResponseBody
	public List<Todo> fetchTodoList(Authentication auth, @PathVariable long collectionId ){
		List<Todo> fetchedTodoList = todoCollServ.getCurrentUserTodoListByCollection(collectionId);
		return fetchedTodoList;
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
	
	
	@GetMapping("profile")
	public String displayUserProfile(Model model, Authentication auth) {
		model.addAttribute("currentUserDetail", userServ.getCurrentUserDetail(auth));
		model.addAttribute("currentPage", "profile");
		model.addAttribute("usernameOnPath", false);
		return "/user/user-content-container";
	}
	
	@GetMapping("{username}")
	public String displayUserProfileWithUsername(Model model, Authentication auth, @PathVariable("username") String username) {
		Users UserByUsernameResult = userServ.getUserDetailByUsername(username);
		
		  if(UserByUsernameResult==null) 
			  model.addAttribute("UserDetailByUsername", "User does not exist");	
		  else		  
			  model.addAttribute("UserDetailByUsername", UserByUsernameResult);			 
		
		
		
		model.addAttribute("currentUserDetail", userServ.getCurrentUserDetail(auth));
		model.addAttribute("currentPage", "profile");
		model.addAttribute("usernameOnPath", true);
		return "/user/user-content-container";
	}
	
	
	

}
