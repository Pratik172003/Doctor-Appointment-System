package com.example.doctorappointment.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.doctorappointment.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashBinding binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.startBtn.setOnClickListener(v ->
                startActivity(new Intent(SplashActivity.this, RoleSelectionActivity.class)));
    }
}
