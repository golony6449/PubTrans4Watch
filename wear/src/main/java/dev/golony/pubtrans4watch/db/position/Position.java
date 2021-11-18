package dev.golony.pubtrans4watch.db.position;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Position {
    @PrimaryKey
    public int id;

    public String type;
    public Double latitude;
    public Double longitude;
    public String station_name;
    public String line_num;

    @Override
    public String toString(){
        return line_num + "   " + station_name;
    }
}
