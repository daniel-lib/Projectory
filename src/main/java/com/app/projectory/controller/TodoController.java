package com.app.projectory.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.app.projectory.dao.TodoListCollectionRepository;
import com.app.projectory.dao.TodoListRepository;
import com.app.projectory.entity.Todo;
import com.app.projectory.entity.TodoListCollection;
import com.app.projectory.service.TodoListCollectionService;
import com.app.projectory.service.userAccountService;

@Controller
@RequestMapping("/todo")
public class TodoController {
	@Autowired
	TodoListRepository todoData;	
	@Autowired
	TodoListCollectionRepository collectionDao;
	@Autowired
	userAccountService userServ;
	@Autowired
	TodoListCollectionService collectionServ;
	
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
	
	@PostMapping("add-item-js")
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

	/** HTMX: add item and return the refreshed item list for that collection. */
	@PostMapping("fragment/add-item")
	public String addTodoItemFragment(@RequestParam String title, @RequestParam long collectionId,
			Model model, javax.servlet.http.HttpServletResponse response) {
		Optional<TodoListCollection> collection = collectionDao.findById(collectionId);
		boolean added = collection.isPresent() && !title.trim().isEmpty();
		if (added) {
			Todo todo = new Todo();
			TodoListCollection value = collection.get();
			todo.setCollection(value);
			todo.setTitle(title.trim());
			todoData.save(todo);
			response.setHeader("HX-Trigger",
					"{\"notify\":{\"type\":\"success\",\"message\":\"Todo item added\"}}");
		} else {
			response.setHeader("HX-Trigger",
					"{\"notify\":{\"type\":\"error\",\"message\":\"Todo item could not be added\"}}");
		}
		// Reload the collection so the item list is fresh
		Optional<TodoListCollection> refreshed = collectionDao.findById(collectionId);
		refreshed.ifPresent(c -> model.addAttribute("collection", c));
		return "user/todo/todoLayouts :: displayItems";
	}

	/** HTMX: delete a single item and return the refreshed item list for that collection. */
	@PostMapping("fragment/delete-item")
	public String deleteTodoItemFragment(@RequestParam long itemId, @RequestParam long collectionId, Model model) {
		todoData.deleteById(itemId);
		Optional<TodoListCollection> refreshed = collectionDao.findById(collectionId);
		refreshed.ifPresent(c -> model.addAttribute("collection", c));
		return "user/todo/todoLayouts :: displayItems";
	}

	/** HTMX: update completion state for one todo item. */
	@PostMapping("fragment/toggle-done")
	public String toggleTodoItemDone(@RequestParam long itemId,
			@RequestParam(defaultValue = "false") boolean done, Model model) {
		Todo todo = todoData.findById(itemId)
				.orElseThrow(() -> new IllegalArgumentException("Todo item does not exist"));
		todo.setCompleted(done);
		Todo updatedTodo = todoData.save(todo);
		model.addAttribute("item", updatedTodo);
		model.addAttribute("collection", todo.getCollection());
		return "user/todo/todoLayouts :: todo-item";
	}

	/** HTMX: update an item title and return its refreshed row. */
	@PostMapping("fragment/update-item")
	public String updateTodoItemFragment(@RequestParam long itemId, @RequestParam String title,
			Authentication auth, Model model, javax.servlet.http.HttpServletResponse response) {
		Todo todo = getTodoForCurrentUser(itemId, auth);
		String updatedTitle = title.trim();
		if (updatedTitle.isEmpty()) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			response.setHeader("HX-Trigger",
					"{\"notify\":{\"type\":\"error\",\"message\":\"Todo item title cannot be empty\"}}");
			return "user/todo/todoLayouts :: todo-item";
		}

		todo.setTitle(updatedTitle);
		Todo updatedTodo = todoData.save(todo);
		model.addAttribute("item", updatedTodo);
		model.addAttribute("collection", updatedTodo.getCollection());
		response.setHeader("HX-Trigger",
				"{\"notify\":{\"type\":\"success\",\"message\":\"Todo item updated\"}}");
		return "user/todo/todoLayouts :: todo-item";
	}

	/** Legacy redirect — kept for non-HTMX fallback. */
	@PostMapping("/delete-item")
	public String deleteTodoItem(@RequestParam long itemId) {
		todoData.deleteById(itemId);
		return "redirect:/todo?item=deleted";
	}

	@GetMapping("displayTodoList")
	public String displayTodoList(Model model) {
		List<Todo> fetchedItems = todoData.findAll();
		model.addAttribute("items", fetchedItems);
		return "/user/display-todo-list";
	}

	@PostMapping("/update-item")
	public String updateTodoItem(@RequestParam long itemId, @RequestParam String title, Authentication auth) {
		Todo todo = getTodoForCurrentUser(itemId, auth);
		String updatedTitle = title.trim();
		if (updatedTitle.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todo item title cannot be empty");
		}
		todo.setTitle(updatedTitle);
		todoData.save(todo);
		return "redirect:/todo?item=updated";
	}

	/** Legacy bulk-delete — kept for non-HTMX form submit fallback. */
	@PostMapping("/delete-selected")
	public String deleteSelectedItems(@RequestParam("selectedForRemoval") Long[] selectedValues) {
		for(int i=0; i < selectedValues.length; i++ ) {
			todoData.deleteById(selectedValues[i]);
		}
		return "redirect:/todo?item=multiple-deleted";
	}

	/** HTMX: bulk-delete and return the refreshed collection list. */
	@PostMapping("fragment/delete-selected")
	public String deleteSelectedItemsFragment(
			@RequestParam(value = "selectedForRemoval", required = false) Long[] selectedValues,
			Authentication auth, Model model) {
		if (selectedValues != null) {
			for (Long id : selectedValues) {
				todoData.deleteById(id);
			}
		}
		long userId = userServ.getUserId(auth);
		model.addAttribute("TodoCollections", collectionServ.getTodoCollectionForCurrentUser(userId));
		return "user/todo/todoLayouts :: todo-collection-list";
	}

	private Todo getTodoForCurrentUser(long itemId, Authentication auth) {
		Todo todo = todoData.findById(itemId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		if (todo.getCollection() == null || todo.getCollection().getCreator() == null
				|| todo.getCollection().getCreator().getUserId() != userServ.getUserId(auth)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return todo;
	}
}
