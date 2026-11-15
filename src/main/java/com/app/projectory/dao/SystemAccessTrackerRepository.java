package com.app.projectory.dao;

import com.app.projectory.entity.SystemAccessTracker;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemAccessTrackerRepository extends JpaRepository<SystemAccessTracker, Long> {
  Optional<SystemAccessTracker> findByActorIdentifier(String actorIdentifier);
}
