package dev.golony.pubtrans4watch;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends WearableActivity {

    private TextView mTextView;
    private FusedLocationProviderClient fusedLocationProviderClient;

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
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        mTextView.setText(Double.toString(location.getAltitude()) + "  " + Double.toString(location.getLongitude()));
                    }
                });
    }


}
