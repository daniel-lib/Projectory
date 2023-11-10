package com.app.projectory.controller;

import java.time.LocalDateTime;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.projectory.dao.TodoListCollectionRepository;
import com.app.projectory.dao.UsersRepository;
import com.app.projectory.entity.TodoListCollection;
import com.app.projectory.service.userAccountService;

@Controller
@RequestMapping("todo-list-collection")
public class TodoListCollectionController {
	
	@Autowired
	TodoListCollectionRepository collectionDao;
	@Autowired
	userAccountService userServ;
	@Autowired
	UsersRepository userRepo;
	
	@GetMapping("/add-collection")
	@ResponseBody
	public int addTodoListCollection(@RequestParam String title, Authentication auth) {
//		Calendar d = Calendar.getInstance();
//		String currentToday = d.get(Calendar.DATE)+"/"+d.get(Calendar.MONTH+1)+"/"+d.get(Calendar.YEAR);
		
		if(!title.equals("") && title.length() <= 40) {
			LocalDateTime data = LocalDateTime.now(); 
			String authUsername = userServ.getCurrentUsername(auth);
			TodoListCollection collection = new TodoListCollection();
			collection.setCollectionTitle(title);
			collection.setCreator(userRepo.findByUsername(authUsername));
			collectionDao.save(collection);
			return 1;
		}
		return -1;
	}
}
