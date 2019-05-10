package com.byteshaft.solidariedadediria.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovementDao {

    @Query("SELECT * FROM Movement")
    List<Movement> getAllMovements();

    @Insert
    void insert(Movement... movement);
}
