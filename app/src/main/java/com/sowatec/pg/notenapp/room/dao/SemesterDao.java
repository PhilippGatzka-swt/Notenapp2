package com.sowatec.pg.notenapp.room.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.sowatec.pg.notenapp.room.entity.Semester;

import java.util.List;

public interface SemesterDao {
    @Query("SELECT * FROM semester")
    List<Semester> selectAll();

    @Insert
    void insertAll(Semester... semesters);

    @Delete
    void delete(Semester... semester);

}
