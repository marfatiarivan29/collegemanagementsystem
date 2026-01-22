package com.collegemanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StudentDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🔴 MAKE SURE THIS MATCHES YOUR EXISTING XML FILE NAME
        setContentView(R.layout.activity_student_detail);

        // Bind EXISTING views (NO UI CHANGE)
        TextView txtName = findViewById(R.id.txtName);
        TextView txtCourse = findViewById(R.id.txtCourse);
        Button btnFees = findViewById(R.id.btnFees);
        Button btnAttendance = findViewById(R.id.btnAttendance);

        int studentId = getIntent().getIntExtra("student_id", -1);
        String name = getIntent().getStringExtra("name");
        String course = getIntent().getStringExtra("course");

        if (studentId == -1) {
            Toast.makeText(this, "Invalid student", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        txtName.setText(name);
        txtCourse.setText(course);

        // ✅ FEES BUTTON WORKING
        btnFees.setOnClickListener(v -> {
            Intent i = new Intent(StudentDetailActivity.this, FeesActivity.class);
            i.putExtra("student_id", studentId);
            i.putExtra("course", course);
            startActivity(i);
        });

        // ✅ ATTENDANCE BUTTON WORKING
        btnAttendance.setOnClickListener(v -> {
            Intent i = new Intent(StudentDetailActivity.this, AttendanceActivity.class);
            i.putExtra("student_id", studentId);
            startActivity(i);
        });
    }
}
