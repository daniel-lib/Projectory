package com.app.projectory.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class TodoListCollection {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long todoCollectionId;
	private String collectionTitle;
	@CreationTimestamp
	private LocalDate creationDate;
	@UpdateTimestamp
	private LocalDate modificationDate;
	
	@OneToMany(mappedBy = "collection")
	private List<Todo> todoListItems;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private Users creator;

	public TodoListCollection() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TodoListCollection(String collectionTitle, LocalDate creationDate, LocalDate modificationDate) {
		this.collectionTitle = collectionTitle;
		this.creationDate = creationDate;
		this.modificationDate = modificationDate;
	}

	public long getTodoCollectionId() {
		return todoCollectionId;
	}

	public void setTodoCollectionId(long todoCollectionId) {
		this.todoCollectionId = todoCollectionId;
	}

	public String getCollectionTitle() {
		return collectionTitle;
	}

	public void setCollectionTitle(String collectionTitle) {
		this.collectionTitle = collectionTitle;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDate getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(LocalDate modificationDate) {
		this.modificationDate = modificationDate;
	}

	public List<Todo> getTodoListItems() {
		return todoListItems;
	}

	public void setTodoListItems(List<Todo> todoListItems) {
		this.todoListItems = todoListItems;
	}

	public Users getCreator() {
		return creator;
	}

	public void setCreator(Users creator) {
		this.creator = creator;
	}

	
	
	
	
}
