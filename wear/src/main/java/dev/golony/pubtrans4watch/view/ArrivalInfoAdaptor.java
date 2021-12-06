package dev.golony.pubtrans4watch.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import dev.golony.pubtrans4watch.R;
import dev.golony.pubtrans4watch.api.TopisHelper;
import dev.golony.pubtrans4watch.db.position.Position;
import dev.golony.pubtrans4watch.presenter.ArrivalInfoPresenter;
import dev.golony.pubtrans4watch.presenter.ArrivalInfoPresenterInterface;

import java.util.List;

public class ArrivalInfoAdaptor extends RecyclerView.Adapter<ArrivalInfoAdaptor.ViewHolder> {
    ArrivalInfoPresenterInterface.Presenter arrivalInfoPresenter;

    List<Position> listStationInfo;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout view;

        private final ImageView image;
        private final TextView stationName;
        private final TextView nearestArrivalInfoTv;
        private final TextView nearerArrivalInfoTv;

        public ViewHolder(View view){
            super(view);

            image = view.findViewById(R.id.iconImageView);
            stationName = view.findViewById(R.id.stationNameTv);
            nearestArrivalInfoTv = view.findViewById(R.id.nearestArrivalInfoTv);
            nearerArrivalInfoTv = view.findViewById(R.id.nearerArrivalInfoTv);
        }

        public void setData(String strStationName, List<ArrivalInfo> listArrivalInfo){
            stationName.setText(strStationName);

            if (listArrivalInfo.size() > 0)
                nearestArrivalInfoTv.setText(listArrivalInfo.get(0).getStrArrivalInfo());

            if (listArrivalInfo.size() > 1)
                nearerArrivalInfoTv.setText(listArrivalInfo.get(1).getStrArrivalInfo());
        }

        public ConstraintLayout getView(){
            return view;
        }
    }

    public ArrivalInfoAdaptor(List<Position> data){
        this.listStationInfo = data;
    }

    @NonNull
    @Override
    public ArrivalInfoAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.arrival_info, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Position stationInfo = this.listStationInfo.get(position);

        arrivalInfoPresenter = new ArrivalInfoPresenter(this);
        arrivalInfoPresenter.setDataFromTopis(holder, stationInfo);
    }

    public void setResponseData(@NonNull ViewHolder holder, String stationName, List<ArrivalInfo> listArrivalInfo){
        System.out.println("length: " + listArrivalInfo.size());

        holder.setData(stationName, listArrivalInfo);
    }

    @Override
    public int getItemCount() {
        return this.listStationInfo.size();
    }

    public void updateStationInfo(List<Position> newStationInfo){
        this.listStationInfo = newStationInfo;
    }
}
