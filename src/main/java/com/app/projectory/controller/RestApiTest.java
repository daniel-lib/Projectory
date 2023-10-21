
  package com.app.projectory.controller;
  
  import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import
  org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import
  org.springframework.web.bind.annotation.RequestMapping;
import
  org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.projectory.entity.Todo;
import com.app.projectory.service.TodoListCollectionService;
  
  @RestController  
  @RequestMapping("") 
  public class RestApiTest {
	  
	  @Autowired
	private TodoListCollectionService todoCollServ;
  
  @GetMapping("/api") public String returnData(@RequestParam String name) {
  return "Hi, "+name; 
  } 
  
  @GetMapping("/api/todo-list/{collectionId}")
	public List<Todo> fetchTodoList(Authentication auth, @PathVariable long collectionId ){
		List<Todo> fetchedTodoList = todoCollServ.getCurrentUserTodoListByCollection(collectionId);
		return fetchedTodoList;
	}
  
  
  }
 