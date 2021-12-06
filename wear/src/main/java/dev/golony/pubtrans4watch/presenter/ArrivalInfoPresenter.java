package dev.golony.pubtrans4watch.presenter;

import com.android.volley.Response;
import dev.golony.pubtrans4watch.api.TopisHelper;
import dev.golony.pubtrans4watch.db.position.Position;
import dev.golony.pubtrans4watch.view.ArrivalInfo;
import dev.golony.pubtrans4watch.view.ArrivalInfoAdaptor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArrivalInfoPresenter implements ArrivalInfoPresenterInterface.Presenter {
    ArrivalInfoAdaptor arrivalInfoAdaptor;

    public ArrivalInfoPresenter(ArrivalInfoAdaptor arrivalInfoAdaptor) {
        this.arrivalInfoAdaptor = arrivalInfoAdaptor;
    }

    @Override
    public void setDataFromTopis(ArrivalInfoAdaptor.ViewHolder holder, Position position) {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<ArrivalInfo> listArrivalInfo = new ArrayList<>();
                JSONArray jsonResponse = new JSONArray();

                try {
                    jsonResponse = response.getJSONObject("data").getJSONArray("realtimeArrivalList");
                } catch (JSONException e) {
                    System.out.println("ERROR: Wrong Json Syntax");
                    e.printStackTrace();
                }

                for (int i = 0; i < jsonResponse.length(); i++){
                    String strHeadingInfo = "";
                    String strArrivalInfo = "";

                    try {
                        JSONObject response_origin = jsonResponse.getJSONObject(i);
                        strHeadingInfo = response_origin.getString("trainLineNm");
                        strArrivalInfo = response_origin.getString("arvlMsg2");

                    } catch (JSONException e) {
                        System.out.println("ERROR: Wrong Json Syntax");
                        e.printStackTrace();
                    }

                    ArrivalInfo arrivalInfo = new ArrivalInfo();

                    arrivalInfo.setStationName(position.getStation_name());
                    arrivalInfo.setStrArrivalInfo(String.format("%s   %s", strHeadingInfo, strArrivalInfo));

                    listArrivalInfo.add(arrivalInfo);
                }

                arrivalInfoAdaptor.setResponseData(holder, position.getStation_name(), listArrivalInfo);
            }
        };

        TopisHelper.callTopisApi(position, response);
    }
}
