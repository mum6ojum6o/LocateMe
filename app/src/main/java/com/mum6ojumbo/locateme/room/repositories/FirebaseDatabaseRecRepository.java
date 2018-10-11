package com.mum6ojumbo.locateme.room.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.mum6ojumbo.locateme.daos.CurrentUserLocationSyncDao;
import com.mum6ojumbo.locateme.daos.FirebaseDatabaseRecDao;
import com.mum6ojumbo.locateme.model.CurrentUserLocationSync;
import com.mum6ojumbo.locateme.room.LocationMeRoomDatabase;

import java.util.List;
import java.util.concurrent.Executor;

public class FirebaseDatabaseRecRepository {
    private FirebaseDatabaseRecDao fbDao;
    private CurrentUserLocationSyncDao currentUserLocationSyncDao;
    private LiveData<List<com.mum6ojumbo.locateme.model.LocationSharedByUsersEntity>> mUserAllActivites;
    private Executor executor;

    public FirebaseDatabaseRecRepository(Application application){
        LocationMeRoomDatabase db = LocationMeRoomDatabase.getDatabaseInstance(application);
        fbDao = db.firebaseDatabaseRecDao();

    }
    public void insertRecord(final com.mum6ojumbo.locateme.model.LocationSharedByUsersEntity aRecord){
        executor.execute(() -> {
            fbDao.insert(aRecord);
        });
    }
    public void insertCurrentUserLocation(final CurrentUserLocationSync userRecord){
        executor.execute(()->{
            currentUserLocationSyncDao.insert(userRecord);
        });
    }
}
