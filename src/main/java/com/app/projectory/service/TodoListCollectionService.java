package com.app.projectory.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.projectory.dao.TodoListCollectionRepository;
import com.app.projectory.dao.TodoListRepository;
import com.app.projectory.entity.Todo;
import com.app.projectory.entity.TodoListCollection;

@Service
public class TodoListCollectionService {

	
	@Autowired
	TodoListCollectionRepository collectionRepo;
	@Autowired
	TodoListRepository todoRepo;
	
	
	public List<TodoListCollection> getTodoCollectionForCurrentUser(long userId) {
		List<TodoListCollection> fetchedCollection = collectionRepo.findTodoCollectionByUserId(userId);
			return fetchedCollection;
	}
	
	public List<Todo> getCurrentUserTodoListByCollection(long collectionId) {
		List<Todo> fetchedTodoList = todoRepo.getTodoListByCollection(collectionId);
			return fetchedTodoList;
	}

}
