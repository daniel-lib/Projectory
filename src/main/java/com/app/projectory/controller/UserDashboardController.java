package com.app.projectory.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.projectory.dao.ConnectionsRepository;
import com.app.projectory.dao.ProjectRepository;
import com.app.projectory.dao.ProjectTaskRepository;
import com.app.projectory.dao.TodoListCollectionRepository;
import com.app.projectory.dao.TodoListRepository;
import com.app.projectory.dao.UsersRepository;
import com.app.projectory.dto.ConnectionSenderReceiverDto;
import com.app.projectory.dto.CurrentUserDetailDto;
import com.app.projectory.dto.ProjectDto;
import com.app.projectory.dto.ProjectStatusCount;
import com.app.projectory.dto.PublicUserConnectionsDetailDto;
import com.app.projectory.dto.PublicUserPersonalDetailDto;
import com.app.projectory.dto.PublicUserProjectDetailDto;
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
	@Autowired
	ConnectionsRepository connectionsDao;
	
	
	
	
	//get authenticated user details
	@GetMapping("/username")    
    public @ResponseBody Users currentUserName(Authentication auth) {	
		
		return userServ.getCurrentUserDetail(auth);
		 
    }
	
	@GetMapping("/dashboard")
	public String displayUserDashboard(Model model, Todo todo, Principal principal, Authentication auth) throws JsonProcessingException {
		long userId = userServ.getUserId(auth);
		//List<Project> projectsList = projServ.getProjectForCurrentUser(userId);		
		List<ProjectDto> projectsList = projDao.findProjectListByUserIncUsername(userId);		
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
		
			
		
		model.addAttribute("currentUserDetail", userServ.getCurrentUserDetail(auth));
		model.addAttribute("currentPage", "dashboard");
		 return "/user/user-content-container"; 
		
	}
	
	//for test only
	@GetMapping("/api/todo-list/{collectionId}")
	@ResponseBody
	public List<Todo> fetchTodoList(Authentication auth, @PathVariable long collectionId ){
		List<Todo> fetchedTodoList = todoCollServ.getCurrentUserTodoListByCollection(collectionId);
		return fetchedTodoList;
	}
		
	
	@GetMapping("/profile")
	public String displayUserProfile(Model model, Authentication auth) {
		Users currentUser = userServ.getCurrentUserDetail(auth);
		model.addAttribute("currentUserDetail", currentUser);
		model.addAttribute("currentPage", "profile");
		//model.addAttribute("ownAccount", false);
		model.addAttribute("ownAccount", true);
		model.addAttribute("UserDetailByUsername", currentUser.getUsername());
		return "/user/user-content-container";
	}
	
	@GetMapping("{username}")
	public String displayUserProfileWithUsername(Model model, Authentication auth, @PathVariable("username") String username) {
		Users UserByUsernameResult = userServ.getUserDetailByUsername(username);
		
		  if(UserByUsernameResult==null) 
			  model.addAttribute("UserDetailByUsername", "User does not exist");	
		  else {			  
			  String authUsername = userServ.getCurrentUsername(auth);
			  if(authUsername.equals(username)) {
				  return "redirect:profile";  //redirect to current user profile
			  }
			  else {				 
					model.addAttribute("UserDetailByUsername", UserByUsernameResult.getUsername());
			  }
				  
		  }		  		 		
		  
		
		model.addAttribute("currentUserDetail", userServ.getCurrentUserDetail(auth));
		model.addAttribute("currentPage", "profile");
		model.addAttribute("ownAccount", false);
		return "/user/user-content-container";
	}
	
	
	@GetMapping("/getUserConnectionList")
	public @ResponseBody List<PublicUserConnectionsDetailDto> serveConnectionList(Authentication auth){
		String username = userServ.getCurrentUsername(auth);
		//PublicUserPersonalDetailDto publicUser = userDao.getPublicUserDetail(username);
		List<PublicUserConnectionsDetailDto> publicUserConnections = connectionsDao.findApprovedConnections(username);
		//List<PublicUserProjectDetailDto> publicUserProjects = projDao.findPublicUserProjects(username);
		
		return publicUserConnections;
	}
	
	@GetMapping("/detail")
	public @ResponseBody CurrentUserDetailDto getCurrentUserDetail(Authentication auth) {
		CurrentUserDetailDto userDetail = userDao.getCurrentUserDetail(userServ.getUserId(auth));
		return userDetail;
	}
	@GetMapping("/detail/{username}")
	public @ResponseBody PublicUserPersonalDetailDto getUserDetailByUsername(@PathVariable("username") String username, Authentication auth) {
		/*if(username.equals(userServ.getCurrentUsername(auth)))
			return "redirect:/detail";
		else {*/
		PublicUserPersonalDetailDto publicUser = userDao.getPublicUserDetail(username);
		return publicUser;		
	}
	@GetMapping("/projects/{username}")
	public @ResponseBody List<PublicUserProjectDetailDto> getPublicProjectsByUsername(@PathVariable("username") String username, Authentication auth) {
	List<PublicUserProjectDetailDto> publicUserProjects = projDao.findPublicUserProjects(username);
	return publicUserProjects;
	}
	
	@GetMapping("/connections/{username}")
	public @ResponseBody List<PublicUserConnectionsDetailDto> getConnectionByUsername(@PathVariable("username") String username, Authentication auth) {
		
//		List<PublicUserConnectionsDetailDto> UserConnections;
//	if(username.equals(userServ.getCurrentUsername(auth))) {
//		UserConnections = connectionsDao.findAllConnections(username);
//	}
//	else {
//		UserConnections = connectionsDao.findPublicConnections(username);
//	}	
//	return UserConnections;
		
		return connectionsDao.findPublicConnections(username);	
	}
		
	
	
	//find sent connection request
	@GetMapping("/connection/request/sent/")
	public @ResponseBody List<PublicUserConnectionsDetailDto> getSentConnectionRequests(Authentication auth) {
		long senderUserId = userServ.getUserId(auth);
		return connectionsDao.findSentConnectionRequests(senderUserId);
	}
	
	
	//find received connection request
	@GetMapping("/connection/request/received/")
	public @ResponseBody List<PublicUserConnectionsDetailDto> getReceivedConnectionRequests(Authentication auth) {
		long senderUserId = userServ.getUserId(auth);
		return connectionsDao.findReceivedConnectionRequests(senderUserId);
	}
	
	
	//send connection request
	@GetMapping("/connection/request/send/{receiver}")
	public @ResponseBody int sendConnectionRequest(@PathVariable long receiver, Authentication auth) {
		long senderUserId = userServ.getUserId(auth);
		LocalDate dateToday = LocalDate.now();
		return connectionsDao.sendConnectionRequest(receiver, senderUserId, dateToday);
	}
	
	
	//cancel sent connection request
		@GetMapping("/connection/request/remove/{receiver}")
		public @ResponseBody int cancelConnectionRequest(@PathVariable long receiver, Authentication auth) {
			long senderUserId = userServ.getUserId(auth);
			return connectionsDao.cancelConnectionRequest(receiver, senderUserId);
		}
		
		// cancel sent connection request by username
				@GetMapping("/connection/request/remove-username/{receiver}")
				public @ResponseBody int cancelConnectionRequestWithUsername(@PathVariable String receiver, Authentication auth) {
					long senderUserId = userServ.getUserId(auth);
					long receiverUsername = userServ.getUserDetailByUsername(receiver).getUserId();
					return connectionsDao.cancelConnectionRequest(receiverUsername, senderUserId);
				}
				
				//accept received connection request
				@GetMapping("/connection/request/accept/{connectionId}")
				public @ResponseBody int acceptConnectionRequest(@PathVariable long connectionId, Authentication auth) {
					long senderUserId = userServ.getUserId(auth);
					return connectionsDao.acceptConnectionRequest(connectionId);
				}
				
				//reject received connection request
				@GetMapping("/connection/request/reject/{connectionId}")
				public @ResponseBody int rejectConnectionRequest(@PathVariable long connectionId, Authentication auth) {
					long senderUserId = userServ.getUserId(auth);
					return connectionsDao.rejectConnectionRequest(connectionId);
				}
				
	
	//get connection status
	@GetMapping("/connection/status/{receiver}")
		public @ResponseBody String getConnectionStatus(@PathVariable long receiver, Authentication auth) {
		long senderUserId = userServ.getUserId(auth);
	return connectionsDao.getConnectionStatus(receiver, senderUserId);
	}
	
	//get connection status
		@GetMapping("/connection/SenderReceiverId/{receiver}")
			public @ResponseBody ConnectionSenderReceiverDto getConnectionSenderReceiverId(@PathVariable long receiver, Authentication auth) {
			long senderUserId = userServ.getUserId(auth);
		return connectionsDao.getConnectionSenderReceiverId(receiver, senderUserId); //order: receiver, center
		}
	
	//unfriend user -- by connection id
	@GetMapping("/connection/remove-by-conn-id/{connectionId}")
	public @ResponseBody int terminateConnection(@PathVariable long connectionId, Authentication auth) {
		return connectionsDao.terminateConnectionByConnectionId(connectionId);
	}
				

}
