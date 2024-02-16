package com.example.tvapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvapp.models.Channel;

import java.util.ArrayList;

public class showsRecyclerAdapter extends RecyclerView.Adapter<showsRecyclerHolder>{
    private ArrayList<Channel.Show> shows;
    private OnItemClickListener listener;

    public showsRecyclerAdapter(ArrayList<Channel.Show> shows, OnItemClickListener listener){
        this.shows = shows;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public showsRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_item, parent, false);
        return new showsRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull showsRecyclerHolder holder, int position) {
        holder.bind(shows.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return shows.size();
    }
}
