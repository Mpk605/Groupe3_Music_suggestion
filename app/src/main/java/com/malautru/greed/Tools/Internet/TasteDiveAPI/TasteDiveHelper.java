package com.malautru.greed.Tools.Internet.TasteDiveAPI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.malautru.greed.Activities.ResultActivity;
import com.malautru.greed.Database.ArtistDatabase;
import com.malautru.greed.Tools.Internet.HTTPRequestHandler;

import java.util.concurrent.ExecutionException;

import okhttp3.Request;

public class TasteDiveHelper {
    private static final String API_KEY = "";

    // Ask TastDive.com API for recommendation, based on the artists the user entered
    public static void getRecommendation(String query, Context context) throws ExecutionException, InterruptedException {
        String uri = Uri.parse("https://tastedive.com/api/similar")
                .buildUpon()
                .appendQueryParameter("q", query)
                .appendQueryParameter("type", "music")
                .appendQueryParameter("info", "1")
                .appendQueryParameter("limit", "20")
                .appendQueryParameter("k", API_KEY)
                .build().toString();

        String[] result = new HTTPRequestHandler().execute(
                new Request.Builder()
                        .url(uri)
                        .build()).get().split("\n");

        JsonArray jsonArray = new JsonParser().parse(result[0]).getAsJsonObject().get("Similar").getAsJsonObject().get("Results").getAsJsonArray();

        String[] output = TasteDiveHelper.readResponse(jsonArray, context);

        // Display results
        Intent resultIntent = new Intent(context, ResultActivity.class);
        resultIntent.putExtra("base_bands", query);
        resultIntent.putExtra("band", output[0]);
        resultIntent.putExtra("youtube_id", output[1]);

        context.startActivity(resultIntent);
    }

    // Extracts useful fields from JSON response
    public static String[] readResponse(JsonArray jsonArray, Context context) {
        for (JsonElement jsonResponse : jsonArray) {
            JsonObject jsonObject = jsonResponse.getAsJsonObject();

            if (ArtistDatabase.getInstance(context).getArtistDao().doesArtistExist(jsonObject.get("Name").toString()) == null) {
                return new String[]{jsonObject.get("Name").toString(), jsonObject.get("yID").toString()};
            }
        }

        return new String[]{};
    }
}
