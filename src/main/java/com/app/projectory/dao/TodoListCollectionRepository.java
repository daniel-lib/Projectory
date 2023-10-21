package com.app.projectory.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.projectory.entity.Project;
import com.app.projectory.entity.TodoListCollection;

@Repository
public interface TodoListCollectionRepository extends CrudRepository<TodoListCollection, Long> {
	@Query(nativeQuery = true, value ="SELECT * FROM todo_list_collection WHERE user_id = ?1")
	List<TodoListCollection> findTodoCollectionByUserId(long userId);
	
	
	
	
	
}
