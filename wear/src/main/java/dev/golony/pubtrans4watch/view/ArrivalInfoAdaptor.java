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

        public ViewHolder(View view){
            super(view);

//            ImageView photo = view.findViewById(R.id.dogPhotoImg);
//            TextView breed = view.findViewById(R.id.dogBreedTv);
//            TextView age = view.findViewById(R.id.dogAgeTv);
//            TextView gender = view.findViewById(R.id.dogGenderTv);
//
//            photo.setImageResource(R.mipmap.ic_launcher);
//            breed.setText("breed");
//            age.setText("123");
//            gender.setText("TT");
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

    }

    @Override
    public int getItemCount() {
        return this.listStationInfo.size();
    }
}
