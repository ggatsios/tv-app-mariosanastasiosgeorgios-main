package com.example.tvapp;

import androidx.annotation.Nullable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.tvapp.models.Channel;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProgramDetailsActivity extends Activity {
    String channelName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        setContentView(R.layout.activity_program_details);
        Intent intent = getIntent();
        channelName = (String) intent.getSerializableExtra("ChannelName");

        if (isConnected) {
            getDataFromAPI((Channel.Show) intent.getSerializableExtra("clickedShow"));
        }
        else{
            TextView linkTextView = findViewById(R.id.linkTextView);
            Button shareButton = findViewById(R.id.shareButton);
            linkTextView.setVisibility(View.INVISIBLE);
            shareButton.setVisibility(View.INVISIBLE);
        }
    }

    private void getDataFromAPI(Channel.Show show) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://app-vpigadas.herokuapp.com/api/zapping/tv/"+channelName+"/details";

        // Create a JSONObject to hold the request parameters.
        JSONObject postData = new JSONObject();
        try {
            postData.put("title", show.getTitle());
            postData.put("startTimeCaption", show.getStartTime());
            postData.put("endTimeCaption", show.getEndTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // Create the POST request.
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println(response);
                        Gson gson = new Gson();
                        Channel.Show newShow = gson.fromJson(response.toString(), Channel.Show.class);
                        JsonElement jsonElement = JsonParser.parseString(response.toString());
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        if ( jsonObject.get("image") != null) {
                            newShow.setChannelImageUrl(jsonObject.get("image").getAsString());
                        }
                        else{
                            newShow.setChannelImageUrl("https://via.placeholder.com/1024x512&text=image1");
                        }

                        newShow.setInfoLink(jsonObject.get("link").getAsString());
                        displayData(newShow);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        TextView programTitle = findViewById(R.id.programTitle);
                        programTitle.setText("Something went wrong with the request");
                        Log.e("API Error", error.toString());
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        queue.add(request);
    }


    private void displayData(Channel.Show newShow){
        TextView programTitle = findViewById(R.id.programTitle);
        TextView programDescription = findViewById(R.id.programDescription);
        TextView startTime = findViewById(R.id.startTimeCaption);
        TextView endTime = findViewById(R.id.endTimeCaption);
        ImageView programImage = findViewById(R.id.programImage);
        TextView linkTextView = findViewById(R.id.linkTextView);
        Button shareButton = findViewById(R.id.shareButton);

        String imageUrl = newShow.getChannelImageUrl();

        programTitle.setText(newShow.getTitle());
        if (newShow.getDescription() != null || !Objects.equals(newShow.getDescription(), ""))
        {
            programDescription.setText(newShow.getDescription());
        }
        String startTimeText = getString(R.string.start_time, newShow.getStartTime());
        String endTimeText = getString(R.string.end_time, newShow.getEndTime());

        startTime.setText(startTimeText);
        endTime.setText(endTimeText);

        Glide.with(this).load(imageUrl).into(programImage);

        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(newShow.getInfoLink()));
                startActivity(intent);
            }
        });



        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Check this out!!");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "Channel: "+ channelName + "\nShow: " + newShow.getTitle() + "\nDescription: " + newShow.getDescription() + "\nStart time: " + newShow.getStartTime() + "\nEnd time: " + newShow.getEndTime() + "\nMore info: " + newShow.getInfoLink());

                ProgramDetailsActivity.this.startActivity(Intent.createChooser(sharingIntent, "share via"));
            }
        });
    }


}
