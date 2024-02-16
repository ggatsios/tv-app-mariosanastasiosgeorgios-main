package com.example.tvapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ChannelEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ChannelDao channelDao();
}