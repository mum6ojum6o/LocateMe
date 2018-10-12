package com.mum6ojumbo.locateme;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.arcgisruntime.location.AndroidLocationDataSource;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.SceneView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mum6ojumbo.locateme.room.repositories.LocationTrackingRepository;
import com.mum6ojumbo.locateme.services.SyncService;
import com.mum6ojumbo.locateme.viewModels.LocationTrackerViewModel;
import com.mum6ojumbo.locateme.viewModels.LocationViewModelFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,AdditionalDetailsDialog.UpdateContainer{
    public static final String TAG="LocateMe";
    public static int shareId=11;
    public DrawerLayout mDrawerLayout;
    GoogleMap mGoogleMap;
    //private SupportMapFragment mapFragment;
    private DatabaseReference mDatabase;
    private static int share_id = 1;
    private FloatingActionButton mMyLocation,mTransmitLocation;
    //private Button mFetchLocBtn,mStartTransmittingBtn,mStoptransmitting;
    private Intent mServiceStartingIntent;
    private final int LOC_REQ_CODE=345;
    private int REQUEST_CHECK_SETTINGS = 543;
    private Boolean mRequestingLocationUpdates=false;
    private Boolean mSharingLocation=false;
    private TextView textViewLon,textViewLat;
    private FirebaseUser firebaseUser;
    private MapView mMapView;
    private LocationDisplay mLocationDisplay;
    private LocationTrackerViewModel mLocationTrackerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState !=null)
            updateSavedState(savedInstanceState);

        setContentView(R.layout.activity_main_drawer_included);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        switch(menuItem.getItemId())
                        {
                            case R.id.username:
                                Toast.makeText(getApplicationContext(),"You selected Username:",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.share:
                                Toast.makeText(getApplicationContext(),"You selected share option:",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.signout_menu_item:
                                signOutUser();
                                break;
                        }
                        return true;
                    }
                }
        );

        Toolbar toolbar = findViewById(R.id.toolbar);
        //toolbar.bringToFront();
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black);
        toolbar.setVisibility(View.VISIBLE);


       mMyLocation = (FloatingActionButton)findViewById(R.id.fab_my_location);
       mTransmitLocation = (FloatingActionButton)findViewById(R.id.fab_transmission);
       mMyLocation.setOnClickListener(this);
       mTransmitLocation.setOnClickListener(this);
       //mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        //if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") ==PackageManager.PERMISSION_GRANTED) {

       mMapView = findViewById(R.id.mapView);
       setupMap();
       setupLocationDisplay();
       mLocationTrackerViewModel = ViewModelProviders.of(this,
               new LocationViewModelFactory(this.getApplication(),mLocationDisplay,null))
               .get(LocationTrackerViewModel.class);
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.i("MainAct","In onResume");
        /*if(!mGoogleApiClient.isConnected()){
            //mGoogleApiClient.connect();
        }*/
        if(ContextCompat.checkSelfPermission(this,"android.permission.ACCESS_FINE_LOCATION")==PackageManager.PERMISSION_GRANTED) {
            mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
            mLocationDisplay.startAsync();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        //mGoogleApiClient.disconnect();
        //stopLocationUpdates();
        mLocationDisplay.stop();
    }
    @Override
    protected void onStop(){
        super.onStop();
   }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.fab_my_location:
                getLastLocation();
                break;
            case R.id.fab_transmission:
                if(!mSharingLocation)
                    loadSyncDetailsFragment();
                else {
                    updateMembers();
                    stopService(mServiceStartingIntent);
                }
                //mLocationDisplay.stop();
                //finish();
                //takeAppropriateAction();
                break;
        }
    }

    private void takeAppropriateAction(){
        if(mRequestingLocationUpdates){
            //stopLocationUpdates();
            Toast.makeText(this,"Stopping Location Updates",Toast.LENGTH_SHORT).show();
            mTransmitLocation.setBackgroundTintList(getResources().getColorStateList(R.color.green));
        }
        else{
            //startLocationUpdates();
            Toast.makeText(this,"Starting Location Updates",Toast.LENGTH_SHORT).show();
            //mTransmitLocation.setBackgroundColor(getResources().getColor(R.color.red));
            mTransmitLocation.setBackgroundTintList(getResources().getColorStateList(R.color.red));
        }
    }
    public void signOutUser(){
        //stopLocationUpdates();
        FirebaseAuth.getInstance()
                .signOut();
        AuthenticationActivity.getGoogleSignInClient().signOut();
        //mGoogleApiClient.disconnect();
        startActivity(new Intent(MainActivity.this,AuthenticationActivity.class));

    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] result ){
        switch(requestCode){
            case LOC_REQ_CODE:
                if(permissions.length>0 && result[0]==PackageManager.PERMISSION_GRANTED){
                    //getLastLocation();
                    Log.i(TAG,"permission granted!");
                    //mLocationDisplay.startAsync();
                    //stopLocationUpdates();
                }
                else{
                    Log.i("MainAct","Location Services denied!!");
                    Toast.makeText(this,"Please grant this app access to your Location Services!",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public void getLastLocation(){
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putBoolean("LOCATION_UPDATES_STATUS",mRequestingLocationUpdates);
        super.onSaveInstanceState(outState);
    }
    protected void updateSavedState(Bundle savedInstanceState){
        if(savedInstanceState.containsKey("LOCATION_UPDATES_STATUS"))
            mRequestingLocationUpdates = savedInstanceState.getBoolean("LOCATION_UPDATES_STATUS");
    }


    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG,"MapReady");
        mGoogleMap=googleMap;
        //updateMarker();
    }*/


    public void pointMapMarker(Location aLocation){
        LatLng position=null;
        if (aLocation != null) {
            position = new LatLng(aLocation.getLatitude(), aLocation.getLongitude());
            mGoogleMap.addMarker(new MarkerOptions().position(position).title("Your current Location!"));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15.0f));
        }
        else{
            Log.i(TAG,"aLocation was null");
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        return super.onOptionsItemSelected(item);
    }
    public void loadSyncDetailsFragment(){
        /*FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment syncDetailsFragment = new SyncDetailsFragment();
        fragmentTransaction.add(R.id.sync_details_fragment,syncDetailsFragment);
        fragmentTransaction.commit();*/
        //bundle.putSerializable("LocationRepo:",mLocationDisplay);
        DialogFragment dialogFragment = new AdditionalDetailsDialog();
        dialogFragment.show(getSupportFragmentManager(),"additional details");

    }
    private void setupMap() {
        mLocationDisplay=mMapView.getLocationDisplay();
        Basemap.Type basemapType = Basemap.Type.STREETS_VECTOR;
        double latitude = 34.05293;
        double longitude = -118.24368;
        int levelOfDetail = 11;
       /* if(mCurrentLocation!=null){
            latitude = mCurrentLocation.getLatitude();
            longitude = mCurrentLocation.getLongitude();
        }*/

        if (mMapView != null) {
            ArcGISMap map = new ArcGISMap(basemapType, latitude, longitude, levelOfDetail);
            mMapView.setMap(map);
            //mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
            //mLocationDisplay.startAsync();
        }else{
            Toast.makeText(this,"Unable to initialize MapView",Toast.LENGTH_SHORT).show();
        }

    }
    public void setupLocationDisplay(){
        mLocationDisplay=mMapView.getLocationDisplay();
        mLocationDisplay.addDataSourceStatusChangedListener(new LocationDisplay.DataSourceStatusChangedListener() {
            @Override
            public void onStatusChanged(LocationDisplay.DataSourceStatusChangedEvent dataSourceStatusChangedEvent) {

                // If LocationDisplay started OK or no error is reported, then continue.
                if (dataSourceStatusChangedEvent.isStarted() || dataSourceStatusChangedEvent.getError() == null) {
                    return;
                }

                int requestPermissionsCode = 2;
                String[] requestPermissions = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

                // If an error is found, handle the failure to start.
                // Check permissions to see if failure may be due to lack of permissions.
                if (!(ContextCompat.checkSelfPermission(MainActivity.this, requestPermissions[0]) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(MainActivity.this, requestPermissions[1]) == PackageManager.PERMISSION_GRANTED)) {
                    // If permissions are not already granted, request permission from the user.
                    ActivityCompat.requestPermissions(MainActivity.this, requestPermissions, requestPermissionsCode);
                } else {
                    // Report other unknown failure types to the user - for example, location services may not
                    // be enabled on the device.
                    String message = String.format("Error in DataSourceStatusChangedListener: %s", dataSourceStatusChangedEvent
                            .getSource().getLocationDataSource().getError().getMessage());
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }
        });
        //mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
        //mLocationDisplay.startAsync();
    }


    public void updateButtons(boolean sharingLocation){
        if(sharingLocation){
            //mTransmitLocation.setEnabled(false);
            //mTransmitLocation.setBackgroundColor(getResources().getColor(R.color.red));
            mTransmitLocation.
                    setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red,null)));
        }else{
            Intent intent = new Intent(MainActivity.this, SyncService.class);
            stopService(intent);
            mTransmitLocation.setEnabled(true);
        //    mTransmitLocation.setBackgroundColor(getResources().getColor(R.color.green));
            mTransmitLocation.
                    setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green,null)));
        }
    }

    @Override
    public void updateMembers() {
        mSharingLocation=!mSharingLocation;
        updateButtons(mSharingLocation);

    }

    @Override
    public void storeServiceStartingIntent(Intent serviceStartingIntent) {
        mServiceStartingIntent =serviceStartingIntent;
    }
}


