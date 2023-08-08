package com.app.projectory.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.app.projectory.entity.TodoListCollection;


public interface TodoListCollectionRepository extends CrudRepository<TodoListCollection, Long> {
	
	List<TodoListCollection> findAll();
	
}
