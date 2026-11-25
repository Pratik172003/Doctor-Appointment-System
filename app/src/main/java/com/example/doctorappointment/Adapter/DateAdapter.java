
package com.example.doctorappointment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappointment.R;
import com.example.doctorappointment.databinding.ViewholderDateBinding;

import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {

    private final List<String> dateSlots;
    private int selectedPosition = 0;

    public interface OnDateSelectedListener {
        void onDateSelected(String date);
    }

    private final OnDateSelectedListener listener;

    public DateAdapter(List<String> dateSlots,
                       OnDateSelectedListener listener) {
        this.dateSlots = dateSlots;
        this.listener = listener;

        if (!dateSlots.isEmpty() && listener != null) {
            listener.onDateSelected(dateSlots.get(0));
        }
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        ViewholderDateBinding binding =
                ViewholderDateBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                );

        return new DateViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(
            @NonNull DateViewHolder holder,
            int position) {

        holder.bind(dateSlots.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dateSlots.size();
    }

    public class DateViewHolder extends RecyclerView.ViewHolder {

        private final ViewholderDateBinding binding;

        public DateViewHolder(ViewholderDateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String date, int position) {

            String[] dateParts = date.split("/");

            if (dateParts.length != 3) return;

            binding.dayTxt.setText(dateParts[0]);
            binding.dateMonthTxt.setText(
                    dateParts[1] + " " + dateParts[2]
            );

            Context context = binding.getRoot().getContext();

            if (selectedPosition == position) {

                binding.mainLayout.setBackgroundResource(
                        R.drawable.blue_btn_bg
                );

                binding.dayTxt.setTextColor(
                        ContextCompat.getColor(context, R.color.white)
                );

                binding.dateMonthTxt.setTextColor(
                        ContextCompat.getColor(context, R.color.white)
                );

            } else {

                binding.mainLayout.setBackgroundResource(
                        R.drawable.light_grey_bg
                );

                binding.dayTxt.setTextColor(
                        ContextCompat.getColor(context, R.color.black)
                );

                binding.dateMonthTxt.setTextColor(
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
                    listener.onDateSelected(date);
                }
            });
        }
    }
}
