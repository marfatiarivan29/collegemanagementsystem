package com.collegemanagementsystem;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class AddStudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_add_student);

        EditText n = findViewById(R.id.edtName);
        EditText c = findViewById(R.id.edtCourse);
        Button s = findViewById(R.id.btnSave);

        DBHelper db = new DBHelper(this);

        s.setOnClickListener(v -> {
            if (db.insertStudent(n.getText().toString(), c.getText().toString())) {
                Toast.makeText(this, "Student Added", Toast.LENGTH_SHORT).show();
                n.setText(""); c.setText("");
            }
        });
    }
}
