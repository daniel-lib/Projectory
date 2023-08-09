package com.app.projectory.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Todo implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long todoItemId;
	
	private String title;
	
	@ManyToOne(cascade= {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name="collection_id")
	private TodoListCollection collection;
	
	public Todo() {
		
	}
	public Todo(String title) {
		this.title = title;
	}
	public long getTodoItemId() {
		return todoItemId;
	}
	public void setTodoItemId(long todoItemId) {
		this.todoItemId = todoItemId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public TodoListCollection getCollection() {
		return collection;
	}
	public void setCollection(TodoListCollection collection) {
		this.collection = collection;
	}
	
	
}
