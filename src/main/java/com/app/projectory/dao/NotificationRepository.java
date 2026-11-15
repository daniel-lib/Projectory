package com.app.projectory.dao;

import com.app.projectory.entity.Notification;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> findTop25ByReceiver_UserIdOrderByCreatedAtDesc(long receiverUserId);

  long countByReceiver_UserIdAndSeenFalse(long receiverUserId);

  @Transactional
  @Modifying
  @Query(
      "UPDATE Notification n SET n.seen = true, n.seenAt = :seenAt, n.modifiedAt = :seenAt "
          + "WHERE n.receiver.userId = :receiverUserId AND n.seen = false")
  int markAllSeen(
      @Param("receiverUserId") long receiverUserId, @Param("seenAt") LocalDateTime seenAt);
}
