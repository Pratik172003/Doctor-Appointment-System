package com.example.doctorappointment.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RoleSelectionActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL); root.setGravity(17); root.setPadding(40,40,40,40); root.setBackgroundColor(0xFFFFFFFF);
        TextView title = new TextView(this); title.setText("Choose Login Type"); title.setTextSize(26); title.setTextColor(0xFF1565C0); title.setGravity(17);
        root.addView(title, new LinearLayout.LayoutParams(-1,-2));
        Button patient = new Button(this); patient.setText("Patient Login"); root.addView(patient, new LinearLayout.LayoutParams(-1,-2));
        Button doctor = new Button(this); doctor.setText("Doctor Login"); root.addView(doctor, new LinearLayout.LayoutParams(-1,-2));
        setContentView(root);
        patient.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        doctor.setOnClickListener(v -> startActivity(new Intent(this, DoctorLoginActivity.class)));
    }
}
