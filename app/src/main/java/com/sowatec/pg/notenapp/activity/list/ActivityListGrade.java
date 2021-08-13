package com.sowatec.pg.notenapp.activity.list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.sowatec.pg.notenapp.R;
import com.sowatec.pg.notenapp.activity.abstract_.AbstractListActivity;
import com.sowatec.pg.notenapp.activity.create.ActivityCreateGrade;
import com.sowatec.pg.notenapp.activity.create.ActivityCreateSemester;
import com.sowatec.pg.notenapp.activity.create.ActivityCreateSubject;
import com.sowatec.pg.notenapp.activity.list.fragment.GradeListItem;
import com.sowatec.pg.notenapp.activity.list.fragment.SubjectListItem;
import com.sowatec.pg.notenapp.room.DatabaseTaskRunner;
import com.sowatec.pg.notenapp.room.GradeDatabase;
import com.sowatec.pg.notenapp.room.entity.Grade;
import com.sowatec.pg.notenapp.room.entity.Semester;
import com.sowatec.pg.notenapp.room.entity.Subject;

import java.util.ArrayList;
import java.util.List;

public class ActivityListGrade extends AppCompatActivity implements AbstractListActivity {

    private LinearLayout view_list_grade_list;
    private TextView label_list_grade_elements;
    private TextView label_list_grade_average;

    private List<Grade> gradeList;
    private int subject_id;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_grade);
        gradeList = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
    }

    @Override
    public void init() {
        subject_id = getIntent().getIntExtra("subject_id", -1);
        view_list_grade_list = findViewById(R.id.view_list_grade_list);
        label_list_grade_elements = findViewById(R.id.label_list_grade_elements);
        label_list_grade_average = findViewById(R.id.label_list_grade_average);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

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
            case R.id.menuEmail:
                menuActionEmail();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        view_list_grade_list.removeAllViews();
        populateList();
        super.onResume();
    }

    @Override
    public void populateList() {
        new DatabaseTaskRunner().executeAsync(() -> GradeDatabase.get(getApplicationContext()).gradeDao().selectBySubjectId(subject_id), result -> {
            final boolean[] dark = {false};
            gradeList.clear();
            gradeList.addAll(result);
            result.forEach(element -> {
                GradeListItem item = new GradeListItem(element, view_list_grade_list.getContext());
                item.setOnClickListener(this::viewElement);
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
        });
        new DatabaseTaskRunner().executeAsync(() -> GradeDatabase.get(getApplicationContext()).semesterDao().selectAverage(), result -> {
            if (result == null) result = 0.0;
            label_list_grade_average.setText(getApplicationContext().getString(R.string.average, result + ""));
        });
    }

    @Override
    public void viewElement(View view) {

    }


    @Override
    public void createElement(View view) {
        Intent intent = new Intent(getApplicationContext(), ActivityCreateGrade.class);
        intent.putExtra("subject_id", subject_id);
        startActivity(intent);
    }


    @Override
    public void menuActionRefresh() {
        view_list_grade_list.removeAllViews();
        populateList();
    }

    @Override
    public void menuActionEmail() {
        ArrayAdapter<Grade> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, gradeList);
        Spinner spinner = new Spinner(this);
        spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        spinner.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setTitle(R.string.email);
        builder.setPositiveButton(R.string.send, (dialog, which) -> {
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{"peter@sowatec.com"});
            email.putExtra(Intent.EXTRA_SUBJECT, "Noteneintrag");
            Grade grade = (Grade) spinner.getSelectedItem();
            String message = "Hallo Kai\n Ich habe gerade eine Note eingetragen: " +
                    "Semester: " + grade.getGrade_subject().getSubject_semester().getSemester_name() + "\n" +
                    "Fach: " + grade.getGrade_subject().getSubject_name() + "\n" +
                    "Note: " + grade.getGrade_name() + " " + grade.getGrade_grade();
            email.putExtra(Intent.EXTRA_TEXT, message);
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, "Choose an Email client :"));
        });
        builder.setView(spinner);
        builder.create().show();
    }

    @Override
    public void menuActionEdit() {
        ArrayAdapter<Grade> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, gradeList);
        Spinner spinner = new Spinner(this);
        spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        spinner.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setTitle(R.string.edit);
        builder.setPositiveButton(R.string.edit, (dialog, which) -> {
            Grade grade = (Grade) spinner.getSelectedItem();
            Intent intent = new Intent(getApplicationContext(), ActivityCreateGrade.class);
            intent.putExtra("grade_id", grade.getGrade_id());
            startActivity(intent);
        });
        builder.setView(spinner);
        builder.create().show();
    }

    @Override
    public void menuActionDelete() {
        ArrayAdapter<Grade> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, gradeList);
        Spinner spinner = new Spinner(this);
        spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        spinner.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setTitle(R.string.delete);
        builder.setPositiveButton(R.string.delete, (dialog, which) -> {
            Grade grade = (Grade) spinner.getSelectedItem();
            new DatabaseTaskRunner().executeAsync(() -> {
                GradeDatabase.get(getApplicationContext()).gradeDao().delete(grade);
                return null;
            }, result -> {

            });
        });

        builder.setView(spinner);
        builder.create().show();
    }
}