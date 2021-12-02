package dev.golony.pubtrans4watch;

import android.Manifest;
import android.app.DownloadManager;
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
import dev.golony.pubtrans4watch.db.position.Position;
import dev.golony.pubtrans4watch.db.position.PositionDatabase;
import dev.golony.pubtrans4watch.view.ArrivalInfoAdaptor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class MainActivity extends WearableActivity {

    RecyclerView mRecyclerView;

    private TextView mTextView;
    private TextView mTextView2;
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

        // TODO 임시
        mRecyclerView = findViewById(R.id.ListArrivalInfo);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        // 임시끝

        mTextView = (TextView) findViewById(R.id.text);
        mTextView2 = (TextView) findViewById(R.id.textView);
        mTextView3 = (TextView) findViewById(R.id.textView2);
        timestamp = (TextView) findViewById(R.id.lastUpdateTime);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        posDatabase = Room.databaseBuilder(getApplicationContext(), PositionDatabase.class, "pubtrans4watch_inside.db")
                .allowMainThreadQueries()
                .createFromAsset("pubtrans4watch.db")
                .build();
        List<Position> res = posDatabase.positionDao().getAll();

        for (int i = 0; i < res.size(); i++){
            Log.d("DB", "Data: " + res.get(i).toString());
        }


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

                for (Location loc : locationResult.getLocations()) {
                    //                    Log.i("INFO", "Location Updated");
                    mCurrentLocation = loc;
                    mTextView.setText("Latitude: " + Double.toString(loc.getLatitude()) + "\nLongitude: " + Double.toString(loc.getLongitude()));

                    List<Position> nearByStation = posDatabase.positionDao().getNearBy(loc.getLatitude(), loc.getLongitude(), 3);

                    mRecyclerView.setAdapter(new ArrivalInfoAdaptor(nearByStation));

                    String res = new String("");
                    if (nearByStation.size() == 0) {
                        res = "근처 역 없음";
                    } else {

                        for (int i = 0; i < nearByStation.size(); i++) {
                            Position position = nearByStation.get(i);

                            res += position;
                            res += "\n";

                            if (i == 0) {
                                callTopisApi(position, true);
                            } else {
                                callTopisApi(position, false);
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

    private void callTopisApi(Position stationInfo, boolean clear){

        // API 요청 DEMO
        String stationName = stationInfo.getStation_name();

        System.out.println("역 포함여부: " + stationInfo.getStation_name().contains("역"));
        if (stationInfo.getStation_name().contains("역")){
            stationName = stationName.substring(0, stationName.length()-1);
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://pubtrans4watch.golony.dev/v1/ArrivalInfo/" + stationName;
        System.out.println("url: " + url);

        String finalStationName = stationName;
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject){
                        String data = (String) mTextView2.getText();
                        if (clear) {
                            data = "";
                        }

                        data += "\n\n" + stationInfo + "\n";

                        try {
                            System.out.println();
                            System.out.println(jsonObject);
                            JSONArray arrivalData = jsonObject.getJSONObject("data").getJSONArray("realtimeArrivalList");

                            for (int i = 0; i < arrivalData.length(); i++){
                                JSONObject jsonData = arrivalData.getJSONObject(i);

                                data += jsonData.getString("trainLineNm");
                                data += "\n";
                                data += jsonData.getString("arvlMsg2");
                                data += "\n\n";
                            }

                            mTextView2.setText(data);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "onErrorResponse: ", error);
                        mTextView2.setText("That didn't work!");
                    }
                });

        queue.add(stringRequest);
    }
}
