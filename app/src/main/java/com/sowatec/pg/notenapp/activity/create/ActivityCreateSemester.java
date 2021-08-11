package com.sowatec.pg.notenapp.activity.create;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sowatec.pg.notenapp.R;
import com.sowatec.pg.notenapp.activity.abstract_.AbstractCreateActivity;

public class ActivityCreateSemester extends AppCompatActivity implements AbstractCreateActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_semester);
    }
}