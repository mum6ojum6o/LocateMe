package com.mum6ojumbo.locateme.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.esri.arcgisruntime.geometry.Point;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mum6ojumbo.locateme.model.CurrentUserLocationSync;
import com.mum6ojumbo.locateme.model.FirebaseDataRecordUserSyncModel;
import com.mum6ojumbo.locateme.model.UserDataModel;
import com.mum6ojumbo.locateme.room.repositories.LocationRepository;
import com.mum6ojumbo.locateme.room.repositories.LocationTrackingRepository;

import java.util.Date;

public class SyncService extends Service implements LocationRepository.UpdateLocation {
    private static final String TAG="SyncService";
    private LocationRepository locationRepository;
    private CurrentUserLocationSync mRecordToBeUploaded;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabase;
    private int startId;
    public SyncService() {
        super();

    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        Log.i(TAG,"starting loction listening service");
        Bundle b = intent.getExtras();
        locationRepository = LocationRepository.getINSTANCE(this);
        locationRepository.init();

        mRecordToBeUploaded= (CurrentUserLocationSync)b.getSerializable("CurrentCustomerDetails");
        Log.i(TAG,"sharing with:"+mRecordToBeUploaded.getShareWith());
        this.startId=startId;
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
    public void goOnline(){
        Log.i(TAG,"about to upload!!");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //Log.i(TAG,mCurrentLocation.getLongitude()+" "+mCurrentLocation.getLatitude());
        Query getSharedUserId=mDatabase.child("Users").orderByChild("mEmail")
                .equalTo(mRecordToBeUploaded.getShareWith());

        getSharedUserId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i(TAG,""+dataSnapshot.getChildrenCount());
                for(DataSnapshot singleDataSnapShot: dataSnapshot.getChildren()){
                    UserDataModel aRequest = singleDataSnapShot.getValue(UserDataModel.class);
                    Log.i(TAG,aRequest.getEmail());
                    if(aRequest!=null ){
                        //fetch user details
                        pushToDatabase(aRequest.getgUid());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    void pushToDatabase(String sharedWIthId){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Date aDate = new Date();
        FirebaseDataRecordUserSyncModel aRec=
                new  FirebaseDataRecordUserSyncModel(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                        mRecordToBeUploaded.getLongitude(),mRecordToBeUploaded.getLatitude(),
                        aDate.getTime(),mRecordToBeUploaded.getShareId());
        mDatabase.child("LocationsHistory/"+sharedWIthId)
                .child("Sharing").push().setValue(aRec);

    }
    @Override
    public void getCoordinates() {
        Location currentLocation=null;
        if(mRecordToBeUploaded!=null) {
            currentLocation = locationRepository.getCoordinates();
            mRecordToBeUploaded.setLongitude(currentLocation.getLongitude());
            mRecordToBeUploaded.setLatitude(currentLocation.getLatitude());
            goOnline();
            if(new Date().getTime()>=mRecordToBeUploaded.getTimestamp().getTime()){
                stopService(startId);
            }
        }


    }
    public void stopService(int startId){
        Log.i(TAG,"Stopping Service!!");
        /*if(new Date().getTime()< mRecordToBeUploaded.getTimestamp().getTime()){
            */locationRepository.stopLocationUpdates();
            stopSelf(startId);
       // }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i(TAG,"onDestory");
        locationRepository.stopLocationUpdates();
    }
}
