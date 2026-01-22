package com.collegemanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnAdd, btnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAddStudent);
        btnView = findViewById(R.id.btnViewStudents);

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddStudentActivity.class)));

        btnView.setOnClickListener(v ->
                startActivity(new Intent(this, ViewStudentsActivity.class)));
    }
}
