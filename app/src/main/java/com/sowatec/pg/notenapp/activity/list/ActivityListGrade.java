package com.sowatec.pg.notenapp.activity.list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sowatec.pg.notenapp.R;
import com.sowatec.pg.notenapp.activity.abstract_.AbstractListActivity;
import com.sowatec.pg.notenapp.activity.create.ActivityCreateGrade;
import com.sowatec.pg.notenapp.activity.create.ActivityCreateSubject;
import com.sowatec.pg.notenapp.activity.list.fragment.GradeListItem;
import com.sowatec.pg.notenapp.activity.list.fragment.SubjectListItem;
import com.sowatec.pg.notenapp.room.DatabaseTaskRunner;
import com.sowatec.pg.notenapp.room.GradeDatabase;
import com.sowatec.pg.notenapp.room.entity.Grade;
import com.sowatec.pg.notenapp.room.entity.Subject;

import java.util.List;

public class ActivityListGrade extends AppCompatActivity implements AbstractListActivity {

    private LinearLayout view_list_grade_list;
    private TextView label_list_grade_elements;
    private TextView label_list_grade_average;

    private int subject_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_grade);
        init();
        populateList();
    }

    @Override
    public void init() {
        subject_id = getIntent().getIntExtra("subject_id", -1);
        view_list_grade_list = findViewById(R.id.view_list_grade_list);
        label_list_grade_elements = findViewById(R.id.label_list_grade_elements);
        label_list_grade_average = findViewById(R.id.label_list_grade_average);
    }

    @Override
    protected void onResume() {
        view_list_grade_list.removeAllViews();
        populateList();
        super.onResume();
    }

    @Override
    public void populateList() {
        new DatabaseTaskRunner().executeAsync(() -> GradeDatabase.get(getApplicationContext()).gradeDao().selectBySubjectId(subject_id), new DatabaseTaskRunner.Callback<List<Grade>>() {
            @Override
            public void onComplete(List<Grade> result) {
                final boolean[] dark = {false};
                result.forEach(element -> {
                    GradeListItem item = new GradeListItem(element, view_list_grade_list.getContext());
                    item.setOnClickListener(view -> viewElement(view));
                    view_list_grade_list.addView(item);
                    if (dark[0])
                        item.setBackgroundColor(getApplicationContext().getColor(R.color.listItemDark));
                    if (!dark[0])
                        item.setBackgroundColor(getApplicationContext().getColor(R.color.listItemBright));
                    dark[0] = !dark[0];
                });
                label_list_grade_elements.setText(getApplicationContext().getString(R.string.elements, result.size()));
                if (result.size() == 1) {
                    label_list_grade_elements.setText(getApplicationContext().getString(R.string.element, result.size()));
                }
            }
        });
        new DatabaseTaskRunner().executeAsync(() -> GradeDatabase.get(getApplicationContext()).semesterDao().selectAverage(), new DatabaseTaskRunner.Callback<Double>() {
            @Override
            public void onComplete(Double result) {
                if (result == null) result = 0.0;
                label_list_grade_average.setText(getApplicationContext().getString(R.string.average, result + ""));
            }
        });
    }

    @Override
    public void viewElement(View view) {

    }

    @Override
    public void editElement(View view) {

    }

    @Override
    public void createElement(View view) {
        Intent intent = new Intent(getApplicationContext(), ActivityCreateGrade.class);
        intent.putExtra("subject_id", subject_id);
        startActivity(intent);
    }


}