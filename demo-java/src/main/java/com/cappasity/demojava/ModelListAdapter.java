//--------------------------------------------------------------------------------------
// Cappasity technology / Cappasity 3D Scanning software solutions. U.S. Patent Pending.
//
// CONTENT IS PROHIBITED FROM USAGE. CAPPASITY INC. CONFIDENTIAL.
//
// Copyright (C) Cappasity Inc. 2013-2021. All rights reserved.
//--------------------------------------------------------------------------------------

package com.cappasity.demojava;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cappasity.framework.CappasityModel;
import java.util.ArrayList;
import java.util.List;

public class ModelListAdapter extends RecyclerView.Adapter<ModelListAdapter.ModelListViewHolder> {

    private final List<CappasityModel> items = new ArrayList<>();

    private OnItemClickListener onItemClickListener;

    public ModelListAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClicked(CappasityModel model);
    }

    public void updateItems(List<CappasityModel> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ModelListAdapter.ModelListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_model, parent, false);
        return new ModelListViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ModelListAdapter.ModelListViewHolder holder, int position) {
        holder.bind(items.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ModelListViewHolder extends RecyclerView.ViewHolder {

        public ModelListViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(CappasityModel model, OnItemClickListener onItemClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClicked(model);
                }
            });

            AppCompatTextView tvName = itemView.findViewById(R.id.tvName);
            AppCompatImageView imageView = itemView.findViewById(R.id.imageView);

            Glide.with(imageView).load(model.getPreviewUrl()).into(imageView);
            tvName.setText(model.getTitle());
        }
    }

}