package com.example.sunchen.calendarmi.Service;
//import com.google.android.libraries.places.api.Places;

import android.app.Service;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.widget.Toast;

//import com.google.android.libraries.places.api.model.Place;

public class NotificationService extends Service {
    public NotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("create notification service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void getLocation() {
        LocationListener locationListener;
        LocationManager locationManager;
////        Place place = PlacePicker.getPlace(data, this);
////        String toastMsg = String.format("Place: %s", place.getName());
//        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
    }
}
