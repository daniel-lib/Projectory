package com.app.projectory.dao;

import com.app.projectory.entity.GuestTask;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestTaskRepository extends JpaRepository<GuestTask, Long> {
  List<GuestTask> findAllByWorkspaceIdOrderByProjectIdAscTaskIdAsc(String workspaceId);

  Optional<GuestTask> findByTaskIdAndWorkspaceId(long taskId, String workspaceId);

  void deleteAllByWorkspaceId(String workspaceId);
}
