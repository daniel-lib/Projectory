package com.app.projectory;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.projectory.dao.GuestConnectionRepository;
import com.app.projectory.dao.GuestParticipantRepository;
import com.app.projectory.dao.GuestProjectRepository;
import com.app.projectory.dao.GuestTaskRepository;
import com.app.projectory.dao.GuestTodoCollectionRepository;
import com.app.projectory.dao.GuestTodoItemRepository;
import com.app.projectory.dao.GuestWorkspaceRepository;
import com.app.projectory.dao.SystemAccessTrackerRepository;
import com.app.projectory.dao.UsersRepository;
import com.app.projectory.entity.SystemAccessTracker;
import com.app.projectory.entity.Users;
import com.app.projectory.security.GuestPrincipal;
import com.app.projectory.service.GuestSessionService;
import java.time.Instant;
import java.util.UUID;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GuestWorkspaceFlowTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private GuestSessionService guestSessionService;
  @Autowired private GuestParticipantRepository participantRepository;
  @Autowired private GuestProjectRepository projectRepository;
  @Autowired private GuestTaskRepository taskRepository;
  @Autowired private GuestConnectionRepository connectionRepository;
  @Autowired private GuestTodoCollectionRepository collectionRepository;
  @Autowired private GuestTodoItemRepository todoRepository;
  @Autowired private GuestWorkspaceRepository workspaceRepository;
  @Autowired private UsersRepository usersRepository;
  @Autowired private SystemAccessTrackerRepository accessTrackerRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  @Test
  void anonymousPageVisitsReuseOneTrackerRecordPerBrowserIdentifier() throws Exception {
    String visitorId = UUID.randomUUID().toString();
    String actorIdentifier = "UNAUTHENTICATED_" + visitorId;

    mockMvc
        .perform(
            post("/system-access/visit")
                .with(csrf())
                .header("X-Visitor-Id", visitorId)
                .header("User-Agent", "Mozilla/5.0 Chrome/123.0"))
        .andExpect(status().isNoContent());
    long accessId =
        accessTrackerRepository.findByActorIdentifier(actorIdentifier).orElseThrow().getAccessId();

    mockMvc
        .perform(
            post("/system-access/visit")
                .with(csrf())
                .header("X-Visitor-Id", visitorId)
                .header("User-Agent", "Mozilla/5.0 Firefox/124.0"))
        .andExpect(status().isNoContent());

    SystemAccessTracker trackedVisit =
        accessTrackerRepository.findByActorIdentifier(actorIdentifier).orElseThrow();
    assertEquals(accessId, trackedVisit.getAccessId());
    assertEquals("Mozilla/5.0 Firefox/124.0", trackedVisit.getUserAgent());
    assertEquals("127.0.0.1", trackedVisit.getIpAddress());
  }

  @Test
  void inviteIsSingleUseAndGuestDataStaysOutOfRegisteredUserRoutes() throws Exception {
    MvcResult creatorStart =
        mockMvc
            .perform(post("/guest/start").with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/guest/welcome"))
            .andReturn();
    String inviteCode = (String) creatorStart.getFlashMap().get("inviteCode");
    Cookie creatorCookie = guestCookie(creatorStart);
    assertNotNull(inviteCode);
    assertNotNull(creatorCookie);
    assertNotNull(guestSessionService.resolve(creatorCookie.getValue()).orElse(null));
    org.junit.jupiter.api.Assertions.assertTrue(
        accessTrackerRepository.findAll().stream()
            .anyMatch(entry ->
                entry.isGuestAccess()
                    && creatorCookie.getValue() != null
                    && "127.0.0.1".equals(entry.getIpAddress())));
    mockMvc
        .perform(get("/guest/welcome").cookie(creatorCookie).flashAttrs(creatorStart.getFlashMap()))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(inviteCode)))
        .andExpect(content().string(containsString("Copy invitation code")));

    MvcResult initialDashboard =
        mockMvc.perform(get("/guest/dashboard").cookie(creatorCookie)).andReturn();
    assertEquals(
        200,
        initialDashboard.getResponse().getStatus(),
        "Unexpected redirect: " + initialDashboard.getResponse().getRedirectedUrl());
    org.junit.jupiter.api.Assertions.assertTrue(
        initialDashboard.getResponse().getContentAsString().contains("Scrum board"));
    org.junit.jupiter.api.Assertions.assertTrue(
        initialDashboard.getResponse().getContentAsString().contains(inviteCode));
    MockHttpSession guestHttpSession =
        (MockHttpSession) initialDashboard.getRequest().getSession(false);
    if (guestHttpSession != null) {
      org.springframework.security.core.context.SecurityContext savedContext =
          (org.springframework.security.core.context.SecurityContext)
              guestHttpSession.getAttribute(
                  org.springframework.security.web.context.HttpSessionSecurityContextRepository
                      .SPRING_SECURITY_CONTEXT_KEY);
      assertNull(savedContext == null ? null : savedContext.getAuthentication());
    }

    mockMvc
        .perform(
            post("/guest/project")
                .with(csrf())
                .cookie(creatorCookie)
                .header("HX-Request", "true")
                .param("title", "Guest-only project")
                .param("description", "Demo"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("guest-dashboard-content")))
        .andExpect(content().string(containsString("Guest-only project")));

    GuestPrincipal creator = guestSessionService.resolve(creatorCookie.getValue()).orElseThrow();
    long projectId =
        projectRepository
            .findAllByWorkspaceIdOrderByCreatedAtDesc(creator.getWorkspaceId())
            .get(0)
            .getProjectId();
    mockMvc
        .perform(
            post("/guest/task")
                .with(csrf())
                .cookie(creatorCookie)
                .header("HX-Request", "true")
                .param("projectId", String.valueOf(projectId))
                .param("title", "Board rendering task"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Board rendering task")));
    long taskId =
        taskRepository
            .findAllByWorkspaceIdOrderByProjectIdAscTaskIdAsc(creator.getWorkspaceId())
            .get(0)
            .getTaskId();

    mockMvc
        .perform(get("/guest/board").cookie(creatorCookie))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Scrum board")))
        .andExpect(content().string(containsString("/guest/board/tasks")));
    mockMvc
        .perform(
            get("/guest/board/tasks")
                .param("project", String.valueOf(projectId))
                .cookie(creatorCookie))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Board rendering task")))
        .andExpect(content().string(containsString("hx-post=\"/guest/task/update/assignee\"")))
        .andExpect(content().string(containsString("hx-include=\"closest form\"")))
        .andExpect(content().string(containsString("action=\"/guest/task/update/assignee\"")))
        .andExpect(content().string(containsString("name=\"_csrf\"")))
        .andExpect(content().string(containsString("data-can-move=\"false\"")));
    mockMvc
        .perform(
            post("/guest/task/update/assignee")
                .with(csrf())
                .cookie(creatorCookie)
                .header("HX-Request", "true")
                .param("task", String.valueOf(taskId))
                .param("project", String.valueOf(projectId))
                .param("user", creator.getParticipantId()))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Board rendering task")))
        .andExpect(content().string(containsString("data-can-move=\"true\"")))
        .andExpect(header().string("HX-Trigger", containsString("Task assigned")));
    mockMvc
        .perform(
            post("/guest/task/update/status")
                .with(csrf().asHeader())
                .cookie(creatorCookie)
                .header("HX-Request", "true")
                .param("task", String.valueOf(taskId))
                .param("project", String.valueOf(projectId))
                .param("status", "in progress"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Board rendering task")))
        .andExpect(header().string("HX-Trigger", containsString("Task status updated")));
    mockMvc
        .perform(get("/guest/connections").cookie(creatorCookie))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Connections")));
    mockMvc
        .perform(
            get("/guest/profile/{participantId}", creator.getParticipantId()).cookie(creatorCookie))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Guest-only project")));

    MvcResult guestRoute =
        mockMvc
            .perform(get("/guest/dashboard").cookie(creatorCookie))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Guest-only project")))
            .andReturn();
    assertNotNull(guestRoute);

    mockMvc
        .perform(get("/user/dashboard").cookie(creatorCookie))
        .andExpect(status().is3xxRedirection());

    MvcResult joined =
        mockMvc
            .perform(post("/guest/join").with(csrf()).param("inviteCode", inviteCode))
            .andExpect(status().is3xxRedirection())
            .andReturn();
    Cookie secondGuestCookie = guestCookie(joined);
    assertNotNull(secondGuestCookie);
    assertNotEquals(creatorCookie.getValue(), secondGuestCookie.getValue());

    mockMvc
        .perform(get("/guest/dashboard").cookie(secondGuestCookie))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Guest-only project")));
    mockMvc
        .perform(
            get("/guest/profile/{participantId}", creator.getParticipantId())
                .cookie(secondGuestCookie))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Guest-only project")));

    mockMvc
        .perform(post("/guest/join").with(csrf()).param("inviteCode", inviteCode))
        .andExpect(status().isBadRequest());
  }

  @Test
  void guestWorkspaceIsolatedFromAnotherGuestWorkspace() throws Exception {
    MvcResult firstStart =
        mockMvc
            .perform(post("/guest/start").with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andReturn();
    Cookie firstCookie = guestCookie(firstStart);
    mockMvc
        .perform(
            post("/guest/project")
                .with(csrf())
                .cookie(firstCookie)
                .param("title", "First workspace"))
        .andExpect(status().is3xxRedirection());

    MvcResult secondStart =
        mockMvc
            .perform(post("/guest/start").with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andReturn();
    Cookie secondCookie = guestCookie(secondStart);
    mockMvc
        .perform(get("/guest/dashboard").cookie(secondCookie))
        .andExpect(status().isOk())
        .andExpect(content().string(org.hamcrest.Matchers.not(containsString("First workspace"))));
  }

  @Test
  void guestsCanCollaborateOnTasksConnectionsAndTodosWithinTheirWorkspace() throws Exception {
    MvcResult creatorStart =
        mockMvc
            .perform(post("/guest/start").with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andReturn();
    Cookie creatorCookie = guestCookie(creatorStart);
    String inviteCode = (String) creatorStart.getFlashMap().get("inviteCode");
    MvcResult guestJoin =
        mockMvc
            .perform(post("/guest/join").with(csrf()).param("inviteCode", inviteCode))
            .andExpect(status().is3xxRedirection())
            .andReturn();
    Cookie secondCookie = guestCookie(guestJoin);
    GuestPrincipal creator = guestSessionService.resolve(creatorCookie.getValue()).orElseThrow();
    GuestPrincipal second = guestSessionService.resolve(secondCookie.getValue()).orElseThrow();

    mockMvc
        .perform(
            post("/guest/project")
                .with(csrf())
                .cookie(creatorCookie)
                .param("title", "Collaboration project"))
        .andExpect(status().is3xxRedirection());
    long projectId =
        projectRepository
            .findAllByWorkspaceIdOrderByCreatedAtDesc(creator.getWorkspaceId())
            .get(0)
            .getProjectId();
    mockMvc
        .perform(
            post("/guest/task")
                .with(csrf())
                .cookie(creatorCookie)
                .param("projectId", String.valueOf(projectId))
                .param("title", "Shared task")
                .param("assigneeId", second.getParticipantId()))
        .andExpect(status().is3xxRedirection());
    assertEquals(
        second.getParticipantId(),
        taskRepository
            .findAllByWorkspaceIdOrderByProjectIdAscTaskIdAsc(creator.getWorkspaceId())
            .get(0)
            .getAssigneeId());

    mockMvc
        .perform(post("/guest/connection/request").with(csrf()).cookie(creatorCookie))
        .andExpect(status().is3xxRedirection());
    long connectionId =
        connectionRepository
            .findAllByWorkspaceIdOrderByCreatedAtAsc(creator.getWorkspaceId())
            .get(0)
            .getConnectionId();
    mockMvc
        .perform(
            post("/guest/connection/respond")
                .with(csrf())
                .cookie(secondCookie)
                .param("connectionId", String.valueOf(connectionId))
                .param("accept", "true"))
        .andExpect(status().is3xxRedirection());
    assertEquals("approved", connectionRepository.findById(connectionId).orElseThrow().getStatus());

    mockMvc
        .perform(
            post("/guest/collection")
                .with(csrf())
                .cookie(creatorCookie)
                .header("HX-Request", "true")
                .param("title", "Shared todos"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Shared todos")));
    long collectionId =
        collectionRepository
            .findAllByWorkspaceIdOrderByCreatedAtAsc(creator.getWorkspaceId())
            .get(0)
            .getCollectionId();
    mockMvc
        .perform(
            post("/guest/todo")
                .with(csrf())
                .cookie(secondCookie)
                .header("HX-Request", "true")
                .param("collectionId", String.valueOf(collectionId))
                .param("title", "Finish demo"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Finish demo")));
    long todoId =
        todoRepository
            .findAllByWorkspaceIdOrderByItemIdAsc(creator.getWorkspaceId())
            .get(0)
            .getItemId();
    mockMvc
        .perform(
            post("/guest/todo/toggle")
                .with(csrf())
                .cookie(creatorCookie)
                .header("HX-Request", "true")
                .param("itemId", String.valueOf(todoId)))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Finish demo")));
    assertEquals(true, todoRepository.findById(todoId).orElseThrow().isCompleted());
  }

  @Test
  void expiredWorkspaceIsRejectedEvenWhenGuestCookieStillExists() throws Exception {
    MvcResult start =
        mockMvc
            .perform(post("/guest/start").with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andReturn();
    Cookie cookie = guestCookie(start);
    GuestPrincipal principal = guestSessionService.resolve(cookie.getValue()).orElseThrow();
    com.app.projectory.entity.GuestWorkspace workspace =
        workspaceRepository.findById(principal.getWorkspaceId()).orElseThrow();
    workspace.setExpiresAt(Instant.now().minusSeconds(1));
    workspaceRepository.save(workspace);

    mockMvc
        .perform(get("/guest/dashboard").cookie(cookie))
        .andExpect(redirectedUrl("/?guest_expired"));
  }

  @Test
  void registeredUserLoginStillUsesExistingAuthenticationAndIsTrackedSeparately() throws Exception {
    Users user = new Users();
    user.setUsername("regular-user");
    user.setPassword(passwordEncoder.encode("regular-password"));
    user.setRole("USER");
    user.setEnabled(true);
    user.setFirstName("Regular");
    user.setLastName("User");
    usersRepository.save(user);

    MvcResult login =
        mockMvc
            .perform(
                post("/login")
                    .with(csrf())
                    .header(
                        "User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                            + "(KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36")
                    .param("username", "regular-user")
                    .param("password", "regular-password"))
            .andExpect(status().is3xxRedirection())
            .andReturn();
    MockHttpSession session = (MockHttpSession) login.getRequest().getSession(false);
    assertNotNull(session);
    mockMvc
        .perform(get("/project/getProjectCount").session(session))
        .andExpect(status().isOk())
        .andExpect(content().string("0"));
    org.junit.jupiter.api.Assertions.assertTrue(
        accessTrackerRepository.findAll().stream()
            .anyMatch(entry ->
                !entry.isGuestAccess()
                    && "regular-user".equals(entry.getActorIdentifier())
                    && "Chrome".equals(entry.getBrowserType())
                    && "Windows".equals(entry.getOperatingSystem())));
  }

  private Cookie guestCookie(MvcResult result) {
    String setCookie = result.getResponse().getHeader("Set-Cookie");
    assertNotNull(setCookie);
    String prefix = "projectory_guest=";
    int valueStart = setCookie.indexOf(prefix) + prefix.length();
    int valueEnd = setCookie.indexOf(';', valueStart);
    return new Cookie("projectory_guest", setCookie.substring(valueStart, valueEnd));
  }
}
