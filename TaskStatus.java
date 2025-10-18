package com.example.todo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//タスクの状態を表す
public enum TaskStatus {
	TODO("未完了"),
	DONE("完了");
	private final String label;

	TaskStatus(String label){
		this.label=label;
	}

	public String getLabel() {
		return label;
	}
}
