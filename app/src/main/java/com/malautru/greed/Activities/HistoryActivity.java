package com.malautru.greed.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.malautru.greed.Database.ArtistDatabase;
import com.malautru.greed.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    List<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        populateArtistList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        populateArtistList();
    }

    /**
     * Populate ListView with saved artists from database
     */
    public void populateArtistList() {
        ListView lv = findViewById(R.id.lv_history);

        // Get all liked artists from database
        listItems = ArtistDatabase.getInstance(this).getArtistDao().getSavedLikedArtists();
        // Populate ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        lv.setAdapter(adapter);
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
