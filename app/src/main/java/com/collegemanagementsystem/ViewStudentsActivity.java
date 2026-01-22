package com.collegemanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ViewStudentsActivity extends AppCompatActivity {

    ListView listView;
    DBHelper db;
    ArrayList<Student> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);

        listView = findViewById(R.id.listStudents);
        db = new DBHelper(this);

        list = db.getAllStudents();
        StudentAdapter adapter = new StudentAdapter(this, list);
        listView.setAdapter(adapter);

        // 🔴 THIS IS THE FIX (NO UI CHANGE)
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Student s = list.get(position);

            Intent i = new Intent(ViewStudentsActivity.this, StudentDetailActivity.class);
            i.putExtra("student_id", s.id);   // ✅ REQUIRED
            i.putExtra("name", s.name);
            i.putExtra("course", s.course);
            startActivity(i);
        });
    }
}
