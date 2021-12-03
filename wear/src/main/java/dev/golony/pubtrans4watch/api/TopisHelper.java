package dev.golony.pubtrans4watch.api;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import dev.golony.pubtrans4watch.MainActivity;
import dev.golony.pubtrans4watch.db.position.Position;
import dev.golony.pubtrans4watch.view.ArrivalInfoAdaptor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TopisHelper {
    private static final String url = "https://pubtrans4watch.golony.dev/v1/ArrivalInfo/%s";

    public static JsonObjectRequest getTopisRequest(String url){
        return new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject){
                        try {
                            System.out.println();
                            System.out.println(jsonObject);
                            JSONArray arrivalData = jsonObject.getJSONObject("data").getJSONArray("realtimeArrivalList");

                            for (int i = 0; i < arrivalData.length(); i++){
                                JSONObject jsonData = arrivalData.getJSONObject(i);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "onErrorResponse: ", error);
                    }
                });
    }

    public static void callTopisApi(Position stationInfo){

        // API 요청 DEMO
        String stationName = stationInfo.getStation_name();

        System.out.println("역 포함여부: " + stationInfo.getStation_name().contains("역"));
        if (stationInfo.getStation_name().contains("역")){
            stationName = stationName.substring(0, stationName.length()-1);
        }


        System.out.println("url: " + url);

        RequestQueue queue = Volley.newRequestQueue(MainActivity.getAppContext());

        JsonObjectRequest stringRequest = TopisHelper.getTopisRequest(String.format(url, stationName));
        queue.add(stringRequest);
    }
}
