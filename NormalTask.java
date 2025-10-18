package com.example.todo.model;

import java.time.LocalDate;

// AbstractTaskを継承
// 💡 修正: クラスの宣言を追加
public class NormalTask extends AbstractTask {

    // 💡 修正: コンストラクタに6つの引数を定義（あなたの修正内容）
    public NormalTask(int id,
                      String title,
                      String description,
                      LocalDate dueDate,
                      Priority priority,
                      TaskStatus status) {

        // 抽象クラスのコンストラクタを呼び出す
        // AbstractTaskのコンストラクタが6つの引数を受け取る必要があります
        super(id, title, description, dueDate, priority, status);
    }

    // AbstractTaskで定義された抽象メソッドを実装
    @Override
    public void execute() {
        // 何らかの実行処理
        // statusフィールドは AbstractTask で protected か public である必要があります。
        this.status = TaskStatus.DONE;
    }
}