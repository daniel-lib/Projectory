package com.app.projectory.controller;

import com.app.projectory.dto.BoardMemberView;
import com.app.projectory.dto.BoardTaskView;
import com.app.projectory.entity.GuestParticipant;
import com.app.projectory.entity.GuestTask;
import com.app.projectory.security.GuestCookie;
import com.app.projectory.security.GuestPrincipal;
import com.app.projectory.service.GuestSessionService;
import com.app.projectory.service.GuestWorkspaceService;
import com.app.projectory.service.GuestWorkspaceService.GuestDashboard;
import com.app.projectory.service.GuestWorkspaceService.GuestEntry;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/guest")
public class GuestWorkspaceController {
  private final GuestWorkspaceService workspaceService;
  private final GuestSessionService sessionService;

  public GuestWorkspaceController(
      GuestWorkspaceService workspaceService, GuestSessionService sessionService) {
    this.workspaceService = workspaceService;
    this.sessionService = sessionService;
  }

  @PostMapping("/start")
  public String startWorkspace(
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes redirectAttributes) {
    GuestEntry entry = workspaceService.start(request);
    GuestCookie.set(
        response,
        entry.getAccessToken(),
        request.isSecure(),
        Duration.between(Instant.now(), entry.getWorkspace().getExpiresAt()).getSeconds());
    redirectAttributes.addFlashAttribute("inviteCode", entry.getInviteCode());
    redirectAttributes.addFlashAttribute(
        "guestDisplayName", entry.getParticipant().getDisplayName());
    redirectAttributes.addFlashAttribute(
        "guestExpiresAt", entry.getWorkspace().getExpiresAt().toEpochMilli());
    return "redirect:/guest/welcome";
  }

  @PostMapping("/join")
  public String joinWorkspace(
      @RequestParam("inviteCode") String inviteCode,
      HttpServletRequest request,
      HttpServletResponse response,
      RedirectAttributes redirectAttributes) {
    GuestEntry entry = workspaceService.redeem(inviteCode, request);
    GuestCookie.set(
        response,
        entry.getAccessToken(),
        request.isSecure(),
        Duration.between(Instant.now(), entry.getWorkspace().getExpiresAt()).getSeconds());
    redirectAttributes.addFlashAttribute(
        "guestDisplayName", entry.getParticipant().getDisplayName());
    return "redirect:/guest/dashboard";
  }

  @GetMapping("/welcome")
  public String welcome(@AuthenticationPrincipal GuestPrincipal principal, Model model) {
    model.addAttribute("currentGuest", workspaceService.getParticipant(principal));
    return "guest/welcome";
  }

  @GetMapping("/dashboard")
  public String dashboard(@AuthenticationPrincipal GuestPrincipal principal, Model model) {
    addGuestModel(principal, model, "dashboard");
    return "guest/dashboard";
  }

  @GetMapping("/board")
  public String board(@AuthenticationPrincipal GuestPrincipal principal, Model model) {
    addGuestModel(principal, model, "board");
    GuestDashboard dashboard = workspaceService.dashboard(principal);
    Long selectedProjectId =
        dashboard.getProjects().isEmpty() ? null : dashboard.getProjects().get(0).getProjectId();
    model.addAttribute("boardProjects", dashboard.getProjects());
    model.addAttribute("selectedProjectId", selectedProjectId);
    if (selectedProjectId != null) {
      addBoardColumnsModel(principal, dashboard, selectedProjectId, model);
    } else {
      model.addAttribute("boardTasks", java.util.Collections.emptyList());
      model.addAttribute("projectMembers", java.util.Collections.emptyList());
    }
    addBoardUrls(model);
    return "guest/board";
  }

  @GetMapping("/board/projects")
  public String boardProjects(@AuthenticationPrincipal GuestPrincipal principal, Model model) {
    List<com.app.projectory.entity.GuestProject> projects =
        workspaceService.dashboard(principal).getProjects();
    model.addAttribute("boardProjects", projects);
    model.addAttribute(
        "selectedProjectId", projects.isEmpty() ? null : projects.get(0).getProjectId());
    return "user/board/board-layouts :: project-selector-options";
  }

  @GetMapping("/board/tasks")
  public String boardTasks(
      @AuthenticationPrincipal GuestPrincipal principal,
      @RequestParam("project") long projectId,
      Model model) {
    addBoardColumnsModel(principal, workspaceService.dashboard(principal), projectId, model);
    addBoardUrls(model);
    return "user/board/board-layouts :: board-columns";
  }

