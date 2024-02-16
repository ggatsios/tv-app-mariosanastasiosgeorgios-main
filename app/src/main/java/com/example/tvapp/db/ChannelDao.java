package com.example.tvapp.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChannelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChannel(ChannelEntity channel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChannels(List<ChannelEntity> channelEntities);

    @Query("SELECT * FROM channels")
    List<ChannelEntity> getAllChannels();

    @Query("SELECT * FROM channels WHERE channelName = :channelName")
    ChannelEntity getChannelByName(String channelName);
}