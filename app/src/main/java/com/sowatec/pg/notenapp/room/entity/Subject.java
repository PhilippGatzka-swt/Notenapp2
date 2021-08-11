package com.sowatec.pg.notenapp.room.entity;


import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "subject")
public class Subject {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "subject_id")
    private int subject_id;

    @ColumnInfo(name = "subject_name")
    private String subject_name;

    @Embedded
    private Semester subject_semester;

    public Subject() {

    }

    @Ignore
    public Subject(String subject_name, Semester subject_semester) {
        this.subject_name = subject_name;
        this.subject_semester = subject_semester;
    }

    public int getSubjectId() {
        return subject_id;
    }

    public void setSubjectId(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubjectName() {
        return subject_name;
    }

    public void setSubjectName(String subject_name) {
        this.subject_name = subject_name;
    }

    public Semester getSubjectSemester() {
        return subject_semester;
    }

    public void setSubjectSemester(Semester subject_semester) {
        this.subject_semester = subject_semester;
    }
}
