package com.ocean.uploadpdfimageapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ocean.uploadpdfimageapp.databinding.ItemRvMultipleUploadAdapterBinding;

import java.util.List;

public class AdapterRecyclerViewItem extends RecyclerView.Adapter<AdapterRecyclerViewItem.MyViewHolder> {

    List<RvItemModel> list;
    ItemRvMultipleUploadAdapterBinding binding;
    Context context;

    @NonNull
    @Override
    public AdapterRecyclerViewItem.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemRvMultipleUploadAdapterBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerViewItem.MyViewHolder holder, int position) {
//        holder.binding.tvImageNameArray
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ItemRvMultipleUploadAdapterBinding binding;

        public MyViewHolder(ItemRvMultipleUploadAdapterBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
