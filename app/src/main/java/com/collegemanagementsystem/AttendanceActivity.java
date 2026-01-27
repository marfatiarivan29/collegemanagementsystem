package com.collegemanagementsystem;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AttendanceActivity extends AppCompatActivity {

    TextView txtAttendance;
    Button btnPresent, btnAbsent;
    CalendarView calendarView;
    DBHelper db;
    int studentId;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_attendance);

            // Bind Views
            txtAttendance = findViewById(R.id.txtAttendance);
            btnPresent = findViewById(R.id.btnPresent);
            btnAbsent = findViewById(R.id.btnAbsent);
            calendarView = findViewById(R.id.calendarView);

            db = new DBHelper(this);

            studentId = getIntent().getIntExtra("student_id", -1);

            if (studentId == -1) {
                Toast.makeText(this, "Error: Invalid Student ID", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Ensure attendance record exists
            db.initAttendance(studentId);
            updateUI();
            try {
                loadCalendarEvents();
            } catch (Exception e) { 
                Toast.makeText(this, "Calendar Load Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            btnPresent.setOnClickListener(v -> {
                try {
                    Calendar selectedDate = calendarView.getFirstSelectedDate();
                    if (selectedDate == null) {
                        selectedDate = Calendar.getInstance(); // Default to today
                    }
                    String dateStr = sdf.format(selectedDate.getTime());
                    
                    db.markPresent(studentId, dateStr);
                    Toast.makeText(this, "Marked Present: " + dateStr, Toast.LENGTH_SHORT).show();
                    updateUI();
                    loadCalendarEvents();
                } catch (Exception e) {
                    Toast.makeText(this, "Action Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            btnAbsent.setOnClickListener(v -> {
                try {
                    Calendar selectedDate = calendarView.getFirstSelectedDate();
                    if (selectedDate == null) {
                        selectedDate = Calendar.getInstance(); // Default to today
                    }
                    String dateStr = sdf.format(selectedDate.getTime());

                    db.markAbsent(studentId, dateStr);
                    Toast.makeText(this, "Marked Absent: " + dateStr, Toast.LENGTH_SHORT).show();
                    updateUI();
                    loadCalendarEvents();
                } catch (Exception e) {
                    Toast.makeText(this, "Action Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadCalendarEvents() {
        List<EventDay> events = new ArrayList<>();
        Cursor cursor = db.getLogs(studentId);

        if (cursor.moveToFirst()) {
            do {
                try {
                    String dateStr = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                    String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                    
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(sdf.parse(dateStr));

                    if ("P".equals(status)) {
                        events.add(new EventDay(calendar, R.drawable.ic_dot_green));
                    } else if ("A".equals(status)) {
                        events.add(new EventDay(calendar, R.drawable.ic_dot_red));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        calendarView.setEvents(events);
    }

    private void updateUI() {
        int percent = db.getAttendancePercent(studentId);
        txtAttendance.setText("Attendance: " + percent + "%");
    }
}
