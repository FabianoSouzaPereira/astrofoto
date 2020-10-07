package com.fabianospdev.android.astrophoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_dashboard );
        init();
    }

    private void init() {
        BottomNavigationView bottomNavigationView = findViewById( R.id.bottom_navigation );
        bottomNavigationView.setSelectedItemId( R.id.dashboard );
        bottomNavigationView.setOnNavigationItemSelectedListener( new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( @NonNull MenuItem menuItem ) {

                switch (menuItem.getItemId()){

                case R.id.dashboard:
                    return true;

                case R.id.home:
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    overridePendingTransition( 0,0 );
                    return true;

                case R.id.camera:
                    startActivity(new Intent(getApplicationContext(), CameraActivity.class));
                    overridePendingTransition( 0,0 );
                    return true;

                case R.id.notifications:
                    startActivity(new Intent(getApplicationContext(), NotificationActivity.class ));
                    overridePendingTransition( 0,0 );
                    return true;
                }
                return false;
            }

        } );
    }
}