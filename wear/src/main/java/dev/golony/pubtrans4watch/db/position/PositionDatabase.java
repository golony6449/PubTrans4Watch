package dev.golony.pubtrans4watch.db.position;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Position.class}, version = 1)
public abstract class PositionDatabase extends RoomDatabase {
    public abstract PositionDao positionDao();
}
