package com.app.projectory.doa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.app.projectory.entity.Todo;

public interface TodoListRepository extends CrudRepository<Todo, Long> {
	
	List<Todo> findAll();
	/* List<Long> deleteAllById(); */
	
}
