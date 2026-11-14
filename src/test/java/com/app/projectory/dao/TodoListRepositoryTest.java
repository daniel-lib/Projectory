package com.app.projectory.dao;

import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.app.projectory.entity.Todo;
import com.app.projectory.entity.TodoListCollection;
import com.app.projectory.entity.Users;

@DataJpaTest(properties = {
		"spring.jpa.hibernate.ddl-auto=create-drop",
		"spring.sql.init.mode=never"
})
@AutoConfigureTestDatabase(replace = Replace.ANY)
class TodoListRepositoryTest {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private TodoListRepository todoRepository;

	@Test
	void completionStatePersistsSeparatelyFromDeletionSelection() {
		TodoListCollection collection = new TodoListCollection();
		collection.setCollectionTitle("Personal");
		entityManager.persist(collection);

		Todo todo = new Todo("Finish task");
		todo.setCollection(collection);
		entityManager.persist(todo);
		entityManager.flush();

		todo.setCompleted(true);
		todoRepository.save(todo);
		entityManager.flush();
		entityManager.clear();

		assertTrue(todoRepository.findById(todo.getTodoItemId()).orElseThrow().isCompleted());
	}

	@Test
	void deletingTodoAndCollectionDoesNotDeleteTheirOwner() {
		Users owner = new Users();
		owner.setUsername("todo-owner");
		owner.setPassword("password");
		entityManager.persist(owner);

		TodoListCollection collection = new TodoListCollection();
		collection.setCollectionTitle("Personal");
		collection.setCreator(owner);
		entityManager.persist(collection);

		Todo todo = new Todo("Finish task");
		todo.setCollection(collection);
		entityManager.persist(todo);
		entityManager.flush();

		todoRepository.delete(todo);
		entityManager.remove(collection);
		entityManager.flush();
		entityManager.clear();

		assertTrue(entityManager.find(Users.class, owner.getUserId()) != null);
		assertTrue(entityManager.find(TodoListCollection.class, collection.getTodoCollectionId()) == null);
	}
}
