package com.sowatec.pg.notenapp.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.sowatec.pg.notenapp.room.entity.Subject;

import java.util.List;

@Dao
public interface SubjectDao {

    @Query("SELECT * FROM subject")
    List<Subject> selectAll();

    @Insert
    void insertAll(Subject... subjects);

    @Delete
    void delete(Subject... subject);

    @Query("SELECT * FROM subject where subject_id = :subject_id")
    Subject selectBySubjectId(int subject_id);

    @Query("SELECT * FROM subject where semester_id = :semester_id")
    List<Subject> selectBySemesterId(int semester_id);

    @Query("SELECT count(*) FROM grade WHERE subject_id = :subject_id")
    int selectSubjectGradeCount(int subject_id);

    @Query("SELECT avg(grade_grade) FROM grade WHERE subject_id = :subject_id")
    double selectSubjectAverage(int subject_id);

    @Update
    void update(Subject result);
}
