package com.example.todo.exception;

public class TaskNotFoundException extends Exception {
	public TaskNotFoundException(String message) {
		super(message);

	}
	public TaskNotFoundException(int id) {
		super("Task with id " + id + "not found");
	}

}
