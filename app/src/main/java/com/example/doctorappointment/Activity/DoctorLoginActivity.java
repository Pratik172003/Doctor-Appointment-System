package com.example.doctorappointment.Activity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class DoctorLoginActivity extends AppCompatActivity {
    private EditText emailInput, passwordInput; private Button loginButton; private FirebaseAuth auth;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); auth = FirebaseAuth.getInstance();
        LinearLayout root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setGravity(Gravity.CENTER); root.setPadding(32,32,32,32); root.setBackgroundColor(0xFFFFFFFF);
        TextView title = new TextView(this); title.setText("Doctor Login"); title.setTextSize(26); title.setTextColor(0xFF1565C0); title.setGravity(Gravity.CENTER); root.addView(title);
        emailInput = new EditText(this); emailInput.setHint("Doctor email"); emailInput.setInputType( android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS); root.addView(emailInput, new LinearLayout.LayoutParams(-1,-2));
        passwordInput = new EditText(this); passwordInput.setHint("Password"); passwordInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD); root.addView(passwordInput, new LinearLayout.LayoutParams(-1,-2));
        loginButton = new Button(this); loginButton.setText("Login"); root.addView(loginButton, new LinearLayout.LayoutParams(-1,-2));
        TextView note = new TextView(this); note.setText("Doctor accounts are created by the app administrator."); note.setGravity(Gravity.CENTER); root.addView(note); setContentView(root);
        loginButton.setOnClickListener(v -> loginDoctor());
    }
    private void loginDoctor() {
        String email = emailInput.getText().toString().trim(), password = passwordInput.getText().toString();
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) { emailInput.setError("Enter valid email"); return; }
        if (password.isEmpty()) { passwordInput.setError("Enter password"); return; }
        loginButton.setEnabled(false);
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, task -> {
            loginButton.setEnabled(true);
            if (task.isSuccessful() && auth.getCurrentUser()!=null) { startActivity(new android.content.Intent(this, DoctorDashboardActivity.class)); finish(); }
            else Toast.makeText(this, "Login failed: " + (task.getException()!=null?task.getException().getMessage():"Check credentials"), Toast.LENGTH_LONG).show();
        });
    }
}
