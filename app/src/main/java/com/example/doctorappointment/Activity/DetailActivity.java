package com.example.doctorappointment.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.doctorappointment.Adapter.DateAdapter;
import com.example.doctorappointment.Adapter.TimeAdapter;
import com.example.doctorappointment.Domain.DoctorsModel;
import com.example.doctorappointment.R;
import com.example.doctorappointment.databinding.ActivityDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private DoctorsModel item;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private String selectedDate = "";
    private String selectedTime = "";

    private boolean bookingInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        item = (DoctorsModel) getIntent()
                .getSerializableExtra("object");

        if (item == null) {

            Toast.makeText(
                    this,
                    "Doctor details not found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        setVisible();
        initDate();
        initTime();
    }

    // DATE RECYCLER
    private void initDate() {

        binding.dateView.setLayoutManager(
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false
                )
        );

        binding.dateView.setAdapter(
                new DateAdapter(
                        generateDates(),
                        date -> selectedDate = date
                )
        );
    }

    // TIME RECYCLER
    private void initTime() {

        binding.timeView.setLayoutManager(
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false
                )
        );

        binding.timeView.setAdapter(
                new TimeAdapter(
                        generateTimeSlots(),
                        time -> selectedTime = time
                )
        );
    }

    // GENERATE TIME SLOTS
    public static List<String> generateTimeSlots() {

        List<String> timeSlots = new ArrayList<>();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "hh:mm a",
                        Locale.ENGLISH
                );

        for (int i = 0; i < 24; i += 2) {

            LocalTime time = LocalTime.of(i, 0);

            timeSlots.add(
                    time.format(formatter)
            );
        }

        return timeSlots;
    }

    // GENERATE DATES
    public static List<String> generateDates() {

        List<String> dates = new ArrayList<>();

        LocalDate today = LocalDate.now();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "EEE/dd/MMM",
                        Locale.ENGLISH
                );

        for (int i = 0; i < 7; i++) {

            dates.add(
                    today.plusDays(i).format(formatter)
            );
        }

        return dates;
    }

    // DOCTOR DETAILS
    private void setVisible() {

        binding.backBtn.setOnClickListener(
                v -> finish()
        );

        int doctorImage = getIntent().getIntExtra(
                "doctorImage",
                R.drawable.dr_david_johnson
        );

        binding.img.setImageResource(doctorImage);

        binding.addressTxt.setText(
                item.getAddress() != null
                        ? item.getAddress()
                        : "Address not available"
        );

        binding.nameTxt.setText(
                item.getName() != null
                        ? item.getName()
                        : "Doctor"
        );

        binding.specialTxt.setText(
                item.getSpecial() != null
                        ? item.getSpecial()
                        : "Specialization not available"
        );

        binding.patiensTxt.setText(
                String.valueOf(item.getPatiens())
        );

        binding.experienceTxt.setText(
                item.getExperiense() + " Years"
        );

        binding.button.setOnClickListener(
                v -> showPatientNameDialog()
        );
    }

    // BOOK APPOINTMENT
    private void showPatientNameDialog() {

        if (bookingInProgress) {
            return;
        }

        if (selectedDate.isEmpty() ||
                selectedTime.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please select date and time",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {

            Toast.makeText(
                    this,
                    "Please login first",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        EditText input = new EditText(this);

        input.setHint("Enter Patient Name");
        input.setSingleLine(true);

        new AlertDialog.Builder(this)
                .setTitle("Patient Details")
                .setMessage("Please enter patient name")
                .setView(input)
                .setPositiveButton(
                        "Book Appointment",
                        (dialog, which) -> {

                            String patientName =
                                    input.getText()
                                            .toString()
                                            .trim();

                            if (patientName.isEmpty()) {

                                Toast.makeText(
                                        DetailActivity.this,
                                        "Please enter patient name",
                                        Toast.LENGTH_SHORT
                                ).show();

                                return;
                            }

                            saveAppointment(
                                    patientName,
                                    user
                            );
                        }
                )
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .show();
    }

    // SAVE APPOINTMENT
    private void saveAppointment(
            String patientName,
            FirebaseUser user) {

        if (bookingInProgress) {
            return;
        }

        bookingInProgress = true;
        binding.button.setEnabled(false);

        int doctorImage = getIntent().getIntExtra(
                "doctorImage",
                R.drawable.dr_david_johnson
        );

        Map<String, Object> appointment = new HashMap<>();

        // Doctor UID is needed so the correct doctor can see this booking.
        String linkedDoctorUid = item.getDoctorUid();
        if (linkedDoctorUid == null || linkedDoctorUid.trim().isEmpty()) {
            bookingInProgress = false;
            binding.button.setEnabled(true);
            Toast.makeText(this, "This doctor is not linked to a doctor login yet. Please contact the administrator.", Toast.LENGTH_LONG).show();
            return;
        }
        appointment.put("doctorUid", linkedDoctorUid.trim());

        // Doctor information
        appointment.put(
                "doctorName",
                item.getName() != null
                        ? item.getName()
                        : ""
        );

        appointment.put(
                "specialization",
                item.getSpecial() != null
                        ? item.getSpecial()
                        : ""
        );

        appointment.put(
                "doctorSpecialization",
                item.getSpecial() != null
                        ? item.getSpecial()
                        : ""
        );

        appointment.put(
                "doctorAddress",
                item.getAddress() != null
                        ? item.getAddress()
                        : ""
        );

        appointment.put(
                "doctorImage",
                doctorImage
        );

        // Patient information
        appointment.put(
                "patientName",
                patientName
        );

        appointment.put(
                "patientUid",
                user.getUid()
        );

        // Booking information
        appointment.put(
                "appointmentDate",
                selectedDate
        );

        appointment.put(
                "appointmentTime",
                selectedTime
        );

        // Initial status
        appointment.put(
                "status",
                "Booked"
        );

        appointment.put(
                "createdAt",
                FieldValue.serverTimestamp()
        );

        db.collection("Appointments")
                .add(appointment)
                .addOnSuccessListener(documentReference -> {

                    bookingInProgress = false;

                    Toast.makeText(
                            DetailActivity.this,
                            "Appointment booked successfully!",
                            Toast.LENGTH_LONG
                    ).show();

                    finish();
                })
                .addOnFailureListener(e -> {

                    bookingInProgress = false;
                    binding.button.setEnabled(true);

                    Toast.makeText(
                            DetailActivity.this,
                            "Booking failed: "
                                    + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }
}
