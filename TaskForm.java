package com.example.todo.model;

import java.time.LocalDate;

//フォームから送られてくるデータを受け取るためのクラス
public class TaskForm {
	private String title;
	private String description;
	private LocalDate dueDate;
	private Priority priority;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	public Priority getPriority() {
		return priority;
	}
	public void setPriority(Priority priority) {
		this.priority = priority;
	}



}
