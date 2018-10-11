package com.mum6ojumbo.locateme.room;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.mum6ojumbo.locateme.daos.CurrentUserLocationSyncDao;
import com.mum6ojumbo.locateme.daos.FirebaseDatabaseRecDao;
import com.mum6ojumbo.locateme.model.CurrentUserLocationSync;
import com.mum6ojumbo.locateme.model.LocationSharedByUsersEntity;

@Database(entities = {LocationSharedByUsersEntity.class, CurrentUserLocationSync.class},version = 1)
public abstract class LocationMeRoomDatabase extends RoomDatabase{
    public abstract FirebaseDatabaseRecDao firebaseDatabaseRecDao();
    public abstract CurrentUserLocationSyncDao currentUserLocationSyncDao();
    private static LocationMeRoomDatabase INSTANCE;

    public static LocationMeRoomDatabase getDatabaseInstance(final Context context){
        if(INSTANCE==null){
            synchronized (LocationMeRoomDatabase.class){
                if(INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocationMeRoomDatabase.class,"locate_me_database").build();

                }
            }
        }
        return INSTANCE;
    }
}
