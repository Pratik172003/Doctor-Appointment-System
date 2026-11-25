package com.example.doctorappointment.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappointment.R;
import com.example.doctorappointment.Adapter.AppointmentAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyAppointmentsActivity extends AppCompatActivity {

    RecyclerView appointmentsRecyclerView;
    TextView emptyTxt;
    ImageView backBtn;

    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointments);

        appointmentsRecyclerView = findViewById(R.id.appointmentsRecyclerView);
        emptyTxt = findViewById(R.id.emptyTxt);
        backBtn = findViewById(R.id.backBtn);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        appointmentsRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        backBtn.setOnClickListener(v -> finish());

        loadAppointments();
    }

    private void loadAppointments() {

        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db.collection("Appointments")
                .whereEqualTo("patientUid", currentUser.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    List<DocumentSnapshot> appointmentList =
                            queryDocumentSnapshots.getDocuments();

                    if (appointmentList.isEmpty()) {
                        emptyTxt.setVisibility(View.VISIBLE);
                        appointmentsRecyclerView.setVisibility(View.GONE);
                    } else {
                        emptyTxt.setVisibility(View.GONE);
                        appointmentsRecyclerView.setVisibility(View.VISIBLE);

                        AppointmentAdapter adapter =
                                new AppointmentAdapter(appointmentList);

                        appointmentsRecyclerView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(
                            MyAppointmentsActivity.this,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }
}