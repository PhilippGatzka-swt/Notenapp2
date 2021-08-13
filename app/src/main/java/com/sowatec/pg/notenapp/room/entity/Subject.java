package com.sowatec.pg.notenapp.room.entity;


import androidx.annotation.NonNull;
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

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public Semester getSubject_semester() {
        return subject_semester;
    }

    public void setSubject_semester(Semester subject_semester) {
        this.subject_semester = subject_semester;
    }

    @NonNull
    @Override
    public String toString() {
        return subject_name;
    }
}
