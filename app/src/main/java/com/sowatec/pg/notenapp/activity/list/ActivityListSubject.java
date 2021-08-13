package com.sowatec.pg.notenapp.activity.list;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sowatec.pg.notenapp.R;
import com.sowatec.pg.notenapp.activity.abstract_.AbstractListActivity;
import com.sowatec.pg.notenapp.activity.create.ActivityCreateSubject;
import com.sowatec.pg.notenapp.activity.list.fragment.SubjectListItem;
import com.sowatec.pg.notenapp.room.DatabaseTaskRunner;
import com.sowatec.pg.notenapp.room.GradeDatabase;
import com.sowatec.pg.notenapp.room.entity.Grade;
import com.sowatec.pg.notenapp.room.entity.Subject;

import java.util.ArrayList;
import java.util.List;

public class ActivityListSubject extends AppCompatActivity implements AbstractListActivity {

    private LinearLayout view_list_subject_list;
    private TextView label_list_subject_elements;
    private TextView label_list_subject_average;
    private int semester_id;
    private List<Subject> subjectList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_subject);
        subjectList = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        toolbar.getMenu().removeItem(R.id.menuEmail);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuRefresh:
                menuActionRefresh();
                return true;
            case R.id.menuEdit:
                menuActionEdit();
                return true;
            case R.id.menuDelete:
                menuActionDelete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        view_list_subject_list.removeAllViews();
        populateList();
        super.onResume();
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
        new DatabaseTaskRunner().executeAsync(() -> GradeDatabase.get(getApplicationContext()).subjectDao().selectBySemesterId(semester_id), result -> {
            final boolean[] dark = {false};
            subjectList.clear();
            subjectList.addAll(result);

            result.forEach(element -> {
                SubjectListItem item = new SubjectListItem(element, view_list_subject_list.getContext());
                item.setOnClickListener(this::viewElement);
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
        });
        new DatabaseTaskRunner().executeAsync(() -> GradeDatabase.get(getApplicationContext()).semesterDao().selectAverage(), result -> {
            if (result == null) result = 0.0;
            label_list_subject_average.setText(getApplicationContext().getString(R.string.average, result + ""));
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
    public void createElement(View view) {
        Intent intent = new Intent(getApplicationContext(), ActivityCreateSubject.class);
        intent.putExtra("semester_id", semester_id);
        startActivity(intent);
    }


    @Override
    public void menuActionRefresh() {
        view_list_subject_list.removeAllViews();
        populateList();
    }


    @Override
    public void menuActionEdit() {
        ArrayAdapter<Subject> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, subjectList);
        Spinner spinner = new Spinner(this);
        spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        spinner.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setTitle(R.string.edit);
        builder.setPositiveButton(R.string.edit, (dialog, which) -> {
            Subject subject = (Subject) spinner.getSelectedItem();
            Intent intent = new Intent(getApplicationContext(), ActivityCreateSubject.class);
            intent.putExtra("semester_id", subject.getSubject_id());
            startActivity(intent);
        });
        builder.setView(spinner);
        builder.create().show();
    }

    @Override
    public void menuActionDelete() {
        ArrayAdapter<Subject> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, subjectList);
        Spinner spinner = new Spinner(this);
        spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        spinner.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setTitle(R.string.delete);
        builder.setPositiveButton(R.string.delete, (dialog, which) -> {
            Subject subject = (Subject) spinner.getSelectedItem();
            new DatabaseTaskRunner().executeAsync(() -> {
                List<Grade> grades = GradeDatabase.get(getApplicationContext()).gradeDao().selectBySubjectId(subject.getSubject_id());
                grades.forEach(GradeDatabase.get(getApplicationContext()).gradeDao()::delete);
                GradeDatabase.get(getApplicationContext()).subjectDao().delete(subject);
                return null;
            }, result -> {

            });
        });

        builder.setView(spinner);
        builder.create().show();
    }
}