package com.app.projectory.dao;

import com.app.projectory.entity.GuestParticipant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestParticipantRepository extends JpaRepository<GuestParticipant, String> {
  List<GuestParticipant> findAllByWorkspaceIdOrderByCreatedAtAsc(String workspaceId);

  long countByWorkspaceId(String workspaceId);

  void deleteAllByWorkspaceId(String workspaceId);
}
