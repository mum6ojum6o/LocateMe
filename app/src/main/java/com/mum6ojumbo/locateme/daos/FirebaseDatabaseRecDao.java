package com.mum6ojumbo.locateme.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.mum6ojumbo.locateme.model.LocationSharedByUsersEntity;

import java.util.List;

@Dao
public interface FirebaseDatabaseRecDao {
    @Insert
    public void insert(LocationSharedByUsersEntity aRecord);

    @Query("DELETE FROM location_shared_by_users_table")
    public void deleteAll();

    @Query("SELECT * FROM location_shared_by_users_table WHERE username LIKE :name ORDER BY timestamp DESC")
    public LiveData<List<LocationSharedByUsersEntity>> getSpecificUserActivities(String name);

    @Query("SELECT * FROM location_shared_by_users_table")
    public LiveData<List<LocationSharedByUsersEntity>> getHistory();
}
