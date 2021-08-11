package com.sowatec.pg.notenapp.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "semester")
public class Semester {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "semester_id")
    private int semester_id;

    @ColumnInfo(name = "semester_name")
    private String semester_name;

    public Semester(){

    }

    @Ignore
    public Semester(String semester_name){
        this.semester_name = semester_name;
    }

    public int getSemester_id() {
        return semester_id;
    }

    public void setSemester_id(int semester_id) {
        this.semester_id = semester_id;
    }

    public String getSemester_name() {
        return semester_name;
    }

    public void setSemester_name(String semester_name) {
        this.semester_name = semester_name;
    }
}
