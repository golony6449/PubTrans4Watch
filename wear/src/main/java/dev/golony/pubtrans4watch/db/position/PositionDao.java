package dev.golony.pubtrans4watch.db.position;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PositionDao {
    @Query("SELECT * FROM POSITION")
    List<Position> getAll();

    @Query("SELECT * FROM POSITION " +
            "WHERE latitude BETWEEN (:latitude) - 0.01 AND (:latitude) + 0.01 " +
            "AND longitude BETWEEN (:longitude) - 0.01 AND (:longitude) + 0.01")
    List<Position> getNearBy(Double latitude, Double longitude);

    @Query("SELECT * FROM (" +
                "SELECT " +
                "( (ABS(P.latitude - (:latitude)) * ABS(P.LATITUDE - (:latitude)) + ABS(P.LONGITUDE - (:longitude)) * ABS(P.LONGITUDE - (:longitude))) ) AS DIST " +
                ", P.* " +
                "FROM POSITION AS P " +
                "WHERE latitude BETWEEN (:latitude) - 0.01 AND (:latitude) + 0.01 " +
                "AND longitude BETWEEN (:longitude) - 0.01 AND (:longitude) + 0.01 " +
            ")" +
            "ORDER BY DIST " +
            "LIMIT (:cnt)")
    List<Position> getNearBy(Double latitude, Double longitude, int cnt);
}
