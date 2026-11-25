package com.example.doctorappointment.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doctorappointment.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginBtn, signupBtn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signupBtn);

        loginBtn.setOnClickListener(v -> loginUser());
        signupBtn.setOnClickListener(v -> registerUser());
    }

    private boolean validateInput(String email, String password) {

        if (email.isEmpty()) {
            emailInput.setError("Email required");
            emailInput.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Enter valid email");
            emailInput.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            passwordInput.setError("Password required");
            passwordInput.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            passwordInput.requestFocus();
            return false;
        }

        return true;
    }

    private void loginUser() {

        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (!validateInput(email, password)) return;

        setButtonsEnabled(false);

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    setButtonsEnabled(true);

                    if (task.isSuccessful()) {
                        Toast.makeText(this,
                                "Login Successful",
                                Toast.LENGTH_SHORT).show();

                        openMainActivity();

                    } else {
                        Toast.makeText(this,
                                "Login Failed: " +
                                        (task.getException() != null
                                                ? task.getException().getMessage()
                                                : "Please try again"),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void registerUser() {

        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (!validateInput(email, password)) return;

        setButtonsEnabled(false);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    setButtonsEnabled(true);

                    if (task.isSuccessful()) {
                        Toast.makeText(this,
                                "Account Created Successfully",
                                Toast.LENGTH_SHORT).show();

                        openMainActivity();

                    } else {
                        Toast.makeText(this,
                                "Signup Failed: " +
                                        (task.getException() != null
                                                ? task.getException().getMessage()
                                                : "Please try again"),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setButtonsEnabled(boolean enabled) {
        loginBtn.setEnabled(enabled);
        signupBtn.setEnabled(enabled);
    }

    private void openMainActivity() {
        Intent intent = new Intent(
                LoginActivity.this,
                MainActivity.class
        );
        startActivity(intent);
        finish();
    }
}
