package com.sowatec.pg.notenapp.activity.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sowatec.pg.notenapp.R;
import com.sowatec.pg.notenapp.activity.abstract_.AbstractListActivity;
import com.sowatec.pg.notenapp.activity.create.ActivityCreateSubject;
import com.sowatec.pg.notenapp.activity.list.fragment.SubjectListItem;
import com.sowatec.pg.notenapp.room.DatabaseTaskRunner;
import com.sowatec.pg.notenapp.room.GradeDatabase;
import com.sowatec.pg.notenapp.room.entity.Subject;

import java.util.List;

public class ActivityListSubject extends AppCompatActivity implements AbstractListActivity {

    private LinearLayout view_list_subject_list;
    private TextView label_list_subject_elements;
    private TextView label_list_subject_average;
    private int semester_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_subject);
        init();
        populateList();
    }

    @Override
    public void init() {
        semester_id = getIntent().getIntExtra("semester_id", -1);
        view_list_subject_list = findViewById(R.id.view_list_subject_list);
        label_list_subject_elements = findViewById(R.id.label_list_subject_elements);
        label_list_subject_average = findViewById(R.id.label_list_subject_average);
    }

    @Override
    public void populateList() {
        new DatabaseTaskRunner().executeAsync(() -> GradeDatabase.get(getApplicationContext()).subjectDao().selectBySemesterId(semester_id), new DatabaseTaskRunner.Callback<List<Subject>>() {
            @Override
            public void onComplete(List<Subject> result) {
                final boolean[] dark = {false};
                result.forEach(element -> {
                    SubjectListItem item = new SubjectListItem(element, view_list_subject_list.getContext());
                    item.setOnClickListener(view -> viewElement(view));
                    view_list_subject_list.addView(item);
                    if (dark[0])
                        item.setBackgroundColor(getApplicationContext().getColor(R.color.listItemDark));
                    if (!dark[0])
                        item.setBackgroundColor(getApplicationContext().getColor(R.color.listItemBright));
                    dark[0] = !dark[0];
                });
                label_list_subject_elements.setText(getApplicationContext().getString(R.string.elements, result.size()));
                if (result.size() == 1) {
                    label_list_subject_elements.setText(getApplicationContext().getString(R.string.element, result.size()));
                }
            }
        });
        new DatabaseTaskRunner().executeAsync(() -> GradeDatabase.get(getApplicationContext()).semesterDao().selectAverage(), new DatabaseTaskRunner.Callback<Double>() {
            @Override
            public void onComplete(Double result) {
                if (result == null) result = 0.0;
                label_list_subject_average.setText(getApplicationContext().getString(R.string.average, result + ""));
            }
        });
    }

    @Override
    public void viewElement(View view) {
        Intent intent = new Intent(getApplicationContext(), ActivityListGrade.class);
        int subject_id = ((SubjectListItem) view).getEntity().getSubject_id();
        intent.putExtra("subject_id", subject_id);
        startActivity(intent);
    }

    @Override
    public void editElement(View view) {

    }

    @Override
    public void createElement(View view) {
        Intent intent = new Intent(getApplicationContext(), ActivityCreateSubject.class);
        intent.putExtra("semester_id", semester_id);
        startActivity(intent);
    }


}