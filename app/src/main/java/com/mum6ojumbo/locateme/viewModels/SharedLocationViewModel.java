package com.mum6ojumbo.locateme.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.mum6ojumbo.locateme.model.LocationSharedByUsersEntity;
import com.mum6ojumbo.locateme.room.repositories.FirebaseDatabaseRecRepository;

import java.util.List;

public class SharedLocationViewModel extends AndroidViewModel {
    private FirebaseDatabaseRecRepository mFirebaseDBRec;
    private LiveData<List<LocationSharedByUsersEntity>> mSharedLocations;
    public  SharedLocationViewModel(Application app){
        super(app);
        mFirebaseDBRec =new FirebaseDatabaseRecRepository(app);
        mSharedLocations=mFirebaseDBRec.getHistory();
    }
    public LiveData<List<LocationSharedByUsersEntity>> getSharedLocations() {
        return mSharedLocations;
    }
}
