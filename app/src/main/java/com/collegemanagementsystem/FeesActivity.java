package com.collegemanagementsystem;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FeesActivity extends AppCompatActivity {

    TextView txtCourse, txtTotalFees, txtPendingFees;
    EditText edtPaidFees;

    int totalFees = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees);

        txtCourse = findViewById(R.id.txtCourse);
        txtTotalFees = findViewById(R.id.txtTotalFees);
        txtPendingFees = findViewById(R.id.txtPendingFees);
        edtPaidFees = findViewById(R.id.edtPaidFees);

        String course = getIntent().getStringExtra("course");

        totalFees = getFeesByCourse(course);

        txtCourse.setText(course);
        txtTotalFees.setText("₹ " + totalFees);
        txtPendingFees.setText("₹ " + totalFees);

        // 🔄 Recalculate pending fees when paid fees change
        edtPaidFees.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePendingFees(s.toString());
            }
        });
    }

    private void updatePendingFees(String paidText) {
        if (paidText.isEmpty()) {
            txtPendingFees.setText("₹ " + totalFees);
            return;
        }

        int paid = Integer.parseInt(paidText);

        if (paid > totalFees) {
            edtPaidFees.setError("Paid fees cannot exceed total fees");
            return;
        }

        int pending = totalFees - paid;
        txtPendingFees.setText("₹ " + pending);
    }

    // 💡 Dynamic fees by course
    private int getFeesByCourse(String course) {
        if (course == null) return 0;

        switch (course.toUpperCase()) {
            case "BCA": return 60000;
            case "BBA": return 55000;
            case "MCA": return 80000;
            case "MBA": return 90000;
            default: return 50000;
        }
    }
}
