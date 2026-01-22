package com.collegemanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {

            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            // Simple admin login
            if (username.equals("admin") && password.equals("admin")) {

                // 🔥 FIX: Open Admin Dashboard (NOT MainActivity)
                Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(LoginActivity.this, "Invalid Login", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
