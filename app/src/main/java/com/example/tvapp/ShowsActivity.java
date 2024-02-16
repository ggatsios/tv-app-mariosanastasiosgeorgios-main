package com.example.tvapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvapp.models.Channel;

import java.util.ArrayList;

public class ShowsActivity extends Activity implements showsRecyclerAdapter.OnItemClickListener {

    ArrayList<Channel.Show> shows;
    String channelName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shows_activity);

        Intent intent = getIntent();
        channelName =  (String) intent.getSerializableExtra("ChannelName");

        shows =  (ArrayList<Channel.Show>) intent.getSerializableExtra("clickedChannelShows");

        RecyclerView recyclerView = findViewById(R.id.showsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new showsRecyclerAdapter(shows,this));

    }

    public void onItemClick(int position) {
        Channel.Show clickedShow = shows.get(position);
        Intent intent = new Intent(ShowsActivity.this, ProgramDetailsActivity.class);
        intent.putExtra("clickedShow", clickedShow);
        intent.putExtra("ChannelName",channelName);
        startActivity(intent);
    }
}
