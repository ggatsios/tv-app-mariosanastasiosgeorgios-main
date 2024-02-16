package com.example.tvapp.models;

import com.example.tvapp.db.ChannelEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Channel implements Serializable {
    private List<Show> shows = new ArrayList<>();
    private String channelName;
    private String channelImageUrl;

    public List<Show> getShows() {
        return shows;
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
    }

    public void setChannelName(String channelName){
        this.channelName = channelName;
    }

    public String getChannelName(){
        return this.channelName;
    }

    public String getChannelImageUrl() {
        return channelImageUrl;
    }

    public void setChannelImageUrl(String channelImageUrl) {
        this.channelImageUrl = channelImageUrl;
    }

    public static class Show implements Serializable {
        private String title;
        private String startTime;
        private String endTime;
        private String description;
        private String channelImageUrl;
        private String infoLink;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }


        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getDescription() {
            return description;
        }

        public String getChannelImageUrl() {
            return channelImageUrl;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setChannelImageUrl(String channelImageUrl) {
            this.channelImageUrl = channelImageUrl;
        }

        public String getInfoLink() {
            return infoLink;
        }

        public void setInfoLink(String infoLink) {
            this.infoLink = infoLink;
        }

        @Override
        public String toString() {
            return "Show{" +
                    "title='" + title + '\'' +
                    ", startTime='" + startTime + '\'' +
                    ", endTime='" + endTime + '\'' +
                    ", description='" + description + '\'' +
                    ", channelImageUrl='" + channelImageUrl + '\'' +
                    ", infoLink='" + infoLink + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Channel{" +
                "shows=" + shows +
                ", channelName='" + channelName + '\'' +
                '}';
    }

    public static Channel fromChannelEntity(ChannelEntity entity) {
        Channel channel = new Channel();
        channel.setChannelName(entity.getChannelName());
        List<Show> shows = new ArrayList<>();
        try {
            JSONArray showsJsonArray = new JSONArray(entity.getShowsJson());
            for (int i = 0; i < showsJsonArray.length(); i++) {
                JSONObject showJson = showsJsonArray.getJSONObject(i);
                Show show = new Show();
                show.setTitle(showJson.optString("title"));
                System.out.println(showJson.toString());
                show.setStartTime(showJson.optString("startTime"));
                show.setEndTime(showJson.optString("endTime"));
                shows.add(show);
            }
            channel.setShows(shows);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return channel;
    }
}