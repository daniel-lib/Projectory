package com.app.projectory.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
//	UserAuthService userLgServ = new UserAuthService();
	@Autowired
	UsersRepository userDao;
	
	
	
	@RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Principal principal, Authentication auth) {
		
        return auth.getDetails().toString();
    }
	
	@GetMapping("/dashboard")
	public String displayUserDashboard(Model model, Todo todo, Principal principal) throws JsonProcessingException {
//		return "/user/user-dashboard?indicator = pass"; 
		List<Todo> fetchedItems = todoData.findAll();
		List<Project> projectsList = projDao.findAll();
		List<Project> userProjects = projDao.findAll();
		List<ProjectTasks> projectTasks = projTaskDao.findAll();
		List<TodoListCollection> collection = collectionDao.findAll();
		
		List<ProjectStatusCount> StatusCountList = projDao.countProjectStatus();
		Map<String, ProjectStatusCount> StatusCntMap = new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();
		for(ProjectStatusCount obj : StatusCountList)
		StatusCntMap.put(obj.getStatusLabel() , obj);
		
		String jsonString = objectMapper.writeValueAsString(StatusCountList);
		model.addAttribute("ProjectStatusCount",jsonString);
		
		model.addAttribute("itemCount", todoData.count());
		/* model.addAttribute("items", fetchedItems); */
		model.addAttribute("user", principal.getName());
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
