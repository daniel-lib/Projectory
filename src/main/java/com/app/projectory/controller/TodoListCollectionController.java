package com.app.projectory.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.app.projectory.dao.TodoListCollectionRepository;
import com.app.projectory.dao.TodoListRepository;
import com.app.projectory.dao.UsersRepository;
import com.app.projectory.entity.Todo;
import com.app.projectory.entity.TodoListCollection;
import com.app.projectory.service.TodoListCollectionService;
import com.app.projectory.service.userAccountService;

@Controller
@RequestMapping("todo-list-collection")
public class TodoListCollectionController {
	
	@Autowired
	TodoListCollectionRepository collectionDao;
	@Autowired
	TodoListRepository todoDao;
	@PersistenceContext
	EntityManager entityManager;
	@Autowired
	userAccountService userServ;
	@Autowired
	UsersRepository userRepo;
	@Autowired
	TodoListCollectionService collectionServ;
	
	@PostMapping("/add-collection")
	@ResponseBody
	public int addTodoListCollection(@RequestParam String title, Authentication auth,
			HttpServletResponse response) {
		if(!title.equals("") && title.length() <= 40) {
			LocalDateTime data = LocalDateTime.now(); 
			String authUsername = userServ.getCurrentUsername(auth);
			TodoListCollection collection = new TodoListCollection();
			collection.setCollectionTitle(title);
			collection.setCreator(userRepo.findByUsername(authUsername));
			collectionDao.save(collection);
			response.setHeader("HX-Trigger", "{\"todoCollectionsChanged\":null,\"notify\":{\"type\":\"success\",\"message\":\"New collection created\"}}");
			return 1;
		}
		response.setHeader("HX-Trigger", "{\"notify\":{\"type\":\"error\",\"message\":\"Collection title must be between 1 and 40 characters\"}}");
		return -1;
	}

	/** Returns the full collection list fragment for HTMX swaps. */
	@GetMapping("/fragment/list")
	public String todoCollectionListFragment(Authentication auth, Model model) {
		long userId = userServ.getUserId(auth);
		List<TodoListCollection> collections = collectionServ.getTodoCollectionForCurrentUser(userId);
		model.addAttribute("TodoCollections", collections);
		return "user/todo/todoLayouts :: todo-collection-list";
	}

	/** HTMX: rename a collection and return the refreshed collection list. */
	@PostMapping("/fragment/update-collection")
	public String updateTodoListCollection(@RequestParam long collectionId, @RequestParam String title,
			Authentication auth, Model model, HttpServletResponse response) {
		TodoListCollection collection = getOwnedCollection(collectionId, auth);
		String updatedTitle = title.trim();
		if (updatedTitle.isEmpty() || updatedTitle.length() > 40) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			response.setHeader("HX-Trigger",
					"{\"notify\":{\"type\":\"error\",\"message\":\"Collection name must be between 1 and 40 characters\"}}");
			return collectionListFragment(auth, model);
		}

		collection.setCollectionTitle(updatedTitle);
		collectionDao.save(collection);
		response.setHeader("HX-Trigger",
				"{\"notify\":{\"type\":\"success\",\"message\":\"Collection name updated\"}}");
		return collectionListFragment(auth, model);
	}

	/** HTMX: delete a collection and its todo items after user confirmation. */
	@Transactional
	@PostMapping("/fragment/delete-collection")
	public String deleteTodoListCollection(@RequestParam long collectionId, Authentication auth, Model model,
			HttpServletResponse response) {
		TodoListCollection collection = getOwnedCollection(collectionId, auth);
		List<Todo> items = todoDao.getTodoListByCollection(collectionId);
		todoDao.deleteAll(items);
		collectionDao.delete(collection);
		entityManager.flush();
		response.setHeader("HX-Trigger",
				"{\"notify\":{\"type\":\"success\",\"message\":\"Collection deleted\"}}");
		return collectionListFragment(auth, model);
	}

	private TodoListCollection getOwnedCollection(long collectionId, Authentication auth) {
		TodoListCollection collection = collectionDao.findById(collectionId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		if (collection.getCreator() == null
				|| collection.getCreator().getUserId() != userServ.getUserId(auth)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return collection;
	}

	private String collectionListFragment(Authentication auth, Model model) {
		long userId = userServ.getUserId(auth);
		List<TodoListCollection> collections = collectionServ.getTodoCollectionForCurrentUser(userId);
		model.addAttribute("TodoCollections", collections);
		return "user/todo/todoLayouts :: todo-collection-list";
	}
}
