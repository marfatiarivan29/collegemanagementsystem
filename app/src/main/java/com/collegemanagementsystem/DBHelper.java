
package com.collegemanagementsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "CollegeDB";
    private static final int DB_VERSION = 2; // Incremented version to force upgrade

    // Attendance Table
    private static final String TABLE_ATTENDANCE = "attendance";
    private static final String COL_ATTENDANCE_ID = "id";
    private static final String COL_PRESENT = "present";
    private static final String COL_TOTAL = "total";

    // Student Table
    private static final String TABLE_STUDENT = "student";
    private static final String COL_STUDENT_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_COURSE = "course";

    // Todo Table
    private static final String TABLE_TODO = "todo";
    private static final String COL_TODO_ID = "id";
    private static final String COL_TASK = "task";

    // Logs Table
    private static final String TABLE_LOGS = "attendance_logs";
    private static final String COL_LOG_STUDENT_ID = "student_id";
    private static final String COL_LOG_DATE = "date"; // Format: YYYY-MM-DD
    private static final String COL_LOG_STATUS = "status"; // "P" or "A"

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 3); // Version 3
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Attendance Table
        String createAttendance = "CREATE TABLE " + TABLE_ATTENDANCE + " (" +
                COL_ATTENDANCE_ID + " INTEGER PRIMARY KEY, " +
                COL_PRESENT + " INTEGER, " +
                COL_TOTAL + " INTEGER)";
        db.execSQL(createAttendance);

        // Create Student Table
        String createStudent = "CREATE TABLE " + TABLE_STUDENT + " (" +
                COL_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_COURSE + " TEXT)";
        db.execSQL(createStudent);

        // Create Todo Table
        String createTodo = "CREATE TABLE " + TABLE_TODO + " (" +
                COL_TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TASK + " TEXT)";
        db.execSQL(createTodo);

        // Create Logs Table
        String createLogs = "CREATE TABLE " + TABLE_LOGS + " (" +
                COL_LOG_STUDENT_ID + " INTEGER, " +
                COL_LOG_DATE + " TEXT, " +
                COL_LOG_STATUS + " TEXT)";
        db.execSQL(createLogs);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            // Add logs table if upgrading
            String createLogs = "CREATE TABLE " + TABLE_LOGS + " (" +
                COL_LOG_STUDENT_ID + " INTEGER, " +
                COL_LOG_DATE + " TEXT, " +
                COL_LOG_STATUS + " TEXT)";
            db.execSQL(createLogs);
        }
    }

    // ---------------- STUDENT METHODS ----------------

    public boolean insertStudent(String name, String course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_COURSE, course);
        long result = db.insert(TABLE_STUDENT, null, cv);
        return result != -1;
    }

    public java.util.ArrayList<Student> getAllStudents() {
        java.util.ArrayList<Student> list = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_STUDENT, null);
        if (c.moveToFirst()) {
            do {
                int id = c.getInt(0);
                String name = c.getString(1);
                String course = c.getString(2);
                list.add(new Student(id, name, course));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    // ---------------- TODO METHODS ----------------

    public void addTodo(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TASK, task);
        db.insert(TABLE_TODO, null, cv);
    }

    public void deleteTodo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, COL_TODO_ID + "=?", new String[]{String.valueOf(id)});
    }

    public Cursor getTodos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TODO, null);
    }

    // ---------------- ATTENDANCE METHODS ----------------

    // Initialize attendance for a student
    public void initAttendance(int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ATTENDANCE +
                " WHERE " + COL_ATTENDANCE_ID + " = ?", new String[]{String.valueOf(studentId)});

        if (!cursor.moveToFirst()) {
            ContentValues cv = new ContentValues();
            cv.put(COL_ATTENDANCE_ID, studentId);
            cv.put(COL_PRESENT, 0);
            cv.put(COL_TOTAL, 0);
            db.insert(TABLE_ATTENDANCE, null, cv);
        }

        cursor.close();
    }

    // Mark Present with Date
    public void markPresent(int studentId, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        // Update stats
        db.execSQL("UPDATE " + TABLE_ATTENDANCE +
                " SET " + COL_PRESENT + " = " + COL_PRESENT + " + 1, " +
                COL_TOTAL + " = " + COL_TOTAL + " + 1 WHERE " + COL_ATTENDANCE_ID + " = " + studentId);
        
        // Log entry
        ContentValues cv = new ContentValues();
        cv.put(COL_LOG_STUDENT_ID, studentId);
        cv.put(COL_LOG_DATE, date);
        cv.put(COL_LOG_STATUS, "P");
        db.insert(TABLE_LOGS, null, cv);
    }

    // Mark Absent with Date
    public void markAbsent(int studentId, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Update stats
        db.execSQL("UPDATE " + TABLE_ATTENDANCE +
                " SET " + COL_TOTAL + " = " + COL_TOTAL + " + 1 WHERE " + COL_ATTENDANCE_ID + " = " + studentId);

        // Log entry
        ContentValues cv = new ContentValues();
        cv.put(COL_LOG_STUDENT_ID, studentId);
        cv.put(COL_LOG_DATE, date);
        cv.put(COL_LOG_STATUS, "A");
        db.insert(TABLE_LOGS, null, cv);
    }

    // Get Logs
    public Cursor getLogs(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_LOGS + " WHERE " + COL_LOG_STUDENT_ID + " = ?", 
        new String[]{String.valueOf(studentId)});
    }
    
    // Backward compatibility methods (if needed by other Activities)
    public void markPresent(int studentId) {
        // Default to today if no date provided? Or just update stats.
        // For safety, we just update stats to avoid breaking old calls, but logs won't have date.
         SQLiteDatabase db = this.getWritableDatabase();
         db.execSQL("UPDATE " + TABLE_ATTENDANCE +
                " SET " + COL_PRESENT + " = " + COL_PRESENT + " + 1, " +
                COL_TOTAL + " = " + COL_TOTAL + " + 1 WHERE " + COL_ATTENDANCE_ID + " = " + studentId);
    }

    public void markAbsent(int studentId) {
         SQLiteDatabase db = this.getWritableDatabase();
         db.execSQL("UPDATE " + TABLE_ATTENDANCE +
                " SET " + COL_TOTAL + " = " + COL_TOTAL + " + 1 WHERE " + COL_ATTENDANCE_ID + " = " + studentId);
    }

    // Get Attendance Percentage
    public int getAttendancePercent(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ATTENDANCE +
                " WHERE " + COL_ATTENDANCE_ID + " = ?", new String[]{String.valueOf(studentId)});

        if (cursor.moveToFirst()) {
            int present = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRESENT));
            int total = cursor.getInt(cursor.getColumnIndexOrThrow(COL_TOTAL));

            cursor.close();

            if (total == 0) return 0;
            return (present * 100) / total;
        }

        cursor.close();
        return 0;
    }
}
