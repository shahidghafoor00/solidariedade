package com.byteshaft.solidariedadediria.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAllUser();


    @Insert
    void insert(User... user);

    @Update
    void update(User... user);
}
