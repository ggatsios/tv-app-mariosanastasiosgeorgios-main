package com.example.tvapp.db;

import android.os.AsyncTask;

import com.example.tvapp.models.Channel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class InsertChannelsTask extends AsyncTask<List<Channel>, Void, Void> {
    private AppDatabase db;

    public InsertChannelsTask(AppDatabase db) {
        this.db = db;
    }

    @SafeVarargs
    @Override
    protected final Void doInBackground(List<Channel>... lists) {
        List<Channel> channels = lists[0];
        List<ChannelEntity> entities = new ArrayList<>();
        for (Channel channel : channels) {
            String showsJson = new Gson().toJson(channel.getShows());
            ChannelEntity entity = new ChannelEntity(channel.getChannelName(), showsJson);
            entities.add(entity);
        }
        db.channelDao().insertChannels(entities);
        db.close();
        return null;
    }
}
