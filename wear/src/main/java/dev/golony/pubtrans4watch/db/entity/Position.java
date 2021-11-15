package dev.golony.pubtrans4watch.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Position {
    @PrimaryKey
    public int id;

    public int type;
    public Double latitude;
    public Double longitude;
    public String station_name;
    public String line_num;
}