  @PostMapping("/task/update/status")
  public String updateBoardTaskStatus(
      @AuthenticationPrincipal GuestPrincipal principal,
      @RequestParam("status") String status,
      @RequestParam("task") long taskId,
      @RequestParam("project") long projectId,
      Model model,
      HttpServletResponse response) {
    workspaceService.updateTaskStatus(principal, taskId, status);
    addBoardColumnsModel(principal, workspaceService.dashboard(principal), projectId, model);
    addBoardUrls(model);
    setNotificationHeader(response, "Task status updated");
    return "user/board/board-layouts :: board-columns";
  }

  @PostMapping("/task/update/assignee")
  public String updateBoardTaskAssignee(
      @AuthenticationPrincipal GuestPrincipal principal,
      @RequestParam("user") String assigneeId,
      @RequestParam("task") long taskId,
      @RequestParam("project") long projectId,
      Model model,
      HttpServletResponse response) {
    workspaceService.assignTask(principal, taskId, assigneeId);
    addBoardColumnsModel(principal, workspaceService.dashboard(principal), projectId, model);
    addBoardUrls(model);
    setNotificationHeader(response, "Task assigned");
    return "user/board/board-layouts :: board-columns";
  }

  @GetMapping("/connections")
  public String connections(@AuthenticationPrincipal GuestPrincipal principal, Model model) {
    addGuestModel(principal, model, "connections");
    return "guest/connections";
  }

  @GetMapping("/profile/{participantId}")
  public String profile(
      @AuthenticationPrincipal GuestPrincipal principal,
      @PathVariable String participantId,
      Model model) {
    addGuestModel(principal, model, "profile");
    GuestDashboard dashboard = workspaceService.dashboard(principal);
    GuestParticipant profile = workspaceService.getParticipant(principal, participantId);
    List<GuestTask> assignedTasks =
        dashboard.getTasks().stream()
            .filter(task -> participantId.equals(task.getAssigneeId()))
            .collect(Collectors.toList());
    model.addAttribute("guestProfile", profile);
    model.addAttribute(
        "profileProjects",
        dashboard.getProjects().stream()
            .filter(project -> participantId.equals(project.getCreatedBy()))
            .collect(Collectors.toList()));
    model.addAttribute("profileTaskCount", assignedTasks.size());
    model.addAttribute(
        "profileConnectionCount",
        (long)
            dashboard.getConnections().stream()
                .filter(connection ->
                    "approved".equals(connection.getStatus())
                        && (participantId.equals(connection.getSenderId())
                            || participantId.equals(connection.getReceiverId())))
                .count());
    model.addAttribute("isOwnGuestProfile", principal.getParticipantId().equals(participantId));
    return "guest/profile";
  }

  @PostMapping("/project")
  public String createProject(
      @AuthenticationPrincipal GuestPrincipal principal,
      @RequestParam String title,
      @RequestParam(defaultValue = "") String description,
      HttpServletRequest request,
      HttpServletResponse response,
      Model model) {
    workspaceService.createProject(principal, title, description);
    return dashboardMutationResponse(principal, request, response, model, "Project created");
  }

  @PostMapping("/task")
  public String createTask(
      @AuthenticationPrincipal GuestPrincipal principal,
      @RequestParam long projectId,
      @RequestParam String title,
      @RequestParam(defaultValue = "") String description,
      @RequestParam(required = false) String assigneeId,
      HttpServletRequest request,
      HttpServletResponse response,
      Model model) {
    workspaceService.createTask(principal, projectId, title, description, assigneeId);
    return dashboardMutationResponse(principal, request, response, model, "Task added");
  }

  @PostMapping("/task/status")
  public String updateTaskStatus(
      @AuthenticationPrincipal GuestPrincipal principal,
      @RequestParam long taskId,
      @RequestParam String status) {
    workspaceService.updateTaskStatus(principal, taskId, status);
    return "redirect:/guest/dashboard";
  }

  @PostMapping("/task/assignee")
  public String assignTask(
      @AuthenticationPrincipal GuestPrincipal principal,
      @RequestParam long taskId,
      @RequestParam(required = false) String assigneeId) {
    workspaceService.assignTask(principal, taskId, assigneeId);
    return "redirect:/guest/dashboard";
  }

  @PostMapping("/connection/request")
  public String sendConnectionRequest(@AuthenticationPrincipal GuestPrincipal principal) {
    workspaceService.sendConnectionRequest(principal);
    return "redirect:/guest/dashboard";
  }

  @PostMapping("/connection/respond")
  public String respondToConnection(
      @AuthenticationPrincipal GuestPrincipal principal,
      @RequestParam long connectionId,
      @RequestParam boolean accept) {
    workspaceService.respondToConnection(principal, connectionId, accept);
    return "redirect:/guest/dashboard";
  }

  @PostMapping("/collection")
  public String createCollection(
      @AuthenticationPrincipal GuestPrincipal principal,
      @RequestParam String title,
      HttpServletRequest request,
      HttpServletResponse response,
      Model model) {
    workspaceService.createCollection(principal, title);
    return dashboardMutationResponse(principal, request, response, model, "Todo list created");
  }

  @PostMapping("/todo")
  public String addTodo(
      @AuthenticationPrincipal GuestPrincipal principal,
      @RequestParam long collectionId,
      @RequestParam String title,
      HttpServletRequest request,
      HttpServletResponse response,
      Model model) {
    workspaceService.addTodo(principal, collectionId, title);
    return dashboardMutationResponse(principal, request, response, model, "Todo added");
  }

  @PostMapping("/todo/toggle")
  public String toggleTodo(
      @AuthenticationPrincipal GuestPrincipal principal,
      @RequestParam long itemId,
      HttpServletRequest request,
      HttpServletResponse response,
      Model model) {
    workspaceService.toggleTodo(principal, itemId);
    return dashboardMutationResponse(principal, request, response, model, "Todo updated");
  }

  @PostMapping("/logout")
  public String logout(HttpServletRequest request, HttpServletResponse response) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (GuestCookie.NAME.equals(cookie.getName())) {
          sessionService.revoke(cookie.getValue());
          break;
        }
      }
    }
    GuestCookie.clear(response, request.isSecure());
    return "redirect:/";
  }

  private void addGuestModel(GuestPrincipal principal, Model model, String currentPage) {
    model.addAttribute("guestData", workspaceService.dashboard(principal));
    model.addAttribute("currentGuestId", principal.getParticipantId());
    model.addAttribute("currentGuestName", principal.getDisplayName());
    model.addAttribute("guestInviteCode", workspaceService.getInvitationCode(principal));
    model.addAttribute("guestCurrentPage", currentPage);
  }

  private String dashboardMutationResponse(
      GuestPrincipal principal,
      HttpServletRequest request,
      HttpServletResponse response,
      Model model,
      String message) {
    if ("true".equalsIgnoreCase(request.getHeader("HX-Request"))) {
      addGuestModel(principal, model, "dashboard");
      setNotificationHeader(response, message);
      return "guest/dashboard :: dashboard-content";
    }
    return "redirect:/guest/dashboard";
  }

  private void setNotificationHeader(HttpServletResponse response, String message) {
    response.setHeader(
        "HX-Trigger", "{\"notify\":{\"type\":\"success\",\"message\":\"" + message + "\"}}");
  }

  private void addBoardColumnsModel(
      GuestPrincipal principal, GuestDashboard dashboard, long projectId, Model model) {
    if (dashboard.getProjects().stream()
        .noneMatch(project -> project.getProjectId() == projectId)) {
      throw new org.springframework.web.server.ResponseStatusException(
          org.springframework.http.HttpStatus.NOT_FOUND);
    }
    List<BoardMemberView> members =
        dashboard.getParticipants().stream()
            .map(person ->
                new BoardMemberView(
                    person.getParticipantId(),
                    person.getParticipantId().equals(principal.getParticipantId())
                        ? "You"
                        : person.getDisplayName(),
                    person.getDisplayName(),
                    "/guest/profile/" + person.getParticipantId()))
            .collect(Collectors.toList());
    List<BoardTaskView> tasks =
        dashboard.getTasks().stream()
            .filter(task -> task.getProjectId() == projectId)
            .map(task -> {
              GuestParticipant assignee =
                  dashboard.getParticipants().stream()
                      .filter(person -> person.getParticipantId().equals(task.getAssigneeId()))
                      .findFirst()
                      .orElse(null);
              return new BoardTaskView(
                  task.getTaskId(),
                  task.getTitle(),
                  task.getDescription(),
                  task.getStatus(),
                  task.getAssigneeId(),
                  assignee == null ? null : assignee.getDisplayName(),
                  assignee == null ? null : "/guest/profile/" + assignee.getParticipantId(),
                  principal.getParticipantId().equals(task.getAssigneeId()));
            })
            .collect(Collectors.toList());
    model.addAttribute("selectedProjectId", projectId);
    model.addAttribute("boardTasks", tasks);
    model.addAttribute("projectMembers", members);
    model.addAttribute("boardProjects", dashboard.getProjects());
  }

  private void addBoardUrls(Model model) {
    model.addAttribute("boardGuestMode", true);
    model.addAttribute("boardProjectOptionsUrl", "/guest/board/projects");
    model.addAttribute("boardTasksUrl", "/guest/board/tasks");
    model.addAttribute("boardStatusUrl", "/guest/task/update/status");
    model.addAttribute("boardAssigneeUrl", "/guest/task/update/assignee");
  }
}
