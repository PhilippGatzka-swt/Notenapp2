package com.sowatec.pg.notenapp.activity.list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sowatec.pg.notenapp.R;
import com.sowatec.pg.notenapp.activity.abstract_.AbstractListActivity;
import com.sowatec.pg.notenapp.activity.create.ActivityCreateSemester;

public class ActivityListSemester extends AppCompatActivity implements AbstractListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_semester);
        Intent intent = new Intent(getApplicationContext(), ActivityCreateSemester.class);
        startActivity(intent);
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

    @Override
    public void init() {

    }
}