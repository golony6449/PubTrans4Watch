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
import dev.golony.pubtrans4watch.db.position.Position;

import java.util.List;

public class ArrivalInfoAdaptor extends RecyclerView.Adapter<ArrivalInfoAdaptor.ViewHolder> {
    List<Position> listStationInfo;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout view;

        ImageView image;
        TextView stationName;
        TextView nearestArrivalInfoTv;
        TextView nearerArrivalInfoTv;

        public ViewHolder(View view){
            super(view);

            image = view.findViewById(R.id.iconImageView);
            stationName = view.findViewById(R.id.stationNameTv);
            nearestArrivalInfoTv = view.findViewById(R.id.nearestArrivalInfoTv);
            nearerArrivalInfoTv = view.findViewById(R.id.nearerArrivalInfoTv);
        }

        public void setData(ArrivalInfo info){
            stationName.setText(info.getStationName());
            nearestArrivalInfoTv.setText(info.getNearestArrivalInfo());
            nearerArrivalInfoTv.setText(info.getNearerArrivalInfoTv());
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
                .inflate(R.layout.arrival_info2, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Position stationInfo = this.listStationInfo.get(position);
        ArrivalInfo arrivalInfo = new ArrivalInfo();

        arrivalInfo.setStationName(stationInfo.getStation_name());
        arrivalInfo.setNearestArrivalInfo("3분후 도착");
        arrivalInfo.setNearerArrivalInfoTv("5분후 도착");

        holder.setData(arrivalInfo);
    }

    @Override
    public int getItemCount() {
        return this.listStationInfo.size();
    }
}
