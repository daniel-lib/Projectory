package com.app.projectory.dto;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


public class TodoDTO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private long todoItemId;
	
	private String title;
	
	public TodoDTO() {
		
	}
	public TodoDTO(String title) {
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
	
	@Override
	public String toString() {
		return "TodoDto [todoItemId=" + todoItemId + ", title=" + title + "]";
	}
	
	
	
}
