package com.app.projectory.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.app.projectory.dao.ProjectRepository;
import com.app.projectory.dao.TodoListCollectionRepository;
import com.app.projectory.entity.TodoListCollection;

@Service
public class TodoListCollectionService {
	
	@Autowired
	ProjectRepository projDao;
	@Autowired
	TodoListCollectionRepository collectionRepo;
	@Autowired
	private userAccountService userServ;
	
	
	public List<TodoListCollection> getTodoCollectionForCurrentUser(Authentication auth) {
		List<TodoListCollection> fetchedCollection = collectionRepo.findTodoCollectionByUserId(userServ.getUserId(auth));
			return fetchedCollection;
	}

}
