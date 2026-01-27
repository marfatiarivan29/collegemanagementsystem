package com.collegemanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    LinearLayout cardAddStudent, cardViewStudents, cardNotice, cardLogout, cardTodo;
    ImageView imgAdminProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Cards
        cardAddStudent = findViewById(R.id.cardAddStudent);
        cardViewStudents = findViewById(R.id.cardViewStudents);
        cardNotice = findViewById(R.id.cardNotice);
        cardLogout = findViewById(R.id.cardLogout);
        cardTodo = findViewById(R.id.cardTodo);

        // Profile Icon
        imgAdminProfile = findViewById(R.id.imgAdminProfile);

        // ADD STUDENT
        cardAddStudent.setOnClickListener(v ->
                startActivity(new Intent(this, AddStudentActivity.class)));

        // VIEW STUDENTS
        cardViewStudents.setOnClickListener(v ->
                startActivity(new Intent(this, ViewStudentsActivity.class)));

        // NOTICE
        cardNotice.setOnClickListener(v ->
                startActivity(new Intent(this, NoticeActivity.class)));

        // LOGOUT
        cardLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // TODO LIST
        cardTodo.setOnClickListener(v ->
                startActivity(new Intent(this, TodoActivity.class)));

        // ADMIN PROFILE CLICK
        imgAdminProfile.setOnClickListener(v ->
                startActivity(new Intent(this, AboutActivity.class)));
    }
}
