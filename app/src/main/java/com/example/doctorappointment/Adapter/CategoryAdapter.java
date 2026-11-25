
package com.example.doctorappointment.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappointment.Domain.CategoryModel;
import com.example.doctorappointment.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Viewholder> {

    private final List<CategoryModel> items;

    // Local category images
    private final int[] categoryImages = {
            R.drawable.cardiology,
            R.drawable.dentistry,
            R.drawable.neurology,
            R.drawable.orthopedics,
            R.drawable.radiology
    };

    public CategoryAdapter(List<CategoryModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_category, parent, false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        CategoryModel item = items.get(position);

        // Category name
        holder.titleCat.setText(item.getName());

        // Local category image
        if (position < categoryImages.length) {
            holder.picCat.setImageResource(categoryImages[position]);
        }

        // Category backgrounds
        int[] backgrounds = {
                R.drawable.blue_rec_bg,
                R.drawable.blue_btn_bg,
                R.drawable.purple_rec_bg,
                R.drawable.orange_rec_bg
        };

        holder.itemView.setBackgroundResource(
                backgrounds[position % backgrounds.length]
        );
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class Viewholder extends RecyclerView.ViewHolder {

        TextView titleCat;
        ImageView picCat;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            titleCat = itemView.findViewById(R.id.titleCat);
            picCat = itemView.findViewById(R.id.picCat);
        }
    }
}
