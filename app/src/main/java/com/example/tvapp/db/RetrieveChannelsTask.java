package com.example.tvapp.db;

import android.os.AsyncTask;

import com.example.tvapp.MainActivity;
import com.example.tvapp.models.Channel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class RetrieveChannelsTask extends AsyncTask<Void, Void, List<Channel>> {

    private final AppDatabase db;
    private final WeakReference<MainActivity> activityRef;

    public RetrieveChannelsTask(AppDatabase db,MainActivity mainActivity) {
        this.db = db;
        this.activityRef = new WeakReference<>(mainActivity);
    }

    @Override
    protected List<Channel> doInBackground(Void... voids) {
        List<ChannelEntity> entities = db.channelDao().getAllChannels();
        List<Channel> channels = new ArrayList<>();
        for (ChannelEntity entity : entities) {
            Channel channel = Channel.fromChannelEntity(entity);
            channels.add(channel);
        }
        return channels;
    }

    @Override
    protected void onPostExecute(List<Channel> channels) {
        MainActivity activity = activityRef.get();
        if (activity != null) {
            activity.onChannelsRetrieved(channels);
        }
    }
}