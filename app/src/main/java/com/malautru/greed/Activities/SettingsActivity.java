package com.malautru.greed.Activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.malautru.greed.Database.ArtistDatabase;
import com.malautru.greed.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference button = getPreferenceManager().findPreference("clearHistory");
            if (button != null)
                button.setOnPreferenceClickListener(preference -> {
                    // When the user clicks on "Clear History" button, display a dialog box asking for confirmation
                    new MaterialAlertDialogBuilder(getContext()).setTitle(R.string.Warning)
                            .setMessage(R.string.confirm_delete)
                            .setCancelable(false)
                            .setPositiveButton("Yes", (dialog, which) -> {
                                Toast.makeText(getContext(), "You've choosen to delete all records", Toast.LENGTH_SHORT).show();
                                ArtistDatabase.getInstance(getContext()).getArtistDao().goodbye();
                            })
                            .setNegativeButton(R.string.cancel, (dialog, which) -> Toast.makeText(getContext(), R.string.cancel, Toast.LENGTH_SHORT).show())
                            .show();

                    // If the user clicks on "Yes", delete all entries in the database
                    // and display a toast for confirmation
                    return false;
                });
        }
    }
}