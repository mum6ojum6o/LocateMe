package com.mum6ojumbo.locateme.model;

public class FirebaseDataRecordUserSyncModel {
        public String email;
        public String username;
        public double latitude,longitude;
        public double timestamp;
        public int shareId;

        public FirebaseDataRecordUserSyncModel(String name, double lon, double lat, double date, int shareId){
            this.username = name;
            this.longitude=lon;
            this.latitude=lat;
            timestamp=date;
            shareId = shareId;
        }

}
