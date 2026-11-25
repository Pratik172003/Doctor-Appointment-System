
package com.example.doctorappointment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappointment.R;
import com.example.doctorappointment.databinding.ViewholderTimeBinding;

import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeViewholder> {

    private final List<String> timeSlots;
    private int selectedPosition = 0;

    public interface OnTimeSelectedListener {
        void onTimeSelected(String time);
    }

    private final OnTimeSelectedListener listener;

    public TimeAdapter(List<String> timeSlots,
                       OnTimeSelectedListener listener) {
        this.timeSlots = timeSlots;
        this.listener = listener;

        if (!timeSlots.isEmpty() && listener != null) {
            listener.onTimeSelected(timeSlots.get(0));
        }
    }

    @NonNull
    @Override
    public TimeViewholder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        ViewholderTimeBinding binding =
                ViewholderTimeBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                );

        return new TimeViewholder(binding);
    }

    @Override
    public void onBindViewHolder(
            @NonNull TimeViewholder holder,
            int position) {

        holder.bind(timeSlots.get(position), position);
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public class TimeViewholder extends RecyclerView.ViewHolder {

        private final ViewholderTimeBinding binding;

        public TimeViewholder(ViewholderTimeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String time, int position) {

            binding.timeTxt.setText(time);

            Context context = binding.getRoot().getContext();

            if (selectedPosition == position) {

                binding.timeTxt.setBackgroundResource(
                        R.drawable.blue_btn_bg
                );

                binding.timeTxt.setTextColor(
                        ContextCompat.getColor(context, R.color.white)
                );

            } else {

                binding.timeTxt.setBackgroundResource(
                        R.drawable.light_grey_bg
                );

                binding.timeTxt.setTextColor(
                        ContextCompat.getColor(context, R.color.black)
                );
            }

            binding.getRoot().setOnClickListener(v -> {

                int oldPosition = selectedPosition;
                selectedPosition = position;

                if (oldPosition != -1) {
                    notifyItemChanged(oldPosition);
                }

                notifyItemChanged(selectedPosition);

                if (listener != null) {
                    listener.onTimeSelected(time);
                }
            });
        }
    }
}
