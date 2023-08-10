package com.app.projectory.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.projectory.entity.TodoListCollection;

@Repository
public interface TodoListCollectionRepository extends CrudRepository<TodoListCollection, Long> {
	
	List<TodoListCollection> findAll();
	
}
