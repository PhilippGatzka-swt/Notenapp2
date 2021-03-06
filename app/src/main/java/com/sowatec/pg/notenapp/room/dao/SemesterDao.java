package com.sowatec.pg.notenapp.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.sowatec.pg.notenapp.room.entity.Semester;

import java.util.List;

@Dao
public interface SemesterDao {
    @Query("SELECT * FROM semester")
    List<Semester> selectAll();

    @Insert
    void insertAll(Semester... semesters);

    @Delete
    void delete(Semester... semester);

    @Query("SELECT * FROM semester where semester_id = :semester_id")
    Semester selectBySemesterId(int semester_id);

    @Query("SELECT count(*) FROM subject WHERE semester_id = :semester_id")
    int selectSubjectCountFromSemester(int semester_id);

    @Query("SELECT avg(grade_grade) FROM grade WHERE semester_id = :semester_id")
    double selectSemesterAverage(int semester_id);

    @Query("SELECT avg(grade_grade) FROM grade")
    Double selectAverage();

    @Update
    void update(Semester result);
}
