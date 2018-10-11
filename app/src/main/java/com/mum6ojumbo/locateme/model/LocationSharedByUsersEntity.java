package com.mum6ojumbo.locateme.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

@Entity(tableName = "location_shared_by_users_table")
@TypeConverters({DateConverter.class})
public class LocationSharedByUsersEntity {
//This class is used to share the logged in user's location coordinates to a selected user.
    public LocationSharedByUsersEntity(@NonNull int shareId, @NonNull String username,
                                       @NonNull double longitude, @NonNull double latitude,
                                       @NonNull Date timestamp, @NonNull boolean outgoing){
        this.username=username;
        this.shareId = shareId;
        this.longitude=longitude;
        this.latitude=latitude;
        this.timestamp=timestamp;
        this.outgoing = outgoing;
    }
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;
    @NonNull
    @ColumnInfo(name = "share_id")
    public int shareId;
    @NonNull
    public String username;
    @NonNull
    public double latitude;
    @NonNull
    public double longitude;
    @NonNull
    public Date timestamp;
    @NonNull
    public boolean outgoing;
    public boolean getOutgoing(){return this.outgoing;}
    public Date getTimestamp(){return  this.timestamp;}
    public double getLongitude(){return this.longitude;}
    public double getLatitude(){return this.latitude;}
    public String getUsername(){return this.username;}
}
