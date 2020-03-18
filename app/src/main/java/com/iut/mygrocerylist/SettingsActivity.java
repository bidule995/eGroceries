package com.iut.mygrocerylist;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private final GroceryDatabase db = new GroceryDatabase(this);
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Theme.getTheme(this));
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        TextView tx = findViewById(R.id.resetSuggestions);
        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder confirmResetSuggestions = new AlertDialog.Builder(SettingsActivity.this);
                confirmResetSuggestions.setMessage(getString(R.string.confirm_reset));
                confirmResetSuggestions.setPositiveButton(R.string.yes_reset, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogI, int which) {
                        Toast.makeText(getApplicationContext(), getString(R.string.suggestions_reset_confirmed), Toast.LENGTH_SHORT).show();
                        db.resetSuggestions();
                    }
                });
                confirmResetSuggestions.setNegativeButton(R.string.no_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogI, int which) {
                        Toast.makeText(getApplicationContext(), getString(R.string.suggestions_reset_canceled), Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialogconfirmResetSuggestions = confirmResetSuggestions.create();
                dialogconfirmResetSuggestions.show();
            }
        });
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                recreate();
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(134679852, returnIntent);
        finish();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}