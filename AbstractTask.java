package com.example.todo.model;

import java.time.LocalDate;

public abstract class AbstractTask implements ExecutableTask {
    protected int id;
    protected String title;
    protected String description; // 💡 修正1: descriptionフィールドを追加
    protected LocalDate dueDate;
    protected TaskStatus status;
    protected Priority priority;

    // 💡 修正2: 6つの引数を受け取るコンストラクタに拡張
    public AbstractTask(int id,
                        String title,
                        String description, // 引数に追加
                        LocalDate dueDate,
                        Priority priority,
                        TaskStatus status) { // 引数に追加

        this.id = id;
        this.title = title;
        this.description = description; // descriptionをセット
        this.dueDate = dueDate;
        this.priority = (priority != null) ? priority : Priority.MEDIUM;
        this.status = status; // statusをセット
    }

    // --- Getter ---
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; } // 💡 修正3: descriptionのGetterを追加
    public LocalDate getDueDate() { return dueDate; }
    public TaskStatus getStatus() { return status; }
    public Priority getPriority() { return priority; }

    // --- Setter/編集メソッド ---
    public void markAsDone() { this.status = TaskStatus.DONE; }
    public void markAsTodo() { this.status = TaskStatus.TODO; }

    public void editTitle(String newTitle) { this.title = newTitle; }
    public void editDueDate(LocalDate newDate) { this.dueDate = newDate; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void editDescription(String newDescription) { this.description = newDescription; } // 💡 修正4: descriptionのSetterを追加

    // --- 抽象メソッド（インターフェースで強制されている） ---
    @Override
    public abstract void execute();

    @Override
    public String toString() {
        return title + " (期限:" + dueDate + ", 状態:" + status + ", 優先度:" + priority + ", 詳細:" + description + ")";
    }
}