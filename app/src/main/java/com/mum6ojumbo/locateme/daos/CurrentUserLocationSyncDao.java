package com.mum6ojumbo.locateme.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.mum6ojumbo.locateme.model.CurrentUserLocationSync;
import com.mum6ojumbo.locateme.room.LocationMeRoomDatabase;

import java.util.List;

@Dao
public interface CurrentUserLocationSyncDao {
    @Insert
    public void insert(CurrentUserLocationSync aRecord);
    @Query("SELECT * FROM current_user_location_table ORDER BY share_id desc, id desc")
    public List<CurrentUserLocationSync> getAllUserSyncs();
    @Query("DELETE FROM current_user_location_table")
    public void deleteHistory();
}
