package com.example.doctorappointment.Activity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappointment.Adapter.DoctorAppointmentAdapter;
import com.example.doctorappointment.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DoctorDashboardActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "doctor_appointments";
    private static final int NOTIFICATION_PERMISSION_REQUEST = 1001;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private ListenerRegistration appointmentListener;

    private RecyclerView appointmentsRecyclerView;
    private View emptyView;
    private DoctorAppointmentAdapter adapter;
    private final List<DocumentSnapshot> appointmentList = new ArrayList<>();

    private boolean firstSnapshot = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        appointmentsRecyclerView = findViewById(R.id.appointmentsRecyclerView);
        emptyView = findViewById(R.id.emptyView);

        appointmentsRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter = new DoctorAppointmentAdapter(
                appointmentList,
                this::updateAppointmentStatus
        );
        appointmentsRecyclerView.setAdapter(adapter);

        findViewById(R.id.refreshBtn).setOnClickListener(
                v -> startAppointmentListener()
        );

        findViewById(R.id.logoutBtn).setOnClickListener(v -> {
            stopAppointmentListener();
            auth.signOut();
            finish();
        });

        createNotificationChannel();
        requestNotificationPermissionIfNeeded();

        startAppointmentListener();
    }

    private void startAppointmentListener() {

        stopAppointmentListener();

        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Doctor login required", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String doctorUid = auth.getCurrentUser().getUid();
        firstSnapshot = true;

        appointmentListener = db.collection("Appointments")
                .whereEqualTo("doctorUid", doctorUid)
                .addSnapshotListener((snapshots, error) -> {

                    if (error != null) {
                        Toast.makeText(
                                this,
                                "Could not load appointments: " + error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                        return;
                    }

                    if (snapshots == null) return;

                    appointmentList.clear();

                    for (DocumentSnapshot document : snapshots.getDocuments()) {
                        appointmentList.add(document);
                    }

                    adapter.notifyDataSetChanged();

                    if (appointmentList.isEmpty()) {
                        emptyView.setVisibility(View.VISIBLE);
                        appointmentsRecyclerView.setVisibility(View.GONE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        appointmentsRecyclerView.setVisibility(View.VISIBLE);
                    }

                    // Do not notify for appointments already present when the
                    // doctor first opens the dashboard.
                    if (!firstSnapshot) {
                        for (DocumentChange change : snapshots.getDocumentChanges()) {
                            if (change.getType() == DocumentChange.Type.ADDED) {
                                showNewAppointmentNotification(change.getDocument());
                            }
                        }
                    }

                    firstSnapshot = false;
                });
    }

    private void updateAppointmentStatus(
            DocumentSnapshot document,
            String status
    ) {
        db.collection("Appointments")
                .document(document.getId())
                .update("status", status)
                .addOnSuccessListener(unused ->
                        Toast.makeText(
                                this,
                                "Appointment " + status.toLowerCase(),
                                Toast.LENGTH_SHORT
                        ).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(
                                this,
                                "Could not update appointment: " + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show()
                );
    }

    private void showNewAppointmentNotification(DocumentSnapshot document) {

        String patient = safe(document.getString("patientName"), "Patient");
        String date = safe(document.getString("appointmentDate"), "Date not available");
        String time = safe(document.getString("appointmentTime"), "Time not available");

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("New Appointment")
                        .setContentText(patient + " • " + date + " • " + time)
                        .setStyle(
                                new NotificationCompat.BigTextStyle()
                                        .bigText(
                                                "New appointment from " + patient
                                                        + "\nDate: " + date
                                                        + "\nTime: " + time
                                        )
                        )
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

        if (Build.VERSION.SDK_INT < 33 ||
                ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED) {

            NotificationManagerCompat.from(this)
                    .notify(
                            document.getId().hashCode(),
                            builder.build()
                    );
        }
    }

    private String safe(String value, String fallback) {
        return value == null || value.trim().isEmpty()
                ? fallback
                : value;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Doctor Appointments",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription(
                    "Notifications for new patient appointments"
            );

            NotificationManager manager =
                    getSystemService(NotificationManager.class);

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= 33 &&
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    NOTIFICATION_PERMISSION_REQUEST
            );
        }
    }

    private void stopAppointmentListener() {
        if (appointmentListener != null) {
            appointmentListener.remove();
            appointmentListener = null;
        }
    }

    @Override
    protected void onDestroy() {
        stopAppointmentListener();
        super.onDestroy();
    }
}
