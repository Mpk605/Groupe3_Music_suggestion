package com.malautru.greed.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.malautru.greed.R;
import com.malautru.greed.Tools.Internet.Internet;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Only start the app if internet is available
        if (Internet.isOnline()) {
            setContentView(R.layout.activity_main);

            // Main button
            findViewById(R.id.start).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FormActivity.class)));

            // Saved artists button
            findViewById(R.id.history).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HistoryActivity.class)));
        } else {
            setContentView(R.layout.activity_no_internet);

            // Button to refresh the view
            findViewById(R.id.try_again).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MainActivity.class)));
        }
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