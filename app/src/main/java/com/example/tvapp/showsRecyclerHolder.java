package com.example.tvapp;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tvapp.models.Channel;



public class showsRecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private showsRecyclerAdapter.OnItemClickListener listener;

    public showsRecyclerHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    public void bind(Channel.Show show, showsRecyclerAdapter.OnItemClickListener listener) {
        this.listener = listener;

        TextView showNameView = itemView.findViewById(R.id.showName);
        showNameView.setText(show.getTitle());

        TextView startTimeView = itemView.findViewById(R.id.timeStart);
        startTimeView.setText("Start time " + show.getStartTime());

        TextView endTimeView = itemView.findViewById(R.id.timeEnd);
        endTimeView.setText("End time " + show.getEndTime());
    }

    @Override
    public void onClick(View v) {
        listener.onItemClick(getAdapterPosition());
    }
}