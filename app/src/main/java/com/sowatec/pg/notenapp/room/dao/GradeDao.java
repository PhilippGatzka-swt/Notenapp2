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

    @Query("SELECT * FROM grade where grade_name = :grade_name AND subject_id = :subject_id")
    Grade doesGradeNameExistInSubject(String grade_name, int subject_id);

    @Query("SELECT * FROM grade WHERE subject_id = :subject_id")
    List<Grade> selectBySubjectId(int subject_id);
}
