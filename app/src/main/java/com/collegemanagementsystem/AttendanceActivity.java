package com.collegemanagementsystem;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AttendanceActivity extends AppCompatActivity {

    DBHelper db;
    int studentId;

    TextView txtAttendance;
    Button btnPresent, btnAbsent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        // Get studentId safely (NO finish, NO close)
        studentId = getIntent().getIntExtra("student_id", 0);

        // Bind views
        txtAttendance = findViewById(R.id.txtAttendance);
        btnPresent = findViewById(R.id.btnPresent);
        btnAbsent = findViewById(R.id.btnAbsent);

        // DB
        db = new DBHelper(this);

        // Init attendance only if valid student
        if (studentId != 0) {
            db.initAttendance(studentId);
            updateAttendance();
        } else {
            txtAttendance.setText("Attendance: N/A");
            Toast.makeText(this, "Student ID missing", Toast.LENGTH_SHORT).show();
        }

        // Present
        btnPresent.setOnClickListener(v -> {
            if (studentId != 0) {
                db.markPresent(studentId);
                updateAttendance();
            }
        });

        // Absent
        btnAbsent.setOnClickListener(v -> {
            if (studentId != 0) {
                db.markAbsent(studentId);
                updateAttendance();
            }
        });
    }

    // ✅ This method MUST exist
    private void updateAttendance() {
        int percent = db.getAttendancePercent(studentId);
        txtAttendance.setText("Attendance: " + percent + "%");
    }
}
