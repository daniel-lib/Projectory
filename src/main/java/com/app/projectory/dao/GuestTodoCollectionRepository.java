package com.app.projectory.dao;

import com.app.projectory.entity.GuestTodoCollection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestTodoCollectionRepository extends JpaRepository<GuestTodoCollection, Long> {
  List<GuestTodoCollection> findAllByWorkspaceIdOrderByCreatedAtAsc(String workspaceId);

  Optional<GuestTodoCollection> findByCollectionIdAndWorkspaceId(
      long collectionId, String workspaceId);

  void deleteAllByWorkspaceId(String workspaceId);
}
