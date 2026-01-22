
package com.marwadi.collegemanagementsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "CollegeDB";
    private static final int DB_VERSION = 1;

    // Attendance Table
    private static final String TABLE_ATTENDANCE = "attendance";
    private static final String COL_ID = "id";
    private static final String COL_PRESENT = "present";
    private static final String COL_TOTAL = "total";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create Attendance Table
        String createAttendance = "CREATE TABLE " + TABLE_ATTENDANCE + " (" +
                COL_ID + " INTEGER PRIMARY KEY, " +
                COL_PRESENT + " INTEGER, " +
                COL_TOTAL + " INTEGER)";
        db.execSQL(createAttendance);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        onCreate(db);
    }

    // ---------------- ATTENDANCE METHODS ----------------

    // Initialize attendance for a student
    public void initAttendance(int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ATTENDANCE +
                " WHERE " + COL_ID + " = ?", new String[]{String.valueOf(studentId)});

        if (!cursor.moveToFirst()) {
            ContentValues cv = new ContentValues();
            cv.put(COL_ID, studentId);
            cv.put(COL_PRESENT, 0);
            cv.put(COL_TOTAL, 0);
            db.insert(TABLE_ATTENDANCE, null, cv);
        }

        cursor.close();
    }

    // Mark Present
    public void markPresent(int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE " + TABLE_ATTENDANCE +
                " SET " + COL_PRESENT + " = " + COL_PRESENT + " + 1, " +
                COL_TOTAL + " = " + COL_TOTAL + " + 1 WHERE " + COL_ID + " = " + studentId);
    }

    // Mark Absent
    public void markAbsent(int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE " + TABLE_ATTENDANCE +
                " SET " + COL_TOTAL + " = " + COL_TOTAL + " + 1 WHERE " + COL_ID + " = " + studentId);
    }

    // Get Attendance Percentage
    public int getAttendancePercent(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ATTENDANCE +
                " WHERE " + COL_ID + " = ?", new String[]{String.valueOf(studentId)});

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
