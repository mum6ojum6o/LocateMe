package com.mum6ojumbo.locateme.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;


import com.esri.arcgisruntime.location.LocationDataSource;
import com.mum6ojumbo.locateme.model.LocationSharedByUsersEntity;
import com.mum6ojumbo.locateme.room.repositories.FirebaseDatabaseRecRepository;
import com.mum6ojumbo.locateme.room.repositories.LocationTrackingRepository;

import java.util.List;

public class LocationTrackerViewModel extends AndroidViewModel {
    public static final String TAG="LocationTrackerVM";
    private LocationTrackingRepository mLocationTrackingRepository;

    private LiveData<LocationDataSource.Location> mLocationDataSource;

    public LocationTrackerViewModel(Application app,LocationTrackingRepository locationTrackingRepository){
        super(app);
        Log.i(TAG,"LTVM initiated");
        mLocationTrackingRepository = locationTrackingRepository;

    }


}
