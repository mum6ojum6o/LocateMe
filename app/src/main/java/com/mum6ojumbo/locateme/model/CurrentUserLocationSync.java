package com.mum6ojumbo.locateme.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName="current_user_location_table")
@TypeConverters({DateConverter.class})
public class CurrentUserLocationSync {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @NonNull
    @ColumnInfo(name="share_id")
    public int shareId;
    @NonNull
    public double latitude;
    @NonNull
    public double longitutde;
    @NonNull
    public Date timestamp;

    public CurrentUserLocationSync(@NonNull int shareId,@NonNull double latitude,@NonNull double longitutde, @NonNull Date timestamp){
        this.shareId=shareId;
        this.latitude=latitude;
        this.longitutde=longitutde;
        this.timestamp=timestamp;
    }
    public int getShareId(){return this.shareId;}
    public double getLatitude(){return  this.latitude;}
    public double getLongitutde(){return this.longitutde;}
    public Date getTimestamp() {return this.timestamp;}
}
