package com.sowatec.pg.notenapp.activity.create;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.sowatec.pg.notenapp.R;
import com.sowatec.pg.notenapp.activity.abstract_.AbstractCreateActivity;

public class ActivityCreateSubject extends AppCompatActivity implements AbstractCreateActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_subject);
    }

    @Override
    public void init() {

    }

    @Override
    public void save(View view) {

    }

    @Override
    public void cancel(View view) {

    }
}