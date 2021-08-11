package com.sowatec.pg.notenapp.activity.list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sowatec.pg.notenapp.R;
import com.sowatec.pg.notenapp.activity.abstract_.AbstractListActivity;
import com.sowatec.pg.notenapp.activity.create.ActivityCreateSemester;
import com.sowatec.pg.notenapp.activity.list.fragment.SemesterListItem;
import com.sowatec.pg.notenapp.room.DatabaseTaskRunner;
import com.sowatec.pg.notenapp.room.GradeDatabase;
import com.sowatec.pg.notenapp.room.entity.Semester;

import java.text.BreakIterator;
import java.util.List;
import java.util.concurrent.Callable;

public class ActivityListSemester extends AppCompatActivity implements AbstractListActivity {

    private LinearLayout view_list_semester_list;
    private TextView label_list_semester_elements;
    private TextView label_list_semester_average;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_semester);
        init();
        populateList();
    }

    @Override
    public void init() {
        view_list_semester_list = findViewById(R.id.view_list_semester_list);
        label_list_semester_elements = findViewById(R.id.label_list_semester_elements);
        label_list_semester_average = findViewById(R.id.label_list_semester_average);
    }

    @Override
    public void populateList() {
        new DatabaseTaskRunner().executeAsync(() -> GradeDatabase.get(getApplicationContext()).semesterDao().selectAll(), new DatabaseTaskRunner.Callback<List<Semester>>() {
            @Override
            public void onComplete(List<Semester> result) {
                final boolean[] dark = {false};
                result.forEach(element -> {
                    SemesterListItem item = new SemesterListItem(element, view_list_semester_list.getContext());
                    item.setOnClickListener(view -> viewElement(view));
                    view_list_semester_list.addView(item);
                    if (dark[0])
                        item.setBackgroundColor(getApplicationContext().getColor(R.color.listItemDark));
                    if (!dark[0])
                        item.setBackgroundColor(getApplicationContext().getColor(R.color.listItemBright));
                    dark[0] = !dark[0];
                });
                label_list_semester_elements.setText(getApplicationContext().getString(R.string.elements,result.size()));
                if (result.size() == 1) {
                    label_list_semester_elements.setText(getApplicationContext().getString(R.string.element,result.size()));
                }
            }
        });
        new DatabaseTaskRunner().executeAsync(() -> GradeDatabase.get(getApplicationContext()).semesterDao().selectAverage(), new DatabaseTaskRunner.Callback<Double>() {
            @Override
            public void onComplete(Double result) {
                if (result == null) result = 0.0;
                label_list_semester_average.setText(getApplicationContext().getString(R.string.average,result + ""));
            }
        });

    }

    @Override
    public void viewElement(View view) {
        Intent intent = new Intent(getApplicationContext(),ActivityListSubject.class);
        int semester_id = ((SemesterListItem) view).getEntity().getSemester_id();
        intent.putExtra("semester_id",semester_id);
        startActivity(intent);
    }

    @Override
    public void editElement(View view) {

    }

    @Override
    public void createElement(View view) {
        Intent intent = new Intent(getApplicationContext(), ActivityCreateSemester.class);
        startActivity(intent);
    }


}