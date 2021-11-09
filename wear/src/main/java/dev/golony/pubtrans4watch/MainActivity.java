package dev.golony.pubtrans4watch;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.*;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends WearableActivity {

    private TextView mTextView;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location mCurrentLocation;

    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Enables Always-on
        setAmbientEnabled();
        
        // TODO: 권한이 없는 경우 권한 요청기능 추가
        // 위치 정보 가져오기
//        fusedLocationProviderClient.getLastLocation()
//                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        mCurrentLocation = location;
//                        mTextView.setText(Double.toString(location.getAltitude()) + "  " + Double.toString(location.getLongitude()));
//                    }
//                });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult == null){
                   Log.d("LOG", "onLocationResult: null");
                }

                for (Location loc : locationResult.getLocations()){
                    Log.i("INFO", "Location Updated");
                    mCurrentLocation = loc;
                    mTextView.setText(Double.toString(loc.getAltitude()) + "  " + Double.toString(loc.getLongitude()));
                }
            }
        };
    }

    @Override
    protected void onResume(){
        super.onResume();
        startLocationUpdates();
    }

    private void startLocationUpdates(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        // TODO: 권한이 없는 경우 권한 요청기능 추가
        fusedLocationProviderClient.requestLocationUpdates(locationRequest
        , locationCallback
        , Looper.getMainLooper());
    }

    @Override
    protected void onPause(){
        super.onPause();

        Log.i("INFO", "onPause: changed");

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
}
