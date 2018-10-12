package com.mum6ojumbo.locateme.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName="current_user_location_table")
@TypeConverters({DateConverter.class})
public class CurrentUserLocationSync implements Serializable{
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
    @NonNull
    public String shareWith;

    public CurrentUserLocationSync(@NonNull int shareId,@NonNull double latitude,
                                   @NonNull double longitutde, @NonNull Date timestamp,
                                   @NonNull String shareWith){
        this.shareId=shareId;
        this.latitude=latitude;
        this.longitutde=longitutde;
        this.timestamp=timestamp;
        this.shareWith=shareWith;
    }
    public int getShareId(){return this.shareId;}
    public double getLatitude(){return  this.latitude;}
    public double getLongitude(){return this.longitutde;}
    public Date getTimestamp() {return this.timestamp;}
    public String getShareWith() {return this.shareWith;}
    public void setLatitude(double latitude){this.latitude=latitude;}
    public void setLongitude(double longitude){this.longitutde=longitude;}
}
