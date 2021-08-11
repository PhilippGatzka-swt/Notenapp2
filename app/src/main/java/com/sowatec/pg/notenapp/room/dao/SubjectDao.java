package com.sowatec.pg.notenapp.room.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.sowatec.pg.notenapp.room.entity.Subject;

import java.util.List;

public interface SubjectDao {

    @Query("SELECT * FROM subject")
    List<Subject> selectAll();

    @Insert
    void insertAll(Subject... subjects);

    @Delete
    void delete(Subject... subject);


}
