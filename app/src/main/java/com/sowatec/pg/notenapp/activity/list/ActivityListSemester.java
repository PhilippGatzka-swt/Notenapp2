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
import com.sowatec.pg.notenapp.activity.create.ActivityCreateSemester;
import com.sowatec.pg.notenapp.activity.list.fragment.SemesterListItem;
import com.sowatec.pg.notenapp.room.DatabaseTaskRunner;
import com.sowatec.pg.notenapp.room.GradeDatabase;
import com.sowatec.pg.notenapp.room.entity.Grade;
import com.sowatec.pg.notenapp.room.entity.Semester;
import com.sowatec.pg.notenapp.room.entity.Subject;

import java.util.ArrayList;
import java.util.List;

public class ActivityListSemester extends AppCompatActivity implements AbstractListActivity {

    private LinearLayout view_list_semester_list;
    private TextView label_list_semester_elements;
    private TextView label_list_semester_average;
    private Toolbar toolbar;
    private List<Semester> semesterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_semester);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        semesterList = new ArrayList<>();
        init();
    }

    @Override
    public void init() {
        view_list_semester_list = findViewById(R.id.view_list_semester_list);
        label_list_semester_elements = findViewById(R.id.label_list_semester_elements);
        label_list_semester_average = findViewById(R.id.label_list_semester_average);
    }

    @Override
    protected void onResume() {
        view_list_semester_list.removeAllViews();
        populateList();
        super.onResume();
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
    public void populateList() {
        new DatabaseTaskRunner().executeAsync(() -> GradeDatabase.get(getApplicationContext()).semesterDao().selectAll(), result -> {
            final boolean[] dark = {false};
            semesterList.clear();
            semesterList.addAll(result);
            result.forEach(element -> {
                SemesterListItem item = new SemesterListItem(element, view_list_semester_list.getContext());
                item.setOnClickListener(this::viewElement);
                view_list_semester_list.addView(item);
                if (dark[0])
                    item.setBackgroundColor(getApplicationContext().getColor(R.color.listItemDark));
                if (!dark[0])
                    item.setBackgroundColor(getApplicationContext().getColor(R.color.listItemBright));
                dark[0] = !dark[0];
            });
            label_list_semester_elements.setText(getApplicationContext().getString(R.string.elements, result.size()));
            if (result.size() == 1) {
                label_list_semester_elements.setText(getApplicationContext().getString(R.string.element, result.size()));
            }
        });
        new DatabaseTaskRunner().executeAsync(() -> GradeDatabase.get(getApplicationContext()).semesterDao().selectAverage(), result -> {
            if (result == null) result = 0.0;
            label_list_semester_average.setText(getApplicationContext().getString(R.string.average, result + ""));
        });
    }

    @Override
    public void viewElement(View view) {
        Intent intent = new Intent(getApplicationContext(), ActivityListSubject.class);
        int semester_id = ((SemesterListItem) view).getEntity().getSemester_id();
        intent.putExtra("semester_id", semester_id);
        startActivity(intent);
    }

    @Override
    public void createElement(View view) {
        Intent intent = new Intent(getApplicationContext(), ActivityCreateSemester.class);
        startActivity(intent);
    }


    @Override
    public void menuActionRefresh() {
        view_list_semester_list.removeAllViews();
        populateList();
    }

    @Override
    public void menuActionEdit() {
        ArrayAdapter<Semester> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, semesterList);
        Spinner spinner = new Spinner(this);
        spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        spinner.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setTitle(R.string.edit);
        builder.setPositiveButton(R.string.edit, (dialog, which) -> {
            Semester semester = (Semester) spinner.getSelectedItem();
            Intent intent = new Intent(getApplicationContext(), ActivityCreateSemester.class);
            intent.putExtra("semester_id", semester.getSemester_id());
            startActivity(intent);
        });
        builder.setView(spinner);
        builder.create().show();
    }

    @Override
    public void menuActionDelete() {
        ArrayAdapter<Semester> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, semesterList);
        Spinner spinner = new Spinner(this);
        spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        spinner.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setTitle(R.string.delete);
        builder.setPositiveButton(R.string.delete, (dialog, which) -> {
            Semester semester = (Semester) spinner.getSelectedItem();
            new DatabaseTaskRunner().executeAsync(() -> {
                List<Grade> grades = GradeDatabase.get(getApplicationContext()).gradeDao().selectBySemesterId(semester.getSemester_id());
                grades.forEach(GradeDatabase.get(getApplicationContext()).gradeDao()::delete);
                List<Subject> subjects = GradeDatabase.get(getApplicationContext()).subjectDao().selectBySemesterId(semester.getSemester_id());
                subjects.forEach(GradeDatabase.get(getApplicationContext()).subjectDao()::delete);
                GradeDatabase.get(getApplicationContext()).semesterDao().delete(semester);
                return null;
            }, result -> {

            });
        });

        builder.setView(spinner);
        builder.create().show();
    }
}