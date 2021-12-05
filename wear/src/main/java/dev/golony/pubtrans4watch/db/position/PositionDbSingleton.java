package dev.golony.pubtrans4watch.db.position;

import android.content.Context;
import androidx.room.Room;

public class PositionDbSingleton {
    private static PositionDatabase positionDatabase = null;

    public static PositionDatabase getDatabase(Context context){
        if (positionDatabase == null) {

            if (context == null) {
                throw new NullPointerException();
            }

            positionDatabase = Room.databaseBuilder(context, PositionDatabase.class, "pubtrans4watch_inside.db")
                    .allowMainThreadQueries()
                    .createFromAsset("pubtrans4watch.db")
                    .build();
        }

        return positionDatabase;
    }
}
