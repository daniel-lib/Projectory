package com.app.projectory.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

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
import com.app.projectory.dto.ProjectMembersDto;
import com.app.projectory.entity.ProjectTasks;
import com.app.projectory.entity.Todo;
import com.app.projectory.entity.TodoListCollection;
import com.app.projectory.entity.Users;
import com.app.projectory.service.ProjectService;
import com.app.projectory.service.NotificationService;
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

	@Autowired
	NotificationService notificationService;
	
	
	
	
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

	@GetMapping("/settings")
	public String displayUserSettings(Model model, Principal principal, Authentication auth) {
		model.addAttribute("user", principal.getName());
		model.addAttribute("currentUserDetail", userServ.getCurrentUserDetail(auth));
		model.addAttribute("currentPage", "settings");
		return "/user/user-content-container";
	}

	@GetMapping("/connections")
	public String displayUserConnections(Model model, Principal principal, Authentication auth) {
		addConnectionsPageModel(model, auth);
		model.addAttribute("user", principal.getName());
		model.addAttribute("currentPage", "connections");
		return "/user/user-content-container";
	}

	@GetMapping("/fragment/connections-page")
	public String connectionsPageFragment(Model model, Authentication auth) {
		addConnectionsPageModel(model, auth);
		return "user/connections :: connections-content";
	}

	private void addConnectionsPageModel(Model model, Authentication auth) {
		long userId = userServ.getUserId(auth);
		String username = userServ.getCurrentUsername(auth);
		model.addAttribute("currentUserDetail", userServ.getCurrentUserDetail(auth));
		model.addAttribute("ownAccount", true);
		model.addAttribute("userConnectionsList", connectionsDao.findApprovedConnections(username));
		model.addAttribute("sentConnections", connectionsDao.findSentConnectionRequests(userId));
		model.addAttribute("receivedConnections", connectionsDao.findReceivedConnectionRequests(userId));
		model.addAttribute("suggestedConnections", userDao.findSuggestedConnections(userId));
	}

	@PostMapping("/settings")
	public String updateUserSettings(@RequestParam String firstName, @RequestParam String lastName,
			@RequestParam String username, @RequestParam String currentPassword,
			@RequestParam(defaultValue = "") String newPassword,
			@RequestParam(defaultValue = "") String confirmNewPassword,
			Authentication auth, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Users currentUser = userServ.getCurrentUserDetail(auth);
		if (!encoder.matches(currentPassword, currentUser.getPassword())) {
			redirectAttributes.addFlashAttribute("settingsError", "Current password is incorrect.");
			return "redirect:/user/settings";
		}

		String updatedFirstName = firstName.trim();
		String updatedLastName = lastName.trim();
		String updatedUsername = username.trim();
		if (updatedFirstName.isEmpty() || updatedFirstName.length() > 50
				|| updatedLastName.isEmpty() || updatedLastName.length() > 50) {
			redirectAttributes.addFlashAttribute("settingsError", "First and last names are required and must be 50 characters or fewer.");
			return "redirect:/user/settings";
		}
		if (!updatedUsername.matches("[A-Za-z0-9._-]{3,30}")) {
			redirectAttributes.addFlashAttribute("settingsError",
					"Username must be 3-30 characters and use only letters, numbers, periods, underscores, or hyphens.");
			return "redirect:/user/settings";
		}
		Users usernameOwner = userDao.findByUsername(updatedUsername);
		if (usernameOwner != null && usernameOwner.getUserId() != currentUser.getUserId()) {
			redirectAttributes.addFlashAttribute("settingsError", "That username is already in use.");
			return "redirect:/user/settings";
		}
		if (!newPassword.isEmpty()
				&& (newPassword.length() < 8 || !newPassword.equals(confirmNewPassword))) {
			redirectAttributes.addFlashAttribute("settingsError",
					"New passwords must match and be at least 8 characters long.");
			return "redirect:/user/settings";
		}
		if (newPassword.isEmpty() && !confirmNewPassword.isEmpty()) {
			redirectAttributes.addFlashAttribute("settingsError", "Enter a new password in both password fields.");
			return "redirect:/user/settings";
		}

		boolean usernameChanged = !currentUser.getUsername().equals(updatedUsername);
		boolean passwordChanged = !newPassword.isEmpty();
		currentUser.setFirstName(updatedFirstName);
		currentUser.setLastName(updatedLastName);
		currentUser.setUsername(updatedUsername);
		if (passwordChanged) {
			currentUser.setPassword(encoder.encode(newPassword));
		}
		userDao.save(currentUser);

		if (usernameChanged || passwordChanged) {
			org.springframework.security.core.userdetails.UserDetails updatedPrincipal = User
					.withUsername(updatedUsername)
					.password(currentUser.getPassword())
					.authorities(auth.getAuthorities())
					.build();
			UsernamePasswordAuthenticationToken updatedAuthentication =
					new UsernamePasswordAuthenticationToken(updatedPrincipal, null, updatedPrincipal.getAuthorities());
			updatedAuthentication.setDetails(auth.getDetails());
			SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
			request.getSession(true).setAttribute(
					HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
					SecurityContextHolder.getContext());
		}

		redirectAttributes.addFlashAttribute("settingsMessage", "Your account settings have been updated.");
		return "redirect:/user/settings";
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
		addProfileModel(model, currentUser, true, auth);
		return "/user/user-content-container";
	}
	
	@GetMapping("{username}")
	public String displayUserProfileWithUsername(Model model, Authentication auth, @PathVariable("username") String username) {
		Users viewedUser = userServ.getUserDetailByUsername(username);
		if (viewedUser != null && userServ.getCurrentUsername(auth).equals(username)) {
			return "redirect:/user/profile";
		}
		addProfileModel(model, viewedUser, false, auth);
		return "/user/user-content-container";
	}

	private void addProfileModel(Model model, Users viewedUser, boolean ownAccount, Authentication auth) {
		String username = viewedUser == null ? null : viewedUser.getUsername();
		List<PublicUserProjectDetailDto> projects = username == null
				? Collections.emptyList() : projDao.findPublicUserProjects(username);
		List<PublicUserConnectionsDetailDto> connections = username == null
				? Collections.emptyList() : connectionsDao.findPublicConnections(username);
		model.addAttribute("currentUserDetail", userServ.getCurrentUserDetail(auth));
		model.addAttribute("currentPage", "profile");
		model.addAttribute("ownAccount", ownAccount);
		model.addAttribute("profileUser", viewedUser);
		model.addAttribute("profileUserExists", viewedUser != null);
		model.addAttribute("profileUsername", username == null ? "" : username);
		model.addAttribute("viewedUserId", viewedUser == null ? 0L : viewedUser.getUserId());
		model.addAttribute("UserDetailByUsername", username == null ? "User does not exist" : username);
		model.addAttribute("userProjectList", projects);
		model.addAttribute("userConnectionsList", connections);
		model.addAttribute("sentConnections", ownAccount
				? connectionsDao.findSentConnectionRequests(userServ.getUserId(auth)) : Collections.emptyList());
		model.addAttribute("receivedConnections", ownAccount
				? connectionsDao.findReceivedConnectionRequests(userServ.getUserId(auth)) : Collections.emptyList());
		model.addAttribute("userProjectCount", projects.size());
		model.addAttribute("userConnectsCount", connections.size());
		model.addAttribute("userCreatedProjectCount", projects.stream()
				.filter(project -> username != null && username.equals(project.getProjectOwnerUsername())).count());
		model.addAttribute("recentUserProjects", projects.stream()
				.sorted(Comparator.comparing(PublicUserProjectDetailDto::getCreationDate,
						Comparator.nullsLast(Comparator.reverseOrder())))
				.limit(3).collect(Collectors.toList()));
		model.addAttribute("recentReceivedConnections", ownAccount
				? connectionsDao.findReceivedConnectionRequests(userServ.getUserId(auth)).stream().limit(5)
						.collect(Collectors.toList())
				: Collections.emptyList());
		model.addAttribute("assignedTaskCount", ownAccount
				? projTaskDao.countByAssignee(userServ.getUserId(auth)) : 0L);
		model.addAttribute("completedTaskCount", ownAccount
				? projTaskDao.countByAssigneeAndStatus(userServ.getUserId(auth), "done") : 0L);
	}

	@GetMapping("/fragment/connection-status")
	public String connectionStatusFragment(@RequestParam("user") long viewedUserId,
			Authentication auth, Model model) {
		long currentUserId = userServ.getUserId(auth);
		model.addAttribute("viewedUserId", viewedUserId);
		model.addAttribute("currentUserId", currentUserId);
		model.addAttribute("connectionActionId", "connection-action-buttons-" + viewedUserId);
		model.addAttribute("connectionStatus",
				connectionsDao.getConnectionStatus(viewedUserId, currentUserId));
		model.addAttribute("connectionSenderReceiverId",
				connectionsDao.getConnectionSenderReceiverId(viewedUserId, currentUserId));
		return "fragments/connection-status :: connection-buttons";
	}

	@GetMapping("/fragment/connections")
	public String profileConnectionsFragment(@RequestParam("username") String username,
			Authentication auth, Model model) {
		boolean ownAccount = userServ.getCurrentUsername(auth).equals(username);
		model.addAttribute("ownAccount", ownAccount);
		model.addAttribute("profileUsername", username);
		model.addAttribute("userConnectionsList", connectionsDao.findPublicConnections(username));
		model.addAttribute("sentConnections", ownAccount
				? connectionsDao.findSentConnectionRequests(userServ.getUserId(auth)) : Collections.emptyList());
		model.addAttribute("receivedConnections", ownAccount
				? connectionsDao.findReceivedConnectionRequests(userServ.getUserId(auth)) : Collections.emptyList());
		model.addAttribute("userConnectsCount", connectionsDao.findPublicConnections(username).size());
		model.addAttribute("recentReceivedConnections", ownAccount
				? connectionsDao.findReceivedConnectionRequests(userServ.getUserId(auth)).stream().limit(5)
						.collect(Collectors.toList())
				: Collections.emptyList());
		return "user/profile/profile-layouts :: connections-preview";
	}
	
	
	@GetMapping("/getUserConnectionList")
	public @ResponseBody List<PublicUserConnectionsDetailDto> serveConnectionList(Authentication auth){
		String username = userServ.getCurrentUsername(auth);
		//PublicUserPersonalDetailDto publicUser = userDao.getPublicUserDetail(username);
		List<PublicUserConnectionsDetailDto> publicUserConnections = connectionsDao.findApprovedConnections(username);
		//List<PublicUserProjectDetailDto> publicUserProjects = projDao.findPublicUserProjects(username);
		
		return publicUserConnections;
	}

	@GetMapping("/fragment/connection-list")
	public String projectConnectionList(@RequestParam("project") long projectId,
			Authentication auth, Model model) {
		long userId = userServ.getUserId(auth);
		List<ProjectMembersDto> members = projDao.findProjectMembersByProjectId(projectId, userId);
		Set<String> memberUsernames = members.stream()
				.map(ProjectMembersDto::getUsername)
				.collect(Collectors.toCollection(HashSet::new));
		List<PublicUserConnectionsDetailDto> availableConnections =
				connectionsDao.findApprovedConnections(userServ.getCurrentUsername(auth)).stream()
				.filter(connection -> !memberUsernames.contains(connection.getConnectedUserUsername()))
				.collect(Collectors.toList());
		model.addAttribute("projectId", projectId);
		model.addAttribute("connectionsList", availableConnections);
		return "user/project/project-layouts :: add-member-checkbox-list";
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
	@PostMapping("/connection/request/send/{receiver}")
	public @ResponseBody int sendConnectionRequest(@PathVariable long receiver, Authentication auth,
			HttpServletResponse response) {
		long senderUserId = userServ.getUserId(auth);
		LocalDate dateToday = LocalDate.now();
		int result = connectionsDao.sendConnectionRequest(receiver, senderUserId, dateToday);
		if (result == 1) {
			notificationService.createFriendRequestNotification(receiver, senderUserId);
		}
		setProfileRefreshHeader(response, result, "Connection request sent");
		return result;
	}
	
	
	//cancel sent connection request
		@PostMapping("/connection/request/remove/{receiver}")
		public @ResponseBody int cancelConnectionRequest(@PathVariable long receiver, Authentication auth,
				HttpServletResponse response) {
			long senderUserId = userServ.getUserId(auth);
			int result = connectionsDao.cancelConnectionRequest(receiver, senderUserId);
			setProfileRefreshHeader(response, result, "Connection request cancelled");
			return result;
		}
		
		// cancel sent connection request by username
				@PostMapping("/connection/request/remove-username/{receiver}")
				public @ResponseBody int cancelConnectionRequestWithUsername(@PathVariable String receiver,
						Authentication auth, HttpServletResponse response) {
					long senderUserId = userServ.getUserId(auth);
					long receiverUsername = userServ.getUserDetailByUsername(receiver).getUserId();
					int result = connectionsDao.cancelConnectionRequest(receiverUsername, senderUserId);
					setProfileRefreshHeader(response, result, "Connection request cancelled");
					return result;
				}
				
				//accept received connection request
				@PostMapping("/connection/request/accept/{connectionId}")
				public @ResponseBody int acceptConnectionRequest(@PathVariable long connectionId, Authentication auth,
						HttpServletResponse response) {
					int result = connectionsDao.acceptConnectionRequest(connectionId);
					setProfileRefreshHeader(response, result, "Connection request accepted");
					return result;
				}
				
				//reject received connection request
				@PostMapping("/connection/request/reject/{connectionId}")
				public @ResponseBody int rejectConnectionRequest(@PathVariable long connectionId, Authentication auth,
						HttpServletResponse response) {
					int result = connectionsDao.rejectConnectionRequest(connectionId);
					setProfileRefreshHeader(response, result, "Connection request rejected");
					return result;
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
	@PostMapping("/connection/remove-by-conn-id/{connectionId}")
	public @ResponseBody int terminateConnection(@PathVariable long connectionId, Authentication auth,
			HttpServletResponse response) {
		int result = connectionsDao.terminateConnectionByConnectionId(connectionId);
		setProfileRefreshHeader(response, result, "Connection removed");
		return result;
	}

	private void setProfileRefreshHeader(HttpServletResponse response, int result, String successMessage) {
		String type = result == 1 ? "success" : "error";
		String message = result == 1 ? successMessage : "The connection action could not be completed.";
		response.setHeader("HX-Trigger-After-Settle", "{\"profileConnectionsChanged\":null,\"notify\":{\"type\":\""
				+ type + "\",\"message\":\"" + message + "\"}}");
	}

}
