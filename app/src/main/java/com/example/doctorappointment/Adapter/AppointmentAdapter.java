package com.example.doctorappointment.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappointment.R;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class AppointmentAdapter
        extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private final List<DocumentSnapshot> appointmentList;

    public AppointmentAdapter(List<DocumentSnapshot> appointmentList) {
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointment_item, parent, false);

        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull AppointmentViewHolder holder,
            int position) {

        DocumentSnapshot document = appointmentList.get(position);

        // Doctor photo
        Object imageValue = document.get("doctorImage");
        if (imageValue instanceof Number) {
            int imageRes = ((Number) imageValue).intValue();
            try {
                holder.doctorImageView.setImageResource(imageRes);
            } catch (Exception e) {
                holder.doctorImageView.setImageResource(R.drawable.doctor_placeholder);
            }
        } else {
            holder.doctorImageView.setImageResource(R.drawable.doctor_placeholder);
        }

        // Doctor name
        holder.doctorNameTxt.setText(
                "Doctor: " + getValue(document, "doctorName")
        );

        // Specialization - read both old and new field
        String specialization =
                document.getString("specialization");

        if (specialization == null ||
                specialization.trim().isEmpty()) {

            specialization =
                    document.getString("doctorSpecialization");
        }

        if (specialization == null ||
                specialization.trim().isEmpty()) {

            specialization = "Not available";
        }

        holder.specializationTxt.setText(
                "Specialization: " + specialization
        );

        // Patient name
        holder.patientNameTxt.setText(
                "Patient: " + getValue(document, "patientName")
        );

        // Date
        holder.dateTxt.setText(
                "Date: " + getValue(document, "appointmentDate")
        );

        // Time
        holder.timeTxt.setText(
                "Time: " + getValue(document, "appointmentTime")
        );

        // Status
        String status = getValue(document, "status");

        holder.statusTxt.setText(
                "Status: " + status
        );
    }

    // Safe Firestore value reader
    private String getValue(
            DocumentSnapshot document,
            String field) {

        String value = document.getString(field);

        if (value == null || value.trim().isEmpty()) {
            return "Not available";
        }

        return value;
    }

    @Override
    public int getItemCount() {
        return appointmentList != null
                ? appointmentList.size()
                : 0;
    }

    public static class AppointmentViewHolder
            extends RecyclerView.ViewHolder {

        ImageView doctorImageView;
        TextView doctorNameTxt;
        TextView specializationTxt;
        TextView patientNameTxt;
        TextView dateTxt;
        TextView timeTxt;
        TextView statusTxt;

        public AppointmentViewHolder(
                @NonNull View itemView) {

            super(itemView);

            doctorImageView = itemView.findViewById(
                    R.id.doctorImageView
            );

            doctorNameTxt = itemView.findViewById(
                    R.id.doctorNameTxt
            );

            specializationTxt = itemView.findViewById(
                    R.id.specializationTxt
            );

            patientNameTxt = itemView.findViewById(
                    R.id.patientNameTxt
            );

            dateTxt = itemView.findViewById(
                    R.id.dateTxt
            );

            timeTxt = itemView.findViewById(
                    R.id.timeTxt
            );

            statusTxt = itemView.findViewById(
                    R.id.statusTxt
            );
        }
    }
}
