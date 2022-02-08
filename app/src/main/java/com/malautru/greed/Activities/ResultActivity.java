package com.malautru.greed.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.malautru.greed.Database.ArtistDatabase;
import com.malautru.greed.Database.Entities.Artist;
import com.malautru.greed.R;
import com.malautru.greed.Tools.Internet.TasteDiveAPI.TasteDiveHelper;
import com.malautru.greed.Tools.Internet.YoutubeAPI;

public class ResultActivity extends YouTubeBaseActivity {
    private String artist, baseBands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        findViewById(R.id.progress_bar).setVisibility(View.GONE);

        // User can ask not to store liked artists in settings
        final boolean keepArtist = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("keepArtists", true);

        baseBands = getIntent().getStringExtra("base_bands");
        artist = getIntent().getStringExtra("band");
        String youtubeId = getIntent().getStringExtra("youtube_id");

        // Display artist's name
        TextView artistTextView = findViewById(R.id.artist_text_view);
        artistTextView.setText(artist);

        // If the user like the artist
        findViewById(R.id.like_button).setOnClickListener(view -> {
            try {
                // Recommend another artist based on the current one
                TasteDiveHelper.getRecommendation(artist, view.getContext());

                findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);

                // If the user wants artists to be stored, insert the artist into the database
                if (keepArtist)
                    ArtistDatabase.getInstance(ResultActivity.this).getArtistDao().insert(new Artist(artist, true));
            } catch (Exception e) {
                findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);
                Toast.makeText(view.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        // If the user dislike the artist
        findViewById(R.id.dislike_button).setOnClickListener(view -> {
            try {
                // Recommend anoter artist base on the original bands the user entered
                TasteDiveHelper.getRecommendation(baseBands, view.getContext());

                findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);

                // If the user wants artists to be stored, insert the artist into the database, as a disliked artist
                if (keepArtist)
                    ArtistDatabase.getInstance(ResultActivity.this).getArtistDao().insert(new Artist(artist, false));
            } catch (Exception e) {
                findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);
                Toast.makeText(view.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        // Display a Youtube player
        final YouTubePlayerView youtubePlayerView = findViewById(R.id.youtube_player);
        try {
            // TasteDive API returns a youtube_id when available
            if (!youtubeId.equals("null"))
                playVideo(youtubeId.replace("\"", ""), youtubePlayerView);
            else // If a youtube_id is not available, use YoutubeAPI to search for a music video
                playVideo(new YoutubeAPI().execute(artist).get(), youtubePlayerView);
        } catch (Exception e) {
            // Debug only
            playVideo("G_cdKPMcjiQ", youtubePlayerView);
            e.printStackTrace();
        }
    }

    public void playVideo(final String videoId, final YouTubePlayerView youTubePlayerView) {
        //initialize youtube player view
        youTubePlayerView.initialize(getResources().getString(R.string.youtube_api_key),
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // Loads the video without playing it
                        youTubePlayer.cueVideo(videoId);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {
                        try {
                            playVideo(new YoutubeAPI().execute(artist).get(), youTubePlayerView);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    // When the user press the back button return to the home menu
    @Override
    public void onBackPressed() {
        Intent goBackHome = new Intent(this, com.malautru.greed.Activities.MainActivity.class);
        goBackHome.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(goBackHome);
    }

    // 3 dots settings menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, com.malautru.greed.Activities.SettingsActivity.class));
        Log.d("menu", "settings");
        return super.onOptionsItemSelected(item);
    }
}
