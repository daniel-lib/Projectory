package com.app.projectory.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class TodoListCollection {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long todoCollectionId;
	private String collectionTitle;
	private String creationDate;
	private String modificationDate;
	
	@OneToMany(mappedBy = "collection")
	private List<Todo> todoListItems;

	public TodoListCollection() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TodoListCollection(String collectionTitle, String creationDate, String modificationDate,
			List<Todo> todoListItems) {
		this.collectionTitle = collectionTitle;
		this.creationDate = creationDate;
		this.modificationDate = modificationDate;
		this.todoListItems = todoListItems;
	}

	public Long getTodoCollectionId() {
		return todoCollectionId;
	}

	public void setTodoCollectionId(Long todoCollectionId) {
		this.todoCollectionId = todoCollectionId;
	}

	public String getCollectionTitle() {
		return collectionTitle;
	}

	public void setCollectionTitle(String collectionTitle) {
		this.collectionTitle = collectionTitle;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(String modificationDate) {
		this.modificationDate = modificationDate;
	}

	public List<Todo> getTodoListItems() {
		return todoListItems;
	}

	public void setTodoListItems(List<Todo> todoListItems) {
		this.todoListItems = todoListItems;
	}
	
	
}
