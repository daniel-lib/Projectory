package com.app.projectory.dao;

import com.app.projectory.entity.GuestProject;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestProjectRepository extends JpaRepository<GuestProject, Long> {
  List<GuestProject> findAllByWorkspaceIdOrderByCreatedAtDesc(String workspaceId);

  Optional<GuestProject> findByProjectIdAndWorkspaceId(long projectId, String workspaceId);

  void deleteAllByWorkspaceId(String workspaceId);
}
