package com.mum6ojumbo.locateme.viewModels;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.mum6ojumbo.locateme.room.repositories.LocationTrackingRepository;

public class LocationViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private LocationDisplay mLocationDisplay;
    private LocationTrackingRepository mLocationTrackingRepo;
    private Application mApp;
    public LocationViewModelFactory(Application app, LocationDisplay display, Context context){
        this.mLocationTrackingRepo=LocationTrackingRepository.getInstance(display,context);
    }
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T)new LocationTrackerViewModel(mApp,mLocationTrackingRepo);
    }
}
