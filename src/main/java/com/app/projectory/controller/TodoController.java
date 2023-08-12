package com.app.projectory.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.projectory.dao.TodoListCollectionRepository;
import com.app.projectory.dao.TodoListRepository;
import com.app.projectory.entity.Todo;
import com.app.projectory.entity.TodoListCollection;

@Controller
@RequestMapping("/todo")
public class TodoController {
	@Autowired
	TodoListRepository todoData;	
	@Autowired
	TodoListCollectionRepository collectionDao;
	
	@GetMapping
	public String displayTodoListPage(Model model, Todo todo) {
		List<Todo> fetchedItems = todoData.findAll();
		List<Long> item = new ArrayList<>();
		model.addAttribute("items", fetchedItems);
		model.addAttribute("itemCount", todoData.count());
		model.addAttribute("ItemsForDeletion", item);
		return "/user/todo/display-todo-list";
	}
	@GetMapping("add-item-form")
	public String displayAddTodoItemForm(Model todoModel, Todo todo) {
		todoModel.addAttribute("todoModel", todo);
		return "/user/todo/add-todo-list-form";
	}
	@PostMapping("/add-item")
	public String addTodoItem(Todo todo) {
		todoData.save(todo);
		return "redirect:/todo/add-item-form?item=added";
	}
	
	@GetMapping("add-item-js")
	@ResponseBody
	public int addTodoUsingJs(@RequestParam String title, @RequestParam long collectionId) {
		Todo todo = new Todo();
		try {
			
			Optional<TodoListCollection> collection = collectionDao.findById(collectionId);
			collection.ifPresent(value -> todo.setCollection(value));
			
			todo.setTitle(title);
			todoData.save(todo);
		}
		catch(Exception er) {
			return -1;
		}
		return 1;
		
	}
	
	@GetMapping("displayTodoList")
	public String displayTodoList(Model model) {
		List<Todo> fetchedItems = todoData.findAll();
		model.addAttribute("items", fetchedItems);
		return "/user/display-todo-list";
	}
	@GetMapping("/delete-item")
	public String deleteTodoItem(@RequestParam long itemId) {
		todoData.deleteById(itemId);
		
		return "redirect:/todo?item=deleted";
	}
	@GetMapping("/update-item")
	public String updateTodoItem(@RequestParam long itemId) {
		todoData.deleteById(itemId);
		
		return "redirect:/todo?item=updated";
	}
	@PostMapping("/delete-selected")
	public String deleteSelectedItems(@RequestParam("selectedForRemoval") Long[] selectedValues) {
		String result = Arrays.toString(selectedValues);
		for(int i=0; i < selectedValues.length; i++ ) {
			todoData.deleteById(selectedValues[i]);
		}
		return "redirect:/todo?item=multiple-deleted";
	}
}
