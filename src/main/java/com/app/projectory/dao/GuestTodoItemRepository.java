package com.app.projectory.dao;

import com.app.projectory.entity.GuestTodoItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestTodoItemRepository extends JpaRepository<GuestTodoItem, Long> {
  List<GuestTodoItem> findAllByWorkspaceIdOrderByItemIdAsc(String workspaceId);

  Optional<GuestTodoItem> findByItemIdAndWorkspaceId(long itemId, String workspaceId);

  void deleteAllByWorkspaceId(String workspaceId);
}
