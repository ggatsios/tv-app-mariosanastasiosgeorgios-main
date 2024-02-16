package com.example.tvapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tvapp.models.Channel;


public class channelHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ChannelAdapter.OnItemClickListener listener;

    public channelHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    public void bind(Channel channel, ChannelAdapter.OnItemClickListener listener) {
        TextView channelText = itemView.findViewById(R.id.channelName);
        ImageView channelImage = itemView.findViewById(R.id.channelImage);
        this.listener = listener;
        channelText.setText(channel.getChannelName());
        Glide.with(itemView.getContext()).load("https://www.cosmotetv.gr" + channel.getChannelImageUrl()).into(channelImage);
    }

    @Override
    public void onClick(View v) {
        listener.onItemClick(getAdapterPosition());
    }
}