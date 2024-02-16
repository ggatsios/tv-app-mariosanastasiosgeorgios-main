package com.example.tvapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tvapp.db.AppDatabase;
import com.example.tvapp.db.InsertChannelsTask;
import com.example.tvapp.db.RetrieveChannelsTask;
import com.example.tvapp.models.Channel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ChannelAdapter.OnItemClickListener {

    RecyclerView channelsRecyclerView;
    ArrayList<Channel> channels;

    public void onChannelsRetrieved(List<Channel> channelsReceived) {
        channels = (ArrayList<Channel>) channelsReceived;
        ArrayList<Channel> channelNames = new ArrayList<>(channels);

        ChannelAdapter channelAdapter = new ChannelAdapter(channelNames,this);

        channelsRecyclerView = findViewById(R.id.channelsRecyclerView);
        channelsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        channelsRecyclerView.setAdapter(channelAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        channelsRecyclerView = findViewById(R.id.channelsRecyclerView);
        channelsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "my-database").build();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://app-vpigadas.herokuapp.com/api/zapping/tv";

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(!isConnected){//If no access to the web display message to user
            Toast.makeText(this, "You are currently offline. Please check your internet connection.", Toast.LENGTH_LONG).show();
            //Gets data from the database
            new RetrieveChannelsTask(db,this).execute();
        }
        else{
            //Access the api to get the channels
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Gson gson = new Gson();
                                JSONArray channelsArray = response.getJSONArray("channels");

                                Type channelListType = new TypeToken<ArrayList<Channel>>() {}.getType();

                                channels = gson.fromJson(channelsArray.toString(), channelListType);

                                ChannelAdapter channelAdapter = new ChannelAdapter(channels,MainActivity.this);

                                channelsRecyclerView.setAdapter(channelAdapter);
                                new InsertChannelsTask(db).execute(channels);

                            } catch (JSONException e) {
                                Log.e("error", "Error parsing JSON", e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("API Error", error.toString());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            queue.add(request);
        }
    }

    public void onItemClick(int position) {
        ArrayList<Channel.Show> clickedChannelShows = (ArrayList<Channel.Show>) channels.get(position).getShows();
        Intent intent = new Intent(MainActivity.this, ShowsActivity.class);
        intent.putExtra("clickedChannelShows", clickedChannelShows);
        intent.putExtra("ChannelName",channels.get(position).getChannelName());
        startActivity(intent);
    }
}