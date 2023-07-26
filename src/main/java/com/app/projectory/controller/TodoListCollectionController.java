package com.app.projectory.controller;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.projectory.doa.TodoListCollectionRepository;
import com.app.projectory.entity.TodoListCollection;

@Controller
@RequestMapping("todo-list-collection")
public class TodoListCollectionController {
	
	@Autowired
	TodoListCollectionRepository collectionDao;
	
	@GetMapping("/add-collection")
	@ResponseBody
	public int addTodoListCollection(@RequestParam String title) {
		Date date = new Date();
		Calendar d = Calendar.getInstance();
		if(!title.equals("") && title.length() <= 40) {
			TodoListCollection collection = new TodoListCollection();
			collection.setCollectionTitle(title);
			String currentToday = d.get(Calendar.DATE)+"/"+d.get(Calendar.MONTH+1)+"/"+d.get(Calendar.YEAR);
			collection.setCreationDate(currentToday);
			collection.setModificationDate(currentToday);
			collectionDao.save(collection);
			return 1;
		}
		return -1;
	}
}
