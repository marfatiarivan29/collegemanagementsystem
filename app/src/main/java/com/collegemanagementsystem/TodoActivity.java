package com.collegemanagementsystem;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TodoActivity extends AppCompatActivity {

    EditText edtTask;
    Button btnAdd;
    ListView listView;

    DBHelper db;

    ArrayList<String> taskList;
    ArrayList<Integer> idList;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        edtTask = findViewById(R.id.edtTask);
        btnAdd = findViewById(R.id.btnAddTask);
        listView = findViewById(R.id.listTodo);

        db = new DBHelper(this);

        loadTasks();

        btnAdd.setOnClickListener(v -> {

            String task = edtTask.getText().toString().trim();

            if (task.isEmpty()) {
                Toast.makeText(this, "Enter task", Toast.LENGTH_SHORT).show();
                return;
            }

            db.addTodo(task);
            edtTask.setText("");
            loadTasks();
        });

        // DELETE TASK ON LONG CLICK
        listView.setOnItemLongClickListener((parent, view, position, id) -> {

            int todoId = idList.get(position);

            db.deleteTodo(todoId);

            loadTasks();

            Toast.makeText(this, "Task Deleted", Toast.LENGTH_SHORT).show();

            return true;
        });
    }

    private void loadTasks() {

        taskList = new ArrayList<>();
        idList = new ArrayList<>();

        Cursor c = db.getTodos();

        if (c.moveToFirst()) {

            do {
                idList.add(c.getInt(0));     // ID
                taskList.add(c.getString(1)); // TASK TEXT

            } while (c.moveToNext());
        }

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                taskList);

        listView.setAdapter(adapter);
    }
}
