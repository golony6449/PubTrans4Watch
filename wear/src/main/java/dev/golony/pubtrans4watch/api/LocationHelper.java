package dev.golony.pubtrans4watch.api;

import android.app.Activity;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.room.Room;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import dev.golony.pubtrans4watch.MainActivity;
import dev.golony.pubtrans4watch.R;
import dev.golony.pubtrans4watch.db.position.Position;
import dev.golony.pubtrans4watch.db.position.PositionDatabase;
import dev.golony.pubtrans4watch.db.position.PositionDbSingleton;
import dev.golony.pubtrans4watch.view.ArrivalInfoAdaptor;

import java.time.LocalTime;
import java.util.List;

public class LocationHelper {
    public static LocationCallback getLocationCallback(ArrivalInfoAdaptor arrivalInfoAdaptor, Activity activityContext){
        return new LocationCallback() {

            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                System.out.println("onLocationResult");
                super.onLocationResult(locationResult);

                TextView mTextView = ((Activity) activityContext).findViewById(R.id.text);
                TextView mTextView2 = ((Activity) activityContext).findViewById(R.id.textView2);
                TextView timestamp = ((Activity) activityContext).findViewById(R.id.lastUpdateTime);

                if (locationResult == null){
                    Log.d("LOG", "onLocationResult: null");
                }

                List<Location> listLocation = locationResult.getLocations();
                System.out.println("Location List Size: " + listLocation.size());

                for (Location loc : listLocation) {
                    //                    Log.i("INFO", "Location Updated");
                    mTextView.setText("Latitude: " + Double.toString(loc.getLatitude()) + "\nLongitude: " + Double.toString(loc.getLongitude()));

                    List<Position> nearByStationInfo = LocationHelper.getNearByStationInfo(loc);

//                    mRecyclerView.setAdapter(new ArrivalInfoAdaptor(nearByStation));
                    arrivalInfoAdaptor.updateStationInfo(nearByStationInfo);
                    arrivalInfoAdaptor.notifyDataSetChanged();

                    String res = new String("");
                    if (nearByStationInfo.size() == 0) {
                        res = "근처 역 없음";
                    } else {

                        for (int i = 0; i < nearByStationInfo.size(); i++) {
                            Position position = nearByStationInfo.get(i);

                            res += position;
                            res += "\n";
                        }
                    }

                    mTextView2.setText(res);
                    timestamp.setText(LocalTime.now().toString());
                }
            }
        };
    }

    private static List<Position> getNearByStationInfo(Location loc){
        PositionDatabase posDatabase = PositionDbSingleton.getDatabase(null);
        List<Position> nearByStation = posDatabase.positionDao().getNearBy(loc.getLatitude(), loc.getLongitude(), 3);
        return nearByStation;
    }
}
