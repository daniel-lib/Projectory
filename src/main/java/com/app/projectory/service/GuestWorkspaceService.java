package com.app.projectory.service;

import com.app.projectory.dao.GuestConnectionRepository;
import com.app.projectory.dao.GuestParticipantRepository;
import com.app.projectory.dao.GuestProjectRepository;
import com.app.projectory.dao.GuestTaskRepository;
import com.app.projectory.dao.GuestTodoCollectionRepository;
import com.app.projectory.dao.GuestTodoItemRepository;
import com.app.projectory.dao.GuestWorkspaceRepository;
import com.app.projectory.entity.GuestConnection;
import com.app.projectory.entity.GuestParticipant;
import com.app.projectory.entity.GuestProject;
import com.app.projectory.entity.GuestTask;
import com.app.projectory.entity.GuestTodoCollection;
import com.app.projectory.entity.GuestTodoItem;
import com.app.projectory.entity.GuestWorkspace;
import com.app.projectory.security.GuestPrincipal;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GuestWorkspaceService {
  private static final Duration WORKSPACE_TTL = Duration.ofHours(1);
  private static final SecureRandom RANDOM = new SecureRandom();
  private final Cache<String, String> invitationCodes =
      Caffeine.newBuilder()
          .expireAfterWrite(WORKSPACE_TTL.toMillis(), TimeUnit.MILLISECONDS)
          .maximumSize(10000)
          .build();

  private final GuestWorkspaceRepository workspaceRepository;
  private final GuestParticipantRepository participantRepository;
  private final GuestProjectRepository projectRepository;
  private final GuestTaskRepository taskRepository;
  private final GuestConnectionRepository connectionRepository;
  private final GuestTodoCollectionRepository collectionRepository;
  private final GuestTodoItemRepository todoRepository;
  private final GuestSessionService sessionService;
  private final SystemAccessTrackerService accessTracker;
  private final GuestAccessRateLimiter rateLimiter;

  public GuestWorkspaceService(
      GuestWorkspaceRepository workspaceRepository,
      GuestParticipantRepository participantRepository,
      GuestProjectRepository projectRepository,
      GuestTaskRepository taskRepository,
      GuestConnectionRepository connectionRepository,
      GuestTodoCollectionRepository collectionRepository,
      GuestTodoItemRepository todoRepository,
      GuestSessionService sessionService,
      SystemAccessTrackerService accessTracker,
      GuestAccessRateLimiter rateLimiter) {
    this.workspaceRepository = workspaceRepository;
    this.participantRepository = participantRepository;
    this.projectRepository = projectRepository;
    this.taskRepository = taskRepository;
    this.connectionRepository = connectionRepository;
    this.collectionRepository = collectionRepository;
    this.todoRepository = todoRepository;
    this.sessionService = sessionService;
    this.accessTracker = accessTracker;
    this.rateLimiter = rateLimiter;
  }

  @Transactional
  public GuestEntry start(HttpServletRequest request) {
    rateLimiter.checkWorkspaceCreation(request.getRemoteAddr());
    Instant now = Instant.now();
    String inviteCode = generateCode();
    GuestWorkspace workspace = new GuestWorkspace();
    workspace.setWorkspaceId(UUID.randomUUID().toString());
    workspace.setInviteCodeHash(GuestSessionService.hash(inviteCode));
    workspace.setInviteRedeemed(false);
    workspace.setCreatedAt(now);
    workspace.setExpiresAt(now.plus(WORKSPACE_TTL));
    workspaceRepository.save(workspace);
    invitationCodes.put(workspace.getWorkspaceId(), inviteCode);

    GuestParticipant participant = newParticipant(workspace, "Guest 1");
    String accessToken = sessionService.createSession(participant, workspace.getExpiresAt());
    accessTracker.record(request, true, participant.getParticipantId());
    return new GuestEntry(inviteCode, accessToken, workspace, participant);
  }

  @Transactional
  public GuestEntry redeem(String inviteCode, HttpServletRequest request) {
    rateLimiter.checkInviteRedemption(request.getRemoteAddr());
    if (inviteCode == null || inviteCode.trim().isEmpty() || inviteCode.length() > 128) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Enter a valid invitation code.");
    }
    GuestWorkspace workspace =
        workspaceRepository
            .findByInviteCodeHashForUpdate(GuestSessionService.hash(inviteCode.trim()))
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "This invitation code is invalid or has already been used."));
    if (!workspace.getExpiresAt().isAfter(Instant.now())
        || workspace.isInviteRedeemed()
        || participantRepository.countByWorkspaceId(workspace.getWorkspaceId()) >= 2) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "This invitation code is expired or has already been used.");
    }

    workspace.setInviteRedeemed(true);
    workspaceRepository.save(workspace);
    invitationCodes.invalidate(workspace.getWorkspaceId());
    GuestParticipant participant = newParticipant(workspace, "Guest 2");
    String accessToken = sessionService.createSession(participant, workspace.getExpiresAt());
    accessTracker.record(request, true, participant.getParticipantId());
    return new GuestEntry(null, accessToken, workspace, participant);
  }

  @Transactional(readOnly = true)
  public GuestDashboard dashboard(GuestPrincipal principal) {
    GuestWorkspace workspace = requireActiveWorkspace(principal.getWorkspaceId());
    List<GuestParticipant> participants =
        participantRepository.findAllByWorkspaceIdOrderByCreatedAtAsc(workspace.getWorkspaceId());
    List<GuestConnection> connections =
        connectionRepository.findAllByWorkspaceIdOrderByCreatedAtAsc(workspace.getWorkspaceId());
    return new GuestDashboard(
        workspace,
        participants,
        projectRepository.findAllByWorkspaceIdOrderByCreatedAtDesc(workspace.getWorkspaceId()),
        taskRepository.findAllByWorkspaceIdOrderByProjectIdAscTaskIdAsc(workspace.getWorkspaceId()),
        connections,
        collectionRepository.findAllByWorkspaceIdOrderByCreatedAtAsc(workspace.getWorkspaceId()),
        todoRepository.findAllByWorkspaceIdOrderByItemIdAsc(workspace.getWorkspaceId()));
  }

  @Transactional(readOnly = true)
  public String getInvitationCode(GuestPrincipal principal) {
    GuestWorkspace workspace = requireActiveWorkspace(principal.getWorkspaceId());
    if (workspace.isInviteRedeemed()) {
      return null;
    }
    return invitationCodes.getIfPresent(workspace.getWorkspaceId());
  }

  @Transactional
  public void createProject(GuestPrincipal principal, String title, String description) {
    String safeTitle = requiredText(title, 120, "Project title");
    GuestWorkspace workspace = requireActiveWorkspace(principal.getWorkspaceId());
    GuestProject project = new GuestProject();
    project.setWorkspaceId(workspace.getWorkspaceId());
    project.setCreatedBy(principal.getParticipantId());
    project.setTitle(safeTitle);
    project.setDescription(optionalText(description, 1000));
    project.setStatus("active");
    project.setCreatedAt(Instant.now());
    projectRepository.save(project);
  }

  @Transactional
  public void createTask(
      GuestPrincipal principal,
      long projectId,
      String title,
      String description,
      String assigneeId) {
    String safeTitle = requiredText(title, 120, "Task title");
    requireActiveWorkspace(principal.getWorkspaceId());
    GuestProject project =
        projectRepository
            .findByProjectIdAndWorkspaceId(projectId, principal.getWorkspaceId())
            .orElseThrow(() -> notFound());
    GuestTask task = new GuestTask();
    task.setWorkspaceId(principal.getWorkspaceId());
    task.setProjectId(project.getProjectId());
    task.setTitle(safeTitle);
    task.setDescription(optionalText(description, 1000));
    task.setStatus("not started");
    task.setAssigneeId(validateAssignee(principal.getWorkspaceId(), assigneeId));
    taskRepository.save(task);
  }

  @Transactional
  public void updateTaskStatus(GuestPrincipal principal, long taskId, String status) {
    requireActiveWorkspace(principal.getWorkspaceId());
    if (!"not started".equals(status) && !"in progress".equals(status) && !"done".equals(status)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Choose a valid task status.");
    }
    GuestTask task =
        taskRepository
            .findByTaskIdAndWorkspaceId(taskId, principal.getWorkspaceId())
            .orElseThrow(() -> notFound());
    task.setStatus(status);
  }

  @Transactional
  public void assignTask(GuestPrincipal principal, long taskId, String assigneeId) {
    requireActiveWorkspace(principal.getWorkspaceId());
    GuestTask task =
        taskRepository
            .findByTaskIdAndWorkspaceId(taskId, principal.getWorkspaceId())
            .orElseThrow(() -> notFound());
    task.setAssigneeId(validateAssignee(principal.getWorkspaceId(), assigneeId));
  }

  @Transactional
  public void sendConnectionRequest(GuestPrincipal principal) {
    GuestWorkspace workspace =
        workspaceRepository
            .findByIdForUpdate(principal.getWorkspaceId())
            .orElseThrow(() -> notFound());
    if (!workspace.getExpiresAt().isAfter(Instant.now())) {
      throw new ResponseStatusException(
          HttpStatus.UNAUTHORIZED, "This guest workspace has expired.");
    }
    List<GuestParticipant> participants =
        participantRepository.findAllByWorkspaceIdOrderByCreatedAtAsc(principal.getWorkspaceId());
    if (participants.size() != 2) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Invite another guest before connecting.");
    }
    GuestParticipant receiver =
        participants.stream()
            .filter(
                participant -> !participant.getParticipantId().equals(principal.getParticipantId()))
            .findFirst()
            .orElseThrow(() -> notFound());
    boolean alreadyRequested =
        connectionRepository
                .findByWorkspaceIdAndSenderIdAndReceiverId(
                    principal.getWorkspaceId(),
                    principal.getParticipantId(),
                    receiver.getParticipantId())
                .isPresent()
            || connectionRepository
                .findByWorkspaceIdAndSenderIdAndReceiverId(
                    principal.getWorkspaceId(),
                    receiver.getParticipantId(),
                    principal.getParticipantId())
                .isPresent();
    if (alreadyRequested) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A connection already exists.");
    }
    GuestConnection connection = new GuestConnection();
    connection.setWorkspaceId(principal.getWorkspaceId());
    connection.setSenderId(principal.getParticipantId());
    connection.setReceiverId(receiver.getParticipantId());
    connection.setStatus("pending");
    connection.setCreatedAt(Instant.now());
    connectionRepository.save(connection);
  }

  @Transactional
  public void respondToConnection(GuestPrincipal principal, long connectionId, boolean accept) {
    requireActiveWorkspace(principal.getWorkspaceId());
    GuestConnection connection =
        connectionRepository
            .findByConnectionIdAndWorkspaceId(connectionId, principal.getWorkspaceId())
            .orElseThrow(() -> notFound());
    if (!connection.getReceiverId().equals(principal.getParticipantId())
        || !"pending".equals(connection.getStatus())) {
      throw notFound();
    }
    connection.setStatus(accept ? "approved" : "rejected");
  }

  @Transactional
  public void createCollection(GuestPrincipal principal, String title) {
    String safeTitle = requiredText(title, 40, "Collection title");
    requireActiveWorkspace(principal.getWorkspaceId());
    GuestTodoCollection collection = new GuestTodoCollection();
    collection.setWorkspaceId(principal.getWorkspaceId());
    collection.setCreatedBy(principal.getParticipantId());
    collection.setTitle(safeTitle);
    collection.setCreatedAt(Instant.now());
    collectionRepository.save(collection);
  }

  @Transactional
  public void addTodo(GuestPrincipal principal, long collectionId, String title) {
    String safeTitle = requiredText(title, 200, "Todo item");
    requireActiveWorkspace(principal.getWorkspaceId());
    collectionRepository
        .findByCollectionIdAndWorkspaceId(collectionId, principal.getWorkspaceId())
        .orElseThrow(() -> notFound());
    GuestTodoItem todo = new GuestTodoItem();
    todo.setWorkspaceId(principal.getWorkspaceId());
    todo.setCollectionId(collectionId);
    todo.setTitle(safeTitle);
    todo.setCompleted(false);
    todoRepository.save(todo);
  }

  @Transactional
  public void toggleTodo(GuestPrincipal principal, long itemId) {
    requireActiveWorkspace(principal.getWorkspaceId());
    GuestTodoItem todo =
        todoRepository
            .findByItemIdAndWorkspaceId(itemId, principal.getWorkspaceId())
            .orElseThrow(() -> notFound());
    todo.setCompleted(!todo.isCompleted());
  }

  @Transactional(readOnly = true)
  public GuestParticipant getParticipant(GuestPrincipal principal) {
    requireActiveWorkspace(principal.getWorkspaceId());
    return participantRepository
        .findById(principal.getParticipantId())
        .filter(participant -> principal.getWorkspaceId().equals(participant.getWorkspaceId()))
        .orElseThrow(() -> notFound());
  }

  @Transactional(readOnly = true)
  public GuestParticipant getParticipant(GuestPrincipal principal, String participantId) {
    requireActiveWorkspace(principal.getWorkspaceId());
    return participantRepository
        .findById(participantId)
        .filter(participant -> principal.getWorkspaceId().equals(participant.getWorkspaceId()))
        .orElseThrow(() -> notFound());
  }

  private GuestWorkspace requireActiveWorkspace(String workspaceId) {
    GuestWorkspace workspace =
        workspaceRepository.findById(workspaceId).orElseThrow(() -> notFound());
    if (!workspace.getExpiresAt().isAfter(Instant.now())) {
      throw new ResponseStatusException(
          HttpStatus.UNAUTHORIZED, "This guest workspace has expired.");
    }
    return workspace;
  }

  private GuestParticipant newParticipant(GuestWorkspace workspace, String displayName) {
    GuestParticipant participant = new GuestParticipant();
    participant.setParticipantId(UUID.randomUUID().toString());
    participant.setWorkspaceId(workspace.getWorkspaceId());
    participant.setDisplayName(displayName);
    participant.setCreatedAt(Instant.now());
    return participantRepository.save(participant);
  }

  private String validateAssignee(String workspaceId, String assigneeId) {
    if (assigneeId == null || assigneeId.trim().isEmpty()) {
      return null;
    }
    return participantRepository
        .findById(assigneeId)
        .filter(participant -> workspaceId.equals(participant.getWorkspaceId()))
        .map(GuestParticipant::getParticipantId)
        .orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Tasks can only be assigned to a guest in this workspace."));
  }

  private String requiredText(String value, int maxLength, String label) {
    String trimmed = value == null ? "" : value.trim();
    if (trimmed.isEmpty() || trimmed.length() > maxLength) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, label + " must be between 1 and " + maxLength + " characters.");
    }
    return trimmed;
  }

  private String optionalText(String value, int maxLength) {
    String trimmed = value == null ? "" : value.trim();
    if (trimmed.length() > maxLength) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Description must be " + maxLength + " characters or fewer.");
    }
    return trimmed;
  }

  private ResponseStatusException notFound() {
    return new ResponseStatusException(HttpStatus.NOT_FOUND);
  }

  private String generateCode() {
    byte[] bytes = new byte[12];
    RANDOM.nextBytes(bytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }

  public static final class GuestEntry {
    private final String inviteCode;
    private final String accessToken;
    private final GuestWorkspace workspace;
    private final GuestParticipant participant;

    private GuestEntry(
        String inviteCode,
        String accessToken,
        GuestWorkspace workspace,
        GuestParticipant participant) {
      this.inviteCode = inviteCode;
      this.accessToken = accessToken;
      this.workspace = workspace;
      this.participant = participant;
    }

    public String getInviteCode() {
      return inviteCode;
    }

    public String getAccessToken() {
      return accessToken;
    }

    public GuestWorkspace getWorkspace() {
      return workspace;
    }

    public GuestParticipant getParticipant() {
      return participant;
    }
  }

  public static final class GuestDashboard {
    private final GuestWorkspace workspace;
    private final List<GuestParticipant> participants;
    private final List<GuestProject> projects;
    private final List<GuestTask> tasks;
    private final List<GuestConnection> connections;
    private final List<GuestTodoCollection> collections;
    private final List<GuestTodoItem> todoItems;

    private GuestDashboard(
        GuestWorkspace workspace,
        List<GuestParticipant> participants,
        List<GuestProject> projects,
        List<GuestTask> tasks,
        List<GuestConnection> connections,
        List<GuestTodoCollection> collections,
        List<GuestTodoItem> todoItems) {
      this.workspace = workspace;
      this.participants = participants;
      this.projects = projects;
      this.tasks = tasks;
      this.connections = connections;
      this.collections = collections;
      this.todoItems = todoItems;
    }

    public GuestWorkspace getWorkspace() {
      return workspace;
    }

    public List<GuestParticipant> getParticipants() {
      return participants;
    }

    public List<GuestProject> getProjects() {
      return projects;
    }

    public List<GuestTask> getTasks() {
      return tasks;
    }

    public List<GuestConnection> getConnections() {
      return connections;
    }

    public List<GuestTodoCollection> getCollections() {
      return collections;
    }

    public List<GuestTodoItem> getTodoItems() {
      return todoItems;
    }
  }
}
