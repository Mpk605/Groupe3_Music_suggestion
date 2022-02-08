package com.malautru.greed.Tools.Internet;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YoutubeAPI extends AsyncTask<String, Void, String> {
    private OkHttpClient client = new OkHttpClient();

    /**
     * Return video ID from band name using Youtube Web Data API
     *
     * @param strings query
     * @return String video Youtube ID
     */
    @Override
    protected String doInBackground(String... strings) {
        try {
            String artist_host = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&q=" + URLEncoder.encode(strings[0], StandardCharsets.UTF_8.name())
                    + "&topicId=%2Fm%2F04rlf&type=video&videoDimension=2d&videoEmbeddable=true&key=AIzaSyDg2njTmJWxXuSHDcnRXR_3A1_9s0LaBJY";

            Request request = new Request.Builder().url(artist_host)
                    .header("accept", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {

                HashMap result = getHashMapFromJSONString(Objects.requireNonNull(response.body()).string());

                String youtubeId = Objects.requireNonNull(((HashMap) Objects.requireNonNull(((HashMap) ((ArrayList) Objects.requireNonNull(result.get("items"))).get(0)).get("id"))).get("videoId")).toString();
                Log.d("test", youtubeId);
                return youtubeId;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private HashMap getHashMapFromJSONString(String json) throws IOException {
        return new ObjectMapper().readValue(json, HashMap.class);
    }
}
