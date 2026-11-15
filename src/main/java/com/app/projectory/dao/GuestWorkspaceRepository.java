package com.app.projectory.dao;

import com.app.projectory.entity.GuestWorkspace;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestWorkspaceRepository extends JpaRepository<GuestWorkspace, String> {
  List<GuestWorkspace> findAllByExpiresAtLessThanEqual(Instant time);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query(
      "select workspace from GuestWorkspace workspace where workspace.workspaceId = :workspaceId")
  Optional<GuestWorkspace> findByIdForUpdate(@Param("workspaceId") String workspaceId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query(
      "select workspace from GuestWorkspace workspace where workspace.inviteCodeHash = :inviteCodeHash")
  Optional<GuestWorkspace> findByInviteCodeHashForUpdate(
      @Param("inviteCodeHash") String inviteCodeHash);
}
