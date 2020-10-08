package com.fabianospdev.android.astrophoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fabianospdev.android.astrophoto.Adapters.RecyclerAdapter;
import com.fabianospdev.android.astrophoto.model.Photo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

import com.fabianospdev.android.astrophoto.database.Database;


public class MainActivity extends AppCompatActivity{
    public static String tag = "0";
    Context context = MainActivity.this;
    Database db = new Database(context);
    private int REQUEST_CODE_PERMISSIONS = 1001;
    public static final int  Permission_All = 1;
    public static final int PERMISSION_CODE = 3;
    public static final String[] Permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    /* RecyclerView */
    private RecyclerView recyclerView;
    private RecyclerAdapter mAdapter;
    RelativeLayout relativeLayout;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Photo> photos;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        listPhotos();

    }

    /** Load devices list from DB */
    void listPhotos() {
        final ArrayList<Photo>  photos = new ArrayList<>();

        try {
            List<Photo> pt = db.listAllPhotos();
            photos.addAll( pt );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecyclerAdapter(photos);

    }

    private void init() {
        BottomNavigationView bottomNavigationView = findViewById( R.id.bottom_navigation );
        bottomNavigationView.setSelectedItemId( R.id.home );
        bottomNavigationView.setOnNavigationItemSelectedListener( new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( @NonNull MenuItem menuItem ) {

                switch (menuItem.getItemId()){

                case R.id.home:
                    return true;
                case R.id.dashboard:
                    startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
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
        relativeLayout = findViewById( R.id.relativeLayOut );
        recyclerView = findViewById( R.id.recyclerView );
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        getMenuInflater().inflate( R.menu.menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        if(item.getItemId() == R.id.action_settings){
            Intent config = new Intent( MainActivity.this, SettingsActivity.class );
            startActivity( config );
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!hasPhonePermissions( this, Permissions )) {
            ActivityCompat.requestPermissions( this,Permissions,Permission_All );
        }
    }

    @SuppressLint("InlinedApi")
    private static boolean hasPhonePermissions(Context context, String... permissions) {
        if(context != null && permissions != null){
            for(String permission: permissions){
                if(ActivityCompat.checkSelfPermission( context, permission ) != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_CODE){
            if(grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText( MainActivity.this, "Permission allowed" , Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText( MainActivity.this, "Permission denied" , Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}