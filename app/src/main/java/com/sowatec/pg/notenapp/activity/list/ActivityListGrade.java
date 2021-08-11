package com.sowatec.pg.notenapp.activity.list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.sowatec.pg.notenapp.R;
import com.sowatec.pg.notenapp.activity.abstract_.AbstractListActivity;

public class ActivityListGrade extends AppCompatActivity implements AbstractListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_grade);
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