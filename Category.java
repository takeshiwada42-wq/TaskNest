package com.example.todo.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.Comparator;
import java.util.Objects;
import java.lang.reflect.Method; // 💡 リフレクション機能に必要

import com.example.todo.exception.TaskNotFoundException;

public class Category {
    private int id;
    private String name;
    private List<AbstractTask> tasks;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    public void addTask(AbstractTask task) {
        tasks.add(task);
    }

    public List<AbstractTask> getTasks() {
        return new ArrayList<>(tasks);
    }

    public long getDaysUntilDue(int taskId) throws TaskNotFoundException {
        AbstractTask task = findTaskById(taskId);
        if (task.getDueDate() == null) {
            throw new IllegalStateException("期限が設定されていません");
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), task.getDueDate());
    }

    public AbstractTask findTaskById(int taskId) throws TaskNotFoundException {
        return tasks.stream()
                .filter(t -> t.getId() == taskId)
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    public static final Function<AbstractTask, Long> DAYS_UNTIL_DUE = task -> {
        if (task.getDueDate() == null) {
            throw new IllegalStateException("期限が設定されていません");
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), task.getDueDate());
    };

    public List<AbstractTask> getTasksSortedByDueDateThenPriority() {
        return tasks.stream()
            .sorted(
                // null安全な比較 (nullの期限を最後に)
                Comparator.comparing(AbstractTask::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()))
                          // null安全な比較 (nullの優先度を最後にし、降順に反転)
                          .thenComparing(Comparator.comparing(AbstractTask::getPriority, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
            )
            .toList();
    }

    // 🔹 検索 + 並び替え + ステータス絞り込み（一本化）
    public List<AbstractTask> searchSortAndFilterByStatus(String keyword, String sortKey, TaskStatus status) {
        // keywordがnullまたは空文字でも安全に処理
        String lowerKeyword = (keyword == null || keyword.isBlank()) ? "" : keyword.toLowerCase();

        // 検索とフィルタリング
        List<AbstractTask> filteredTasks = tasks.stream()
            // --- 1. 検索（部分一致 or 全件）
            .filter(t -> {
                if (lowerKeyword.isEmpty()) return true;

                // Objects.toString()を使用して、null安全に検索を行う
                String title = Objects.toString(t.getTitle(), "").toLowerCase();
                String priorityName = (t.getPriority() != null) ? t.getPriority().name().toLowerCase() : "";
                String dueDateStr = Objects.toString(t.getDueDate(), "");
                String statusName = (t.getStatus() != null) ? t.getStatus().name().toLowerCase() : "";

                return title.contains(lowerKeyword)
                        || priorityName.contains(lowerKeyword)
                        || dueDateStr.contains(lowerKeyword)
                        || statusName.contains(lowerKeyword);
            })
            // --- 2. ステータス絞り込み（nullなら全件対象）
            .filter(t -> status == null || t.getStatus() == status)
            .toList();


        // --- 3. 並び替え（Java Gold: リフレクションを使った動的なソート）
        // 💡 switch式（Java 14以降）でComparatorを生成
        Comparator<AbstractTask> comparator = switch (sortKey) {
            case "dueDateThenPriority" ->
                Comparator.comparing(AbstractTask::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()))
                          .thenComparing(Comparator.comparing(AbstractTask::getPriority, Comparator.nullsLast(Comparator.naturalOrder())).reversed());

            case "dueDate", "priority", "title", "id" -> // リフレクションで処理するフィールド
                getDynamicComparator(sortKey);

            default ->
                (a, b) -> 0; // ソートなし
        };

        // ソートを実行して結果を返す
        filteredTasks.sort(comparator);
        return filteredTasks;
    }

    // Java Gold範囲: リフレクションを使って任意のフィールドでソートする Comparator を生成
    private Comparator<AbstractTask> getDynamicComparator(String sortKey){
    	//比較対象のメソッド名を作成 (例: "title" -> "getTitle")
    	String methodName = "get" + sortKey.substring(0,1).toUpperCase() + sortKey.substring(1);

    	//比較関数を返す (ラムダ式)
    	return(t1, t2) -> {
    		try {
    		// 1. リフレクションを使ってメソッドを取得
    		Method method = AbstractTask.class.getMethod(methodName);

    		// 2. メソッドを呼び出し、Comparableな値を取得
    		Comparable v1 = (Comparable) method.invoke(t1);
    		Comparable v2 = (Comparable) method.invoke(t2);

    		// 3. null安全に比較
    		if(v1 == null && v2 == null) return 0;
    		if(v1 == null) return 1;  // nullは最後 (ソート順で後ろ)
    		if(v2 == null) return -1; // nullは最後 (ソート順で後ろ)

    		// 4. 値を比較
    		return v1.compareTo(v2);
    		} catch (Exception e) {
    			// リフレクションに失敗した場合は実行時例外をスロー
    			throw new RuntimeException("Invalid sort key or reflection error:" + sortKey, e);
    		}
    	};
    }
}