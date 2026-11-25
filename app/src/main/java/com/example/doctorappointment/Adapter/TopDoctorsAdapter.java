
package com.example.doctorappointment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappointment.Activity.DetailActivity;
import com.example.doctorappointment.Domain.DoctorsModel;
import com.example.doctorappointment.R;
import com.example.doctorappointment.databinding.ViewholderTopDoctorBinding;

import java.util.List;

public class TopDoctorsAdapter
        extends RecyclerView.Adapter<TopDoctorsAdapter.Viewholder> {

    private final List<DoctorsModel> items;

    // Local doctor images
    private final int[] doctorImages = {
            R.drawable.dr_david_johnson,
            R.drawable.dr_michael_roberts,
            R.drawable.dr_sarah_thompson,
            R.drawable.dr_richard_lee,
            R.drawable.dr_jessica_wyne
    };

    public TopDoctorsAdapter(List<DoctorsModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        ViewholderTopDoctorBinding binding =
                ViewholderTopDoctorBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                );

        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(
            @NonNull Viewholder holder,
            int position) {

        DoctorsModel doctor = items.get(position);

        // Doctor Name
        holder.binding.nameTxt.setText(doctor.getName());

        // Speciality
        holder.binding.specialTxt.setText(doctor.getSpecial());

        // Rating
        holder.binding.ratingTxt.setText("★ " + doctor.getRating());

        // Patients
        holder.binding.patiensTxt.setText(
                doctor.getPatiens() + "+"
        );

        // Get correct local image
        int imagePosition = position % doctorImages.length;
        int selectedDoctorImage = doctorImages[imagePosition];

        holder.binding.img.setImageResource(selectedDoctorImage);

        // Open Doctor Details
        holder.binding.getRoot().setOnClickListener(v -> {

            Context context = v.getContext();

            Intent intent = new Intent(
                    context,
                    DetailActivity.class
            );

            intent.putExtra("object", doctor);

            // Send same doctor image to DetailActivity
            intent.putExtra("doctorImage", selectedDoctorImage);

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class Viewholder
            extends RecyclerView.ViewHolder {

        private final ViewholderTopDoctorBinding binding;

        public Viewholder(ViewholderTopDoctorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
