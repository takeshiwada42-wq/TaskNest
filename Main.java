package com.example.todo;

import com.example.todo.model.*;
import com.example.todo.exception.TaskNotFoundException;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Category cat = new Category(1, "勉強");

        // 💡 修正: NormalTaskのコンストラクタを 6引数 (id, title, description, dueDate, priority, status) に合わせる

        // NormalTask(id, title, description, dueDate, priority, status)
        AbstractTask t1 = new NormalTask(1, "Java Gold 勉強", "コンテスト形式で過去問を解く", LocalDate.now().plusDays(5), Priority.HIGH, TaskStatus.TODO);

        // RecurringTask(id, title, description, dueDate, priority, recurrenceInterval, status) と仮定
        AbstractTask t2 = new RecurringTask(2, "筋トレ", "上半身の日（週2回）", LocalDate.now().minusDays(1), Priority.MEDIUM, 2, TaskStatus.TODO);

        AbstractTask t3 = new NormalTask(3, "掃除", "風呂とトイレを念入りに", LocalDate.now().plusDays(10), Priority.LOW, TaskStatus.DONE);
        AbstractTask t4 = new NormalTask(4, "読書", "技術書を読む", LocalDate.now().plusDays(5), Priority.MEDIUM, TaskStatus.TODO);
        AbstractTask t5 = new NormalTask(5, "料理", "週末の作り置きをする", LocalDate.now().plusDays(5), Priority.LOW, TaskStatus.DONE);

        // 💡 修正: コンストラクタで状態設定済みのため、以下のmarkAs*の呼び出しは不要となり削除
        /* t1.markAsTodo();
        t2.markAsTodo();
        t3.markAsDone();
        t4.markAsTodo();
        t5.markAsDone();
        */

        cat.addTask(t1);
        cat.addTask(t2);
        cat.addTask(t3);
        cat.addTask(t4);
        cat.addTask(t5);

        // --- 出力開始 ---
        System.out.println("=== タスク一覧アプリ ===");

        // 🔹 検索『勉』＋期限順＋TODOのみ
        printSection("検索『勉』＋期限順＋TODOのみ",
                cat.searchSortAndFilterByStatus("勉", "dueDate", TaskStatus.TODO));

        // 🔹 全件＋優先度順＋DONEのみ
        printSection("全件＋優先度順＋DONEのみ",
                cat.searchSortAndFilterByStatus("", "priority", TaskStatus.DONE));

        // 🔹 検索『筋』＋タイトル順＋TODOのみ
        printSection("検索『筋』＋タイトル順＋TODOのみ",
                cat.searchSortAndFilterByStatus("筋", "title", TaskStatus.TODO));

        // 🔹 全件＋期限⇒優先度順＋全ステータス
        printSection("全件＋期限⇒優先度順＋全ステータス",
                cat.searchSortAndFilterByStatus("", "dueDateThenPriority", null));
    }

    // --- 表示用ヘルパーメソッド ---
    private static void printSection(String title, List<AbstractTask> tasks) {
        System.out.println("\n--- " + title + " ---");
        if (tasks.isEmpty()) {
            System.out.println("（該当するタスクはありません）");
        } else {
            tasks.forEach(t -> System.out.printf(
                    "ID:%d | %s | 期限:%s | 状態:%s | 優先度:%s%n",
                    t.getId(),
                    t.getTitle(),
                    t.getDueDate(),
                    t.getStatus(),
                    t.getPriority()
            ));
        }
    }
}