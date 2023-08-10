package com.app.projectory.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.projectory.entity.Todo;

@Repository
public interface TodoListRepository extends CrudRepository<Todo, Long> {
	
	List<Todo> findAll();
	
}
