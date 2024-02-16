package com.example.tvapp.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "channels")
public class ChannelEntity {

    @PrimaryKey
    @NonNull
    private String channelName;
    private String showsJson;

    public ChannelEntity(String channelName, String showsJson) {
        this.channelName = channelName;
        this.showsJson = showsJson;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getShowsJson() {
        return showsJson;
    }

    public void setShowsJson(String showsJson) {
        this.showsJson = showsJson;
    }
}