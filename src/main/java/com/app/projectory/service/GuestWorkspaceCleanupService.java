package com.app.projectory.service;

import com.app.projectory.dao.GuestConnectionRepository;
import com.app.projectory.dao.GuestParticipantRepository;
import com.app.projectory.dao.GuestProjectRepository;
import com.app.projectory.dao.GuestTaskRepository;
import com.app.projectory.dao.GuestTodoCollectionRepository;
import com.app.projectory.dao.GuestTodoItemRepository;
import com.app.projectory.dao.GuestWorkspaceRepository;
import com.app.projectory.entity.GuestWorkspace;
import java.time.Instant;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuestWorkspaceCleanupService {
  private final GuestWorkspaceRepository workspaceRepository;
  private final GuestParticipantRepository participantRepository;
  private final GuestProjectRepository projectRepository;
  private final GuestTaskRepository taskRepository;
  private final GuestConnectionRepository connectionRepository;
  private final GuestTodoCollectionRepository collectionRepository;
  private final GuestTodoItemRepository todoRepository;

  public GuestWorkspaceCleanupService(
      GuestWorkspaceRepository workspaceRepository,
      GuestParticipantRepository participantRepository,
      GuestProjectRepository projectRepository,
      GuestTaskRepository taskRepository,
      GuestConnectionRepository connectionRepository,
      GuestTodoCollectionRepository collectionRepository,
      GuestTodoItemRepository todoRepository) {
    this.workspaceRepository = workspaceRepository;
    this.participantRepository = participantRepository;
    this.projectRepository = projectRepository;
    this.taskRepository = taskRepository;
    this.connectionRepository = connectionRepository;
    this.collectionRepository = collectionRepository;
    this.todoRepository = todoRepository;
  }

  @Scheduled(fixedDelay = 600000)
  @Transactional
  public void deleteExpiredWorkspaces() {
    List<GuestWorkspace> expired =
        workspaceRepository.findAllByExpiresAtLessThanEqual(Instant.now());
    for (GuestWorkspace workspace : expired) {
      String workspaceId = workspace.getWorkspaceId();
      todoRepository.deleteAllByWorkspaceId(workspaceId);
      collectionRepository.deleteAllByWorkspaceId(workspaceId);
      connectionRepository.deleteAllByWorkspaceId(workspaceId);
      taskRepository.deleteAllByWorkspaceId(workspaceId);
      projectRepository.deleteAllByWorkspaceId(workspaceId);
      participantRepository.deleteAllByWorkspaceId(workspaceId);
      workspaceRepository.delete(workspace);
    }
  }
}
