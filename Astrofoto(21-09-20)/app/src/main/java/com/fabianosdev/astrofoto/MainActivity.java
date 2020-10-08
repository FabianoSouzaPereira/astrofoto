package com.fabianosdev.astrofoto;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fabianosdev.astrofoto.Adapters.RecyclerAdapter;
import com.fabianosdev.astrofoto.ui.dashboard.DashboardFragment;
import com.fabianosdev.astrofoto.ui.home.HomeFragment;
import com.fabianosdev.astrofoto.ui.main.MainFragment;
import com.fabianosdev.astrofoto.ui.notifications.NotificationsFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.images.Size;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    Context context = MainActivity.this;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    // OTHERS //
    public String datahora;
    public static String phoneNumber = "";
    private static final String TAG = "LOG-d";
    // Locations //
    public static double latitude = 0;
    public static double longitude = 0;
    public static int phoneState = -1;
    public static final int PRIORITY_BALANCED_POWER_ACCURACY = 102;
    public static final int PRIORITY_HIGH_ACCURACY = 100;
    public static final int PRIORITY_LOW_POWER = 104;
    public static final int PRIORITY_NO_POWER = 105;
    private static int location_Priority_Request = PRIORITY_HIGH_ACCURACY;
    private static int timeRequest = 60;
    private static int timeFastInterval = 30;

    FusedLocationProviderClient client;
    GeofencingClient geofencingClient;
    LocationCallback locationCallback;

    // PERMISSIONS //
    public final int PERMISSION_CODE = 3;
    private final int Permission_All = 1;
    private final String[] Permissions = {Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private final String[] Permission = {Manifest.permission.READ_PHONE_NUMBERS};

    // CAMERA //
    public static final int CAMERA_FACING_BACK = 0;
    public static final int CAMERA_FACING_FRONT = 1;
    private BarcodeDetector barcodeDetector;
    private SurfaceView cameraView;
    private CameraSource cameraSource;
    private int cameraside;
    private Size sizeCamera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
        //Creating a bottom menu listening
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_menu);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        client = LocationServices.getFusedLocationProviderClient(this);
        geofencingClient = LocationServices.getGeofencingClient(this);

     //  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();
      getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();


    }

    /* switching bottom menu fragments */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.navigation_dashboard:
                            selectedFragment = new DashboardFragment();
                            break;
                        case R.id.navigation_main:
                            selectedFragment = new MainFragment();
                            break;
                        case R.id.navigation_notifications:
                            selectedFragment = new NotificationsFragment();
                            break;
                    }
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                    return true;
                }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent config = new Intent( MainActivity.this, ClienteConfig.class );
            startActivity( config );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onResume() {
        super.onResume();
       // datahora();
        if (!hasPhonePermissions( this, Permissions )) {
            ActivityCompat.requestPermissions( this,Permissions,Permission_All );
        }
        int errorcode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable( MainActivity.this );
        switch (errorcode) {
            case ConnectionResult.SERVICE_MISSING:
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
            case ConnectionResult.SERVICE_DISABLED:
                Log.i( "Teste", "Show dialog =======" );
                GoogleApiAvailability.getInstance().getErrorDialog( MainActivity.this, errorcode, 0, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        finish();
                    }
                } ).show();
                break;
            case ConnectionResult.SUCCESS:
                Log.i( "Teste", "Google Play Services up-to-date =======" );
                break;
        }
        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        client.getLastLocation().addOnSuccessListener( new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.i("Location: ", ""+location.getLatitude());
                    Log.i("Longitude: ","" + location.getLongitude());
                    Log.i("Bearing: ","" + location.getBearing());
                    Log.i("Altitude: ","" + location.getAltitude());
                    Log.i("Speed: ","" + location.getSpeed());
                    Log.i("Provider: ","" + location.getProvider());
                    Log.i("Accuracy: ","" + location.getAccuracy());
                    Log.i("Hora: ","" + DateFormat.getTimeInstance().format( new Date() ) + "  ======= " );
                } else {
                    Log.i( "location - ", "null" );
                }
            }
        } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        } );
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval( timeRequest * 1000 );
        locationRequest.setFastestInterval( timeFastInterval * 1000 );
        locationRequest.setPriority( location_Priority_Request ); //uso preciso com gps.
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest( locationRequest );
        SettingsClient settingsClient = LocationServices.getSettingsClient( this );
        settingsClient.checkLocationSettings( builder.build() ).addOnSuccessListener( new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.i( "teste", locationSettingsResponse.getLocationSettingsStates().isNetworkLocationPresent() + "" );
            }
        } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult( MainActivity.this, 10 );
                    } catch (IntentSender.SendIntentException el) {
                        Log.i("Info", "Catch -> " + el);
                    }
                }
            }
        } );
        //Listener pega sempre a nova posição do provider. Kill app se local é nulo.
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.i( "Local position -> ", "local is null" );
                    return;
                }
                //procurar na lista de localição.
                for (Location location : locationResult.getLocations()) {
                    Log.i( "Location pos -> ", location.getLatitude() + " " + location.getLongitude() );
                }
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                Log.i( "locationAvailability : ", locationAvailability.isLocationAvailable() + "" );
            }
        };
        client.requestLocationUpdates( locationRequest, locationCallback, null );

        /* ------ CAMERA FEATURES ------ */
        if(cameraSource != null) {
            //Todo criar listening para iniciar a camera -> public CameraSource start ()
            cameraside = cameraSource.getCameraFacing();    //Returns the selected camera; one of CAMERA_FACING_BACK or CAMERA_FACING_FRONT.
            sizeCamera = cameraSource.getPreviewSize();     //Returns the preview size that is currently in use by the underlying camera.
        }
        //end RESUME
    }

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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Permissão aceita", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Permissão negada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(cameraSource != null) {
            cameraSource.release(); //Stops the camera and releases the resources of the camera and underlying detector.
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(cameraSource != null) {
            cameraSource.stop(); //Closes the camera and stops sending frames to the underlying frame detector.
        }
    }

//    public void datahora(){
//        SimpleDateFormat formataData = new SimpleDateFormat(getString(R.string._datahota));
//        Date data = new Date();
//        datahora = formataData.format(data);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if(cameraSource != null) {
            cameraSource.stop();
            cameraSource.release();
            cameraSource = null;
        }

        super.onDestroy();
    }
}