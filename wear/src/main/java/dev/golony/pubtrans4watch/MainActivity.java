package dev.golony.pubtrans4watch;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.*;
import com.google.android.gms.tasks.OnSuccessListener;
import dev.golony.pubtrans4watch.api.TopisHelper;
import dev.golony.pubtrans4watch.db.position.Position;
import dev.golony.pubtrans4watch.db.position.PositionDatabase;
import dev.golony.pubtrans4watch.view.ArrivalInfoAdaptor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class MainActivity extends WearableActivity {
    private static Context context;

    RecyclerView mRecyclerView;
    ArrivalInfoAdaptor arrivalInfoAdaptor;

    private TextView mTextView;
    private TextView mTextView3;
    private TextView timestamp;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location mCurrentLocation;

    private LocationCallback locationCallback;

    private PositionDatabase posDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // https://stackoverflow.com/questions/2002288/static-way-to-get-context-in-android
        MainActivity.context = getApplicationContext();

        mRecyclerView = findViewById(R.id.ListArrivalInfo);
        arrivalInfoAdaptor = new ArrivalInfoAdaptor(new ArrayList<Position>());

        mRecyclerView.setAdapter(arrivalInfoAdaptor);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        mTextView = (TextView) findViewById(R.id.text);
        mTextView3 = (TextView) findViewById(R.id.textView2);
        timestamp = (TextView) findViewById(R.id.lastUpdateTime);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        posDatabase = Room.databaseBuilder(getApplicationContext(), PositionDatabase.class, "pubtrans4watch_inside.db")
                .allowMainThreadQueries()
                .createFromAsset("pubtrans4watch.db")
                .build();
        List<Position> res = posDatabase.positionDao().getAll();

        // TODO DB 초기화 테스트트
//       for (int i = 0; i < res.size(); i++){
//            Log.d("DB", "Data: " + res.get(i).toString());
//        }


        // Enables Always-on
        setAmbientEnabled();

        // TODO: 권한이 없는 경우 권한 요청기능 추가
        // 위치 정보 가져오기

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult == null){
                   Log.d("LOG", "onLocationResult: null");
                }

                for (Location loc : locationResult.getLocations()) {
                    //                    Log.i("INFO", "Location Updated");
                    mCurrentLocation = loc;
                    mTextView.setText("Latitude: " + Double.toString(loc.getLatitude()) + "\nLongitude: " + Double.toString(loc.getLongitude()));

                    List<Position> nearByStation = posDatabase.positionDao().getNearBy(loc.getLatitude(), loc.getLongitude(), 3);

//                    mRecyclerView.setAdapter(new ArrivalInfoAdaptor(nearByStation));
                    arrivalInfoAdaptor.updateStationInfo(nearByStation);
                    arrivalInfoAdaptor.notifyDataSetChanged();

                    String res = new String("");
                    if (nearByStation.size() == 0) {
                        res = "근처 역 없음";
                    } else {

                        for (int i = 0; i < nearByStation.size(); i++) {
                            Position position = nearByStation.get(i);

                            res += position;
                            res += "\n";

                            if (i == 0) {
                                TopisHelper.callTopisApi(position);
                            } else {
                                TopisHelper.callTopisApi(position);
                            }

                        }
                    }

                    mTextView3.setText(res);
                    timestamp.setText(LocalTime.now().toString());
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

    public static Context getAppContext(){
        return MainActivity.context;
    }
}
