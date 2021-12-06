package dev.golony.pubtrans4watch.api;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import dev.golony.pubtrans4watch.MainActivity;
import dev.golony.pubtrans4watch.db.position.Position;
import org.json.JSONObject;

public class TopisHelper {
    private static final String url = "https://pubtrans4watch.golony.dev/v1/ArrivalInfo/%s";

    private static JsonObjectRequest getTopisRequest(String url, Response.Listener<JSONObject> listener){
        return new JsonObjectRequest(Request.Method.GET, url, null,
                listener,
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "onErrorResponse: ", error);
                    }
                });
    }

    public static void callTopisApi(Position stationInfo, Response.Listener<JSONObject> listener){

        // API 요청 DEMO
        String stationName = stationInfo.getStation_name();

        System.out.println("역 포함여부: " + stationInfo.getStation_name().contains("역"));
        if (stationInfo.getStation_name().contains("역")){
            stationName = stationName.substring(0, stationName.length()-1);
        }


        System.out.println("url: " + String.format(url, stationName));

        RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());

        JsonObjectRequest stringRequest = TopisHelper.getTopisRequest(String.format(url, stationName), listener);
        queue.add(stringRequest);
    }
}
