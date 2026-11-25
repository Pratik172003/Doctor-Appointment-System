package com.example.doctorappointment.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappointment.R;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class DoctorAppointmentAdapter
        extends RecyclerView.Adapter<DoctorAppointmentAdapter.ViewHolder> {

    public interface StatusListener {
        void onStatusChange(DocumentSnapshot document, String status);
    }

    private final List<DocumentSnapshot> appointments;
    private final StatusListener listener;

    public DoctorAppointmentAdapter(
            List<DocumentSnapshot> appointments,
            StatusListener listener
    ) {
        this.appointments = appointments;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor_appointment, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {
        DocumentSnapshot document = appointments.get(position);

        holder.patientNameTxt.setText(
                "Patient: " + value(document, "patientName")
        );

        holder.dateTxt.setText(
                "Date: " + value(document, "appointmentDate")
        );

        holder.timeTxt.setText(
                "Time: " + value(document, "appointmentTime")
        );

        holder.specializationTxt.setText(
                "Specialization: " +
                        value(document, "specialization")
        );

        holder.statusTxt.setText(
                "Status: " + value(document, "status")
        );

        String status = value(document, "status");

        boolean active = status.equalsIgnoreCase("Booked")
                || status.equalsIgnoreCase("Pending");

        holder.acceptBtn.setEnabled(active);
        holder.rejectBtn.setEnabled(active);

        holder.acceptBtn.setOnClickListener(
                v -> listener.onStatusChange(document, "Accepted")
        );

        holder.rejectBtn.setOnClickListener(
                v -> listener.onStatusChange(document, "Rejected")
        );
    }

    private String value(
            DocumentSnapshot document,
            String field
    ) {
        String value = document.getString(field);

        if (value == null || value.trim().isEmpty()) {
            return "Not available";
        }

        return value;
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView patientNameTxt;
        TextView specializationTxt;
        TextView dateTxt;
        TextView timeTxt;
        TextView statusTxt;
        Button acceptBtn;
        Button rejectBtn;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            patientNameTxt =
                    itemView.findViewById(R.id.patientNameTxt);

            specializationTxt =
                    itemView.findViewById(R.id.specializationTxt);

            dateTxt =
                    itemView.findViewById(R.id.dateTxt);

            timeTxt =
                    itemView.findViewById(R.id.timeTxt);

            statusTxt =
                    itemView.findViewById(R.id.statusTxt);

            acceptBtn =
                    itemView.findViewById(R.id.acceptBtn);

            rejectBtn =
                    itemView.findViewById(R.id.rejectBtn);
        }
    }
}
