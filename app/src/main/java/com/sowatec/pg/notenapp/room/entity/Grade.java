package com.sowatec.pg.notenapp.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "grade")
public class Grade {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "grade_id")
    private int grade_id;

    @ColumnInfo(name = "grade_name")
    private String grade_name;

    @ColumnInfo(name = "grade_date")
    private long grade_date;

    @ColumnInfo(name = "grade_grade")
    private double grade_grade;

    @ColumnInfo(name = "grade_sent")
    private boolean grade_sent;

    @Embedded
    private Subject grade_subject;

    public Grade() {

    }

    @Ignore
    public Grade(String grade_name, long grade_date, double grade_grade, boolean grade_sent, Subject grade_subject) {
        this.grade_name = grade_name;
        this.grade_date = grade_date;
        this.grade_grade = grade_grade;
        this.grade_sent = grade_sent;
        this.grade_subject = grade_subject;
    }

    public int getGradeId() {
        return grade_id;
    }

    public void setGradeId(int grade_id) {
        this.grade_id = grade_id;
    }

    public String getGradeName() {
        return grade_name;
    }

    public void setGradeName(String grade_name) {
        this.grade_name = grade_name;
    }

    public long getGradeDate() {
        return grade_date;
    }

    public void setGradeDate(long grade_date) {
        this.grade_date = grade_date;
    }

    public double getGradeGrade() {
        return grade_grade;
    }

    public void setGradeGrade(double grade_grade) {
        this.grade_grade = grade_grade;
    }

    public boolean isGradeSent() {
        return grade_sent;
    }

    public void setGradeSent(boolean grade_sent) {
        this.grade_sent = grade_sent;
    }

    public Subject getGradeSubject() {
        return grade_subject;
    }

    public void setGradeSubject(Subject grade_subject) {
        this.grade_subject = grade_subject;
    }
}
