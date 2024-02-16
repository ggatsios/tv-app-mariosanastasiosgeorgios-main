package com.example.tvapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvapp.models.Channel;

import java.util.ArrayList;

public class ChannelAdapter extends RecyclerView.Adapter<channelHolder> {
    private ArrayList<Channel> channels;
    private OnItemClickListener listener;

    public ChannelAdapter(ArrayList<Channel> channels, OnItemClickListener listener) {
        this.channels = channels;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public channelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_list_item, parent, false);
        return new channelHolder(view);
    }

    public void onBindViewHolder(@NonNull channelHolder holder, int position) {
        holder.bind(channels.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }
 }