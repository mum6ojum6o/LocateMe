package com.mum6ojumbo.locateme.room.repositories;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.esri.arcgisruntime.location.LocationDataSource;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mum6ojumbo.locateme.MainActivity;

import java.util.concurrent.Executor;

public class LocationTrackingRepository {
    public static final String TAG="LocationTrackingRepo";
    public UpdateLocation updateLocationListener;
    private final int LOC_REQ_CODE=345;
    private Executor locationExecutor;
    private LocationDisplay mLocationDisplay;
    private LiveData<LocationDataSource> mLocationDataSource;
    private static LocationTrackingRepository INSTANCE;
    private LocationTrackingRepository(LocationDisplay locationDisplay,Context context){
        this.mLocationDisplay=locationDisplay;
        setLocationDisplay(mLocationDisplay);
        //updateLocationListener = (UpdateLocation)context;
    }

    public static LocationTrackingRepository getInstance(LocationDisplay locationDisplay, Context context){

        if(INSTANCE==null){
            synchronized (LocationTrackingRepository.class){
                INSTANCE = new LocationTrackingRepository(locationDisplay,context);

            }
        }
        Log.i(TAG,"LTVRepo initiated");
        return INSTANCE;
    }
    public void setLocationDisplay(LocationDisplay display){
        Log.i(TAG,"settingUp LocationDisplaye");
        this.mLocationDisplay = display;
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
        mLocationDisplay.startAsync();
        mLocationDisplay.addLocationChangedListener(new LocationDisplay.LocationChangedListener() {
            @Override
            public void onLocationChanged(LocationDisplay.LocationChangedEvent locationChangedEvent) {
                Log.i(TAG,"LocationChanged!!!");
                    mLocationDisplay = locationChangedEvent.getSource();

            }
        });
    }

    //public LiveData<LocationDataSource> getLocationDataSource(){return }

    public interface UpdateLocation{
        void updateLocation();
    }


    //public MutableLiveData<Location> getmCurrentLocation(){;}

}
