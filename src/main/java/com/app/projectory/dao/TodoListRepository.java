package com.app.projectory.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.projectory.entity.Todo;

@Repository
public interface TodoListRepository extends CrudRepository<Todo, Long> {
	
	List<Todo> findAll();
	
	//@Query(nativeQuery = true, value="SELECT * from todo WHERE collection_id = ?1")
	//List<Todo> getTodoListByCollection(long collectionId);
	
	@Query(nativeQuery = true, value="SELECT * from todo WHERE collection_id = ?1")
	List<Todo> getTodoListByCollection(long collectionId);
	
	//@Query(nativeQuery = true, value="SELECT title from todo WHERE collection_id = ?1")
	//List<String> getTodoListByCollection(long collectionId);
	
}
