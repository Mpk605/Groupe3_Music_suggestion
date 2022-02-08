package com.malautru.greed.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.malautru.greed.R;
import com.malautru.greed.Tools.Internet.TasteDiveAPI.TasteDiveHelper;
import com.malautru.greed.Tools.Internet.TypeAheadAPI;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class FormActivity extends AppCompatActivity {
    TypeAheadAPI typeAheadAPI;

    // Text inputs
    AutoCompleteTextView fav1, fav2, fav3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        typeAheadAPI = TypeAheadAPI.getInstance();

        fav1 = findViewById(R.id.fav1);
        fav1.addTextChangedListener(new BandTextWatcher(fav1));
        fav1.setOnItemClickListener((adapterView, view, i, l) -> {
            // When the user clicks on a name, the list disappears
            fav1.dismissDropDown();

            // Changes focus to the next textfield
            fav2.requestFocus();
        });

        fav2 = findViewById(R.id.fav2);
        fav2.addTextChangedListener(new BandTextWatcher(fav2));
        fav2.setOnItemClickListener((adapterView, view, i, l) -> {
            fav2.dismissDropDown();
            fav3.requestFocus();
        });

        fav3 = findViewById(R.id.fav3);
        fav3.addTextChangedListener(new BandTextWatcher(fav3));
        fav3.setOnItemClickListener((adapterView, view, i, l) -> {
            fav3.dismissDropDown();
            fav3.clearFocus();

            // Dismiss keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(fav3.getWindowToken(), 0);
        });

        findViewById(R.id.continue_button).setOnClickListener(v -> {
            try {
                // If none of the fields are empty
                if (!fav1.getText().toString().equals("") &&
                        !fav2.getText().toString().equals("") &&
                        !fav3.getText().toString().equals("")) {

                    // Display the progress bar
                    findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);

                    // Ask API for a recommendation
                    TasteDiveHelper.getRecommendation(String.valueOf(fav1.getText()) + ',' + fav2.getText() + ',' + fav3.getText(), v.getContext());
                } else {
                    // if the field is empty, display an error message

                    if (fav1.getText().toString().equals(""))
                        fav1.setError("This field can't be empty");

                    if (fav2.getText().toString().equals(""))
                        fav2.setError("This field can't be empty");

                    if (fav3.getText().toString().equals(""))
                        fav3.setError("This field can't be empty");
                }
            } catch (Exception e) {
                e.printStackTrace();

                // Hide progress bar
                findViewById(R.id.progress_bar).setVisibility(View.GONE);

                // Display a toast "error"
                Toast.makeText(FormActivity.this, "An error occured, please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 3 dots settings menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, SettingsActivity.class));
        Log.d("menu", "settings");
        return super.onOptionsItemSelected(item);
    }

    // Go back to the main menu when the back button is pressed
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    // Display suggestions
    class BandTextWatcher implements TextWatcher {
        private final AutoCompleteTextView textView;

        BandTextWatcher(AutoCompleteTextView textView) {
            this.textView = textView;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
            new Thread(() -> {

                try {
                    // Ask Gnoosic typeahead api for suggestions (eg. user typed: "mi" -> API suggests: "Michael Jackson")
                    final String[] suggestions = typeAheadAPI.getTypeAheadSuggestion(String.valueOf(charSequence));

                    // Only display dropdown if the API found suggestions
                    if (suggestions.length > 0 && !suggestions[0].equals(""))
                        runOnUiThread(() -> {
                            if (textView.isFocused()) {
                                // Populate dropdown with suggestions
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(FormActivity.this,
                                        android.R.layout.simple_dropdown_item_1line, suggestions);

                                // Show dropdown on the textfield
                                textView.setAdapter(adapter);
                                textView.showDropDown();
                            } else {
                                textView.setAdapter(null);
                            }
                        });
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

            }).start();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
