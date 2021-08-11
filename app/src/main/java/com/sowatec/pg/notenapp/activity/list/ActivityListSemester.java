package com.sowatec.pg.notenapp.activity.list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.sowatec.pg.notenapp.R;
import com.sowatec.pg.notenapp.activity.abstract_.AbstractListActivity;
import com.sowatec.pg.notenapp.room.DatabaseTaskRunner;
import com.sowatec.pg.notenapp.room.GradeDatabase;
import com.sowatec.pg.notenapp.room.dao.GradeDao;
import com.sowatec.pg.notenapp.room.entity.Grade;
import com.sowatec.pg.notenapp.room.entity.Semester;
import com.sowatec.pg.notenapp.room.entity.Subject;

import java.util.Date;


public class ActivityListSemester extends AppCompatActivity implements AbstractListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_semester);

        new DatabaseTaskRunner().executeAsync(() -> {
            Subject subject = GradeDatabase.get(getApplicationContext()).subjectDao().selectBySubjectId(1);

            Grade grade = new Grade("Semesterprüfung", new Date().getTime(),5.56,false, subject);

            GradeDatabase.get(getApplicationContext()).gradeDao().insertAll(grade);
            return null;
        }, result -> {

        });




    }

    @Override
    public void populateList() {

    }

    @Override
    public void viewElement(View view) {

    }

    @Override
    public void editElement(View view) {

    }

    @Override
    public void createElement(View view) {

    }
}