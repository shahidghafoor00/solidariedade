package com.byteshaft.solidariedadediria.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


@Dao
public interface UserDao {

    @Query("SELECT * FROM user WHERE email LIKE :email AND " + "password LIKE :password LIMIT 1")
    User getUser(String email, String password);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Update
    void update(User... user);

    @Query("UPDATE user SET username = :name, password = :password WHERE email =:email")
    void update(String name, String password, String email);

}
