package com.fabianospdev.android.astrophoto;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;

public class SettingsActivity extends AppCompatActivity {

  @Override
  protected void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.settings_activity );
    getSupportFragmentManager().beginTransaction().replace( R.id.settings, new SettingsFragment() ).commit();
    ActionBar actionBar = getSupportActionBar();
    if ( actionBar != null ) {
      actionBar.setDisplayHomeAsUpEnabled( true );
    }
  }

  public static class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences( Bundle savedInstanceState, String rootKey ) {
      setPreferencesFromResource( R.xml.root_preferences, rootKey );
    }
  }
}