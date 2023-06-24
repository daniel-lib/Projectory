package com.app.projectory.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Todo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long todoItemId;
	private String title;
	private String detail;
	
	public Todo() {
		
	}
	public Todo(String title, String detail) {
		this.title = title;
		this.detail = detail;
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
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
}
