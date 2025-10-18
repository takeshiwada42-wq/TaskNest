package com.example.todo;

import com.example.todo.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class TaskController {

    private final Category cat;
    // 💡 修正: タスクIDカウンターを定義
    private int taskIdCounter = 4;

    public TaskController() {
        this.cat = new Category(1, "勉強");

        // サンプルタスク
        // 💡 修正: 6つの引数 (id, title, description, dueDate, priority, status) に修正
        AbstractTask t1 = new NormalTask(1, "Java Gold 勉強", "コンテスト形式で過去問を解く", LocalDate.now().plusDays(5), Priority.HIGH, TaskStatus.TODO);

        // RecurringTask のコンストラクタが (id, title, description, dueDate, priority, period, status) の順であると仮定
        AbstractTask t2 = new RecurringTask(2, "筋トレ", "上半身の日（2日ごと）", LocalDate.now().minusDays(1), Priority.MEDIUM, 2, TaskStatus.TODO);

        AbstractTask t3 = new NormalTask(3, "掃除", "風呂とトイレを念入りに", LocalDate.now().plusDays(10), Priority.LOW, TaskStatus.DONE);
        AbstractTask t4 = new NormalTask(4, "読書", "技術書を読む", LocalDate.now().plusDays(3), Priority.MEDIUM, TaskStatus.TODO);

        cat.addTask(t1);
        cat.addTask(t2);
        cat.addTask(t3);
        cat.addTask(t4);
    }

    // 初期表示（全件を期限→優先度順で表示）
    @GetMapping("/tasks")
    public String index(Model model) {
        List<AbstractTask> tasks = cat.searchSortAndFilterByStatus("", "dueDateThenPriority", null);
        model.addAttribute("tasks", tasks);
        model.addAttribute("keyword", "");
        model.addAttribute("sortKey", "dueDateThenPriority");
        model.addAttribute("status", "");
        return "index";
    }

    // 検索＋ソート＋ステータス絞り込み
    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "dueDateThenPriority") String sortKey,
            @RequestParam(required = false) String status,
            Model model) {

        TaskStatus filterStatus = null;
        if ("TODO".equalsIgnoreCase(status)) filterStatus = TaskStatus.TODO;
        if ("DONE".equalsIgnoreCase(status)) filterStatus = TaskStatus.DONE;

        List<AbstractTask> tasks = cat.searchSortAndFilterByStatus(keyword, sortKey, filterStatus);

        model.addAttribute("tasks", tasks);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortKey", sortKey);
        model.addAttribute("status", status);

        return "index";
    }

    // 💡 新規タスク作成機能
    @PostMapping("/create")
    public String createTask(@ModelAttribute TaskForm taskForm) {
        // 1.新しいIDを生成
        int newTaskId = ++taskIdCounter;

        // 2.タスクオブジェクトを生成 (6つの引数)
        NormalTask newTask = new NormalTask(
                newTaskId,
                taskForm.getTitle(),
                taskForm.getDescription(),
                taskForm.getDueDate(),
                taskForm.getPriority(),
                TaskStatus.TODO // 新規作成時は必ずTODO
        );
        // 3.Categoryに追加
        cat.addTask(newTask);

        // 4.リスト画面にリダイレクト
        return "redirect:/";
    }
}