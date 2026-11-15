package com.app.projectory.controller;

import com.app.projectory.dao.ProjectRepository;
import com.app.projectory.dao.ProjectTaskRepository;
import com.app.projectory.dao.UsersRepository;
import com.app.projectory.dto.BoardMemberView;
import com.app.projectory.dto.BoardTaskView;
import com.app.projectory.dto.ProjectDto;
import com.app.projectory.dto.ProjectMembersDto;
import com.app.projectory.dto.ProjectStatusCount;
import com.app.projectory.dto.ProjectTasksDto;
import com.app.projectory.entity.Project;
import com.app.projectory.entity.ProjectTasks;
import com.app.projectory.entity.Users;
import com.app.projectory.service.userAccountService;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/project")
public class ProjectController {

  @Autowired private ProjectRepository projDao;
  @Autowired private ProjectTaskRepository projTaskDao;
  @Autowired private userAccountService userServ;
  @Autowired private UsersRepository userRepo;

  @GetMapping("/board")
  public String serveBoard(Model model, Authentication auth, HttpServletRequest request) {
    model.addAttribute("currentUserDetail", userServ.getCurrentUserDetail(auth));
    model.addAttribute(
        "boardProjects", projDao.findProjectListByUserIncUsername(userServ.getUserId(auth)));
    model.addAttribute("selectedProjectId", getSelectedProjectId(request));
    model.addAttribute("currentPage", "board");
    addBoardUrls(model, false);
    return "/user/user-content-container";
  }

  private void addBoardTasksModel(long projectId, Authentication auth, Model model) {
    long userId = userServ.getUserId(auth);
    model.addAttribute("selectedProjectId", projectId);
    List<BoardMemberView> members =
        projDao.findProjectMembersByProjectId(projectId, userId).stream()
            .map(member ->
                new BoardMemberView(
                    String.valueOf(member.getProjectMemberUserId()),
                    member.getUsername(),
                    "@" + member.getUsername(),
                    "/user/" + member.getUsername()))
            .collect(java.util.stream.Collectors.toList());
    Map<String, BoardMemberView> membersById = new HashMap<>();
    for (BoardMemberView member : members) {
      membersById.put(member.getMemberId(), member);
    }
    List<BoardTaskView> boardTasks =
        projDao.findProjectTasksByProject(projectId, userId).stream()
            .map(task -> {
              String assigneeId =
                  task.getAssigneeId() > 0 ? String.valueOf(task.getAssigneeId()) : null;
              BoardMemberView assignee = assigneeId == null ? null : membersById.get(assigneeId);
              return new BoardTaskView(
                  task.getTaskId(),
                  task.getTaskName(),
                  task.getTaskDescription(),
                  task.getStatus(),
                  assigneeId,
                  task.getAssigneeUsername(),
                  assignee == null
                      ? (task.getAssigneeUsername() == null
                          ? null
                          : "/user/" + task.getAssigneeUsername())
                      : assignee.getProfileUrl(),
                  assigneeId != null && assigneeId.equals(String.valueOf(userId)));
            })
            .collect(java.util.stream.Collectors.toList());
    model.addAttribute("boardTasks", boardTasks);
    model.addAttribute("projectMembers", members);
    addBoardUrls(model, false);
  }

  private void addBoardUrls(Model model, boolean guestMode) {
    model.addAttribute("boardGuestMode", guestMode);
    model.addAttribute(
        "boardProjectOptionsUrl",
        guestMode ? "/guest/board/projects" : "/project/fragment/board-projects");
    model.addAttribute(
        "boardTasksUrl", guestMode ? "/guest/board/tasks" : "/project/fragment/board-tasks");
    model.addAttribute(
        "boardStatusUrl", guestMode ? "/guest/task/update/status" : "/project/task/update/status");
    model.addAttribute(
        "boardAssigneeUrl",
        guestMode ? "/guest/task/update/assignee" : "/project/task/update/assignee");
  }

  @PostMapping("add-project")
  public @ResponseBody int addTodoUsingJs(
      @RequestParam String title,
      @RequestParam String description,
      @RequestParam String status,
      Authentication auth,
      HttpServletResponse response) {
    try {
      Project project = new Project();
      project.setTitle(title);
      project.setDescription(description);
      project.setStatus(status);
      project.setProjectOwner(userServ.getCurrentUserDetail(auth));
      projDao.save(project);
      setProjectRefreshHeader(response, "projectChanged", 1, "Project created");

    } catch (Exception error) {
      setProjectRefreshHeader(response, "projectChanged", 0, "Project could not be created");
      return 0; // "project creation - error
    }
    return 1; // "project creation - success"
  }

  /*
   * @GetMapping("/tess") public @ResponseBody int tess(Authentication auth) {
   * long authUserId = userServ.getUserId(auth); long projectId = 57; return
   *
   * }
   */

  @PostMapping("/delete")
  public @ResponseBody int deleteProject(
      @RequestParam("project") long projectId, Authentication auth, HttpServletResponse response) {
    long authUserId = userServ.getUserId(auth);
    boolean safeToDelete = false;
    if (projDao.projectHasMembers(projectId) > 0) { // check if project has members
      if (projDao.deleteProjectMembersOfaProject(projectId, authUserId) > 0) safeToDelete = true;
    } else {
      safeToDelete = true;
    }

    if (safeToDelete) {
      projTaskDao.deleteAllTasksByProject(projectId, authUserId);
      int result = (projDao.deleteProject(projectId, authUserId) > 0) ? 1 : 0;
      setProjectRefreshHeader(
          response,
          "projectChanged",
          result,
          result == 1 ? "Project deleted" : "Project could not be deleted");
      return result;
    }
    setProjectRefreshHeader(response, "projectChanged", 0, "Project could not be deleted");
    return 0;
  }

  @PostMapping("/add-project-task")
  @ResponseBody
  public int addProjectTask(
      @RequestParam("title") String title,
      @RequestParam("desc") String description,
      @RequestParam("project") long projId,
      @RequestParam String status,
      Authentication auth,
      HttpServletResponse response) {

    if (!title.isEmpty() && !description.isEmpty() && projId > 0) {
      ProjectTasks task = new ProjectTasks();
      Optional<Project> project = projDao.findById(projId);
      if (!project.isPresent()) {
        setProjectRefreshHeader(
            response, "projectTasksChanged", 0, "Project task could not be added");
        return 0;
      }
      task.setContainerProject(project.get());
      /* project.orElse(defaultApplicationType); */
      task.setTaskName(title);
      task.setTaskDescription(description);
      task.setStatus(status);
      task.setAssignee(0L);
      projTaskDao.save(task);
      setProjectRefreshHeader(response, "projectTasksChanged", 1, "Project task added");
      return 1;
    }
    setProjectRefreshHeader(response, "projectTasksChanged", 0, "Project task could not be added");
    return 0;
  }

  @GetMapping("/fragment/list")
  public String projectListFragment(Authentication auth, Model model) {
    addProjectListModel(auth, model);
    return "user/project/project-layouts :: project-list-table";
  }

  @GetMapping("/fragment/count")
  public String projectCountFragment(Authentication auth, Model model) {
    addProjectCountModel(auth, model);
    return "user/project/project-layouts :: project-count-info";
  }

  @GetMapping("/fragment/tasks")
  public String projectTasksFragment(
      @RequestParam("project") long projectId, Authentication auth, Model model) {
    long userId = userServ.getUserId(auth);
    model.addAttribute("projectId", projectId);
    model.addAttribute("projectTasks", projDao.findProjectTasksByProject(projectId, userId));
    return "user/project/project-layouts :: project-task-list";
  }

  @GetMapping("/fragment/members")
  public String projectMembersFragment(
      @RequestParam("project") long projectId, Authentication auth, Model model) {
    long userId = userServ.getUserId(auth);
    model.addAttribute("projectId", projectId);
    model.addAttribute("projectMembers", projDao.findProjectMembersByProjectId(projectId, userId));
    return "user/project/project-layouts :: project-member-list";
  }

  @GetMapping("/fragment/board-projects")
  public String boardProjectsFragment(
      Authentication auth, HttpServletRequest request, Model model) {
    model.addAttribute(
        "boardProjects", projDao.findProjectListByUserIncUsername(userServ.getUserId(auth)));
    model.addAttribute("selectedProjectId", getSelectedProjectId(request));
    return "user/board/board-layouts :: project-selector-options";
  }

  @GetMapping("/fragment/board-tasks")
  public String boardTasksFragment(
      @RequestParam("project") long projectId, Authentication auth, Model model) {
    addBoardTasksModel(projectId, auth, model);
    return "user/board/board-layouts :: board-columns";
  }

  private void addProjectListModel(Authentication auth, Model model) {
    long userId = userServ.getUserId(auth);
    List<ProjectDto> projects = projDao.findProjectListByUserIncUsername(userId);
    Map<Long, List<ProjectTasksDto>> tasksByProject = new HashMap<>();
    Map<Long, List<ProjectMembersDto>> membersByProject = new HashMap<>();
    for (ProjectDto project : projects) {
      long projectId = project.getProjectId();
      tasksByProject.put(projectId, projDao.findProjectTasksByProject(projectId, userId));
      membersByProject.put(projectId, projDao.findProjectMembersByProjectId(projectId, userId));
    }
    model.addAttribute("projects", projects);
    model.addAttribute("tasksByProject", tasksByProject);
    model.addAttribute("projectMembers", membersByProject);
    model.addAttribute("currentUserDetail", userServ.getCurrentUserDetail(auth));
  }

  private void addProjectCountModel(Authentication auth, Model model) {
    long userId = userServ.getUserId(auth);
    model.addAttribute("totalProjectCount", projDao.countProjectForUser(userId));
    model.addAttribute("ownedProjectCount", projDao.countProjectsCreatedByUser(userId));
    model.addAttribute("joinedProjectCount", projDao.countJoinedProjects(userId));
  }

  private void setProjectRefreshHeader(
      HttpServletResponse response, String eventName, int result, String message) {
    String type = result == 1 ? "success" : "error";
    response.setHeader(
        "HX-Trigger",
        "{\""
            + eventName
            + "\":null,\"notify\":{\"type\":\""
            + type
            + "\",\"message\":\""
            + message
            + "\"}}");
  }

  private void setNotificationHeader(
      HttpServletResponse response, int result, String successMessage, String errorMessage) {
    String type = result == 1 ? "success" : "error";
    String message = result == 1 ? successMessage : errorMessage;
    response.setHeader(
        "HX-Trigger", "{\"notify\":{\"type\":\"" + type + "\",\"message\":\"" + message + "\"}}");
  }

  private Long getSelectedProjectId(HttpServletRequest request) {
    if (request.getCookies() == null) {
      return null;
    }
    for (Cookie cookie : request.getCookies()) {
      if ("agile-board-project".equals(cookie.getName())) {
        try {
          return Long.parseLong(cookie.getValue());
        } catch (NumberFormatException ignored) {
          return null;
        }
      }
    }
    return null;
  }

  @GetMapping("/getProjects")
  public @ResponseBody List<ProjectDto> serveProjects(Authentication auth, Principal p) {
    // get user id from user account service
    long authUserId = userServ.getUserId(auth);
    // get projects for specific user
    return projDao.findProjectListByUserIncUsername(authUserId);
    // return null;
  }

  @GetMapping("/getProjectTasks")
  public @ResponseBody List<ProjectTasksDto> serveProjectTaskById(
      Authentication auth, @RequestParam("project") long projectId) {
    // get user id from user account service
    long userId = userServ.getUserId(auth);
    // get project tasks for specific user
    return projDao.findProjectTasksByProject(projectId, userId);
    // return null;
  }

  // Project count
  @GetMapping("/getProjectCount")
  public @ResponseBody long serveProjectCount(Authentication auth) {
    // get user id from user account service
    long userId = userServ.getUserId(auth);
    // get projects for specific user
    return projDao.countProjectForUser(userId);
  }

  @GetMapping("/getOwnProjectsCount")
  public @ResponseBody long serveOwnProjectCount(Authentication auth) {
    // get user id from user account service
    long userId = userServ.getUserId(auth);
    // get projects for specific user
    return projDao.countProjectsCreatedByUser(userId);
  }

  @GetMapping("/getJoinedProjectsCount")
  public @ResponseBody long serveJoinedProjectCount(Authentication auth) {
    // get user id from user account service
    long userId = userServ.getUserId(auth);
    // get joined projects for specific user
    return projDao.countJoinedProjects(userId);
  }

  // Project count ended

  // project label count
  @GetMapping("/status/count")
  public @ResponseBody List<ProjectStatusCount> serveProjectStatusCount(Authentication auth) {
    long userId = userServ.getUserId(auth);
    return projDao.countAllProjectStatus(userId);
  }

  @GetMapping("/findByUsername")
  public @ResponseBody Users findByUsername(@RequestParam("user") String username) {
    return userRepo.findByUsername(username);
  }

  // get project members
  @GetMapping("/members")
  public @ResponseBody List<ProjectMembersDto> serveProjectMembersById(
      Authentication auth, @RequestParam("project") long projectId) {
    // get user id from user account service
    long userId = userServ.getUserId(auth);
    // get project member for specific project
    return projDao.findProjectMembersByProjectId(projectId, userId);
    // return null;
  }

  // add project member
  // returns 1 if successful or 0 if not
  @PostMapping("/addProjectMember")
  public @ResponseBody int addProjectMember(
      @RequestParam("project") long projectId,
      @RequestParam("user") String username,
      Authentication auth,
      HttpServletResponse response) {
    if (userRepo.findByUsername(username) != null) {
      long userId = userRepo.findByUsername(username).getUserId();
      long authUserId = userServ.getUserId(auth);
      int result = projDao.addProjectMember(projectId, userId, authUserId);
      setProjectRefreshHeader(
          response,
          "projectMembersChanged",
          result,
          result == 1 ? "Project member added" : "Project member could not be added");
      return result;
    }
    setProjectRefreshHeader(
        response, "projectMembersChanged", 0, "Project member could not be added");
    return 0;
  }

  // remove project member
  // returns 1 if successful or 0 if not
  @PostMapping("/removeProjectMember")
  public @ResponseBody int removeProjectMember(
      @RequestParam("project") long projectId,
      @RequestParam("user") String username,
      Authentication auth,
      HttpServletResponse response) {
    if (userRepo.findByUsername(username) != null) {
      long userId = userRepo.findByUsername(username).getUserId(); // subject user id
      long authUserId = userServ.getUserId(auth); // action performer user id
      int result = projDao.removeProjectMember(projectId, userId, authUserId);
      setProjectRefreshHeader(
          response,
          "projectMembersChanged",
          result,
          result == 1 ? "Project member removed" : "Project member could not be removed");
      return result;
    }
    setProjectRefreshHeader(
        response, "projectMembersChanged", 0, "Project member could not be removed");
    return 0;
  }

  @GetMapping("/task")
  public @ResponseBody List<ProjectTasksDto> serverProjectTasks(
      @RequestParam("project") long projectId, Authentication auth) {
    long userId = userServ.getUserId(auth);
    return projTaskDao.findProjectTasks(projectId, userId);
  }

  @PostMapping("/task/update/status")
  public Object updateProjectTaskStatus(
      @RequestParam("status") String statusUpdate,
      @RequestParam("task") long taskId,
      @RequestParam(value = "project", required = false) Long projectId,
      Authentication auth,
      Model model,
      HttpServletResponse response) {
    long userId = userServ.getUserId(auth);
    int result = projTaskDao.updateProjectStatus(taskId, statusUpdate, userId);
    if (projectId != null) {
      addBoardTasksModel(projectId, auth, model);
      setNotificationHeader(
          response, result, "Task status updated", "Task status could not be updated");
      return new ModelAndView("user/board/board-layouts :: board-columns", model.asMap());
    }
    setNotificationHeader(
        response, result, "Task status updated", "Task status could not be updated");
    return ResponseEntity.ok(result);
  }

  @PostMapping("/task/update/assignee")
  public Object addProjectTaskAssignee(
      @RequestParam("user") long assigneeUserId,
      @RequestParam("task") long taskId,
      @RequestParam(value = "project", required = false) Long projectId,
      Authentication auth,
      Model model,
      HttpServletResponse response) {
    long authUserId = userServ.getUserId(auth);
    int result = projTaskDao.updateProjectTaskAssignee(taskId, assigneeUserId, authUserId);
    if (projectId != null) {
      addBoardTasksModel(projectId, auth, model);
      setNotificationHeader(response, result, "Task assigned", "Task could not be assigned");
      return new ModelAndView("user/board/board-layouts :: board-columns", model.asMap());
    }
    setNotificationHeader(response, result, "Task assigned", "Task could not be assigned");
    return ResponseEntity.ok(result);
  }

  @PostMapping("/task/delete/{taskId}")
  public @ResponseBody int deleteTask(
      @PathVariable long taskId, Authentication auth, HttpServletResponse response) {
    long authUserId = userServ.getUserId(auth);
    int result = projTaskDao.deleteTask(taskId, authUserId);
    setProjectRefreshHeader(
        response,
        "projectTasksChanged",
        result,
        result == 1 ? "Project task deleted" : "Project task could not be deleted");
    return result;
  }

  /**
   * HTMX: board peek card on the dashboard. Reads the 'agile-board-project' cookie to know which
   * project is selected, then returns the task list fragment for that project.
   */
  @GetMapping("/fragment/board-peek")
  public String boardPeekFragment(HttpServletRequest request, Authentication auth, Model model) {
    long userId = userServ.getUserId(auth);
    Long selectedProjectId = null;

    if (request.getCookies() != null) {
      for (Cookie c : request.getCookies()) {
        if ("agile-board-project".equals(c.getName())) {
          try {
            selectedProjectId = Long.parseLong(c.getValue());
          } catch (NumberFormatException ignored) {
          }
          break;
        }
      }
    }

    if (selectedProjectId != null) {
      model.addAttribute(
          "boardPeekTasks", projDao.findProjectTasksByProject(selectedProjectId, userId));
      model.addAttribute("boardPeekProjectId", selectedProjectId);
    } else {
      model.addAttribute("boardPeekTasks", List.of());
      model.addAttribute("boardPeekProjectId", null);
    }
    return "user/project/project-layouts :: board-peek-content";
  }
}
