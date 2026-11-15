package com.app.projectory.dao;

import com.app.projectory.entity.GuestConnection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestConnectionRepository extends JpaRepository<GuestConnection, Long> {
  List<GuestConnection> findAllByWorkspaceIdOrderByCreatedAtAsc(String workspaceId);

  Optional<GuestConnection> findByWorkspaceIdAndSenderIdAndReceiverId(
      String workspaceId, String senderId, String receiverId);

  Optional<GuestConnection> findByConnectionIdAndWorkspaceId(long connectionId, String workspaceId);

  void deleteAllByWorkspaceId(String workspaceId);
}
