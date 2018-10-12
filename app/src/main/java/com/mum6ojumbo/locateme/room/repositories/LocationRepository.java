package com.mum6ojumbo.locateme.room.repositories;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mum6ojumbo.locateme.room.AppExecutor;

import java.util.concurrent.Executor;


public class LocationRepository implements GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = "LocRepo";
    private Executor locationExecutor;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private Location mDevLocation;
    public static LocationRepository INSTANCE;
    private Context mContext;
    private UpdateLocation listener;
    private LocationRepository(Context context){
        this.mContext=context;
        listener = (UpdateLocation)context;
        //init();
    }
    public void init(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        if(mGoogleApiClient == null){
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addApi(LocationServices.API)
                    .build();
        }
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location aLocation : locationResult.getLocations()) {
                    //update UI
                    Log.i(TAG, "from Location Callback");
                    Log.i(TAG,mDevLocation.getLongitude()+" "+mDevLocation.getLatitude());
                    mDevLocation = aLocation;
                    listener.getCoordinates();
                }
            }
        };
        getLastLocation();
        createLocationRequest();
        startLocationUpdates();
    }

    public static LocationRepository getINSTANCE(Context context){
        if(INSTANCE==null){
            INSTANCE=new LocationRepository(context);
        }
        return INSTANCE;
    }

    protected void createLocationRequest(){
        Log.i("MainAct","Creating Location Request");
        // if(ContextCompat.checkSelfPermission(MainActivity.this,"android.permission.ACCESS_FINE_LOCATION")==PackageManager.PERMISSION_GRANTED) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        /*}
        else
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{"android.permission.ACCESS_FINE_LOCATION"},LOC_REQ_CODE);
    */
    }
    public void getLastLocation(){
        Log.i(TAG,"gettingLastLocation");
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(AppExecutor.getInstance().getDiskIO(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                mDevLocation=location;
                                Log.i(TAG,mDevLocation.getLongitude()+" "+mDevLocation.getLatitude());
                                //pointMapMarker(mCurrentLocation);
                            }
                        }
                    });
        }catch (SecurityException e ){e.printStackTrace();}
    }

    private void startLocationUpdates(){
        if(ContextCompat.checkSelfPermission(mContext,"android.permission.ACCESS_FINE_LOCATION")== PackageManager.PERMISSION_GRANTED) {
            Log.i("MainAct","Location Updates Started!");
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

        }
        else
            Log.i(TAG,"Permission not granted!!");

    }

    public void stopLocationUpdates(){
        Log.i("MainAct","Stopping Location Services");
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);

    }
    public Location getCoordinates(){
        return mDevLocation;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG,"Connection with Play Services Failed!");
    }
    public interface UpdateLocation{
        //Point getCoordinates();
        void getCoordinates();
    }
}
