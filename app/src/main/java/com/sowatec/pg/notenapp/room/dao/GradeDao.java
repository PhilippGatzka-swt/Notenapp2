package com.sowatec.pg.notenapp.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.sowatec.pg.notenapp.room.entity.Grade;

import java.util.List;

@Dao
public interface GradeDao {

    @Query("SELECT * FROM grade")
    List<Grade> selectAll();

    @Insert
    void insertAll(Grade... grades);

    @Delete
    void delete(Grade... grade);

    @Query("SELECT * FROM grade where grade_id = :grade_id")
    Grade selectByGradeId(int grade_id);

}
