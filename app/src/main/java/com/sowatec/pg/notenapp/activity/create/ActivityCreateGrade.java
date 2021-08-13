package com.sowatec.pg.notenapp.activity.create;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.sowatec.pg.notenapp.R;
import com.sowatec.pg.notenapp.activity.abstract_.AbstractCreateActivity;
import com.sowatec.pg.notenapp.room.DatabaseTaskRunner;
import com.sowatec.pg.notenapp.room.GradeDatabase;
import com.sowatec.pg.notenapp.room.entity.Grade;
import com.sowatec.pg.notenapp.room.entity.Subject;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;

public class ActivityCreateGrade extends AppCompatActivity implements AbstractCreateActivity {

    private EditText input_create_grade_name;
    private EditText input_create_grade_grade;
    private DatePicker input_create_grade_date;
    private Button button_create_grade_save;
    private TextView label_create_grade_name_char_count;

    private final int maxLen = 20;

    private Subject subject;
    private int subject_id;
    private int grade_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_grade);
        subject_id = getIntent().getIntExtra("subject_id", -1);
        grade_id = getIntent().getIntExtra("grade_id", -1);
        init();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void init() {

        input_create_grade_date = findViewById(R.id.input_create_grade_date);
        input_create_grade_grade = findViewById(R.id.input_create_grade_grade);
        input_create_grade_name = findViewById(R.id.input_create_grade_name);
        button_create_grade_save = findViewById(R.id.button_create_grade_save);

        if (grade_id != -1) {
            new DatabaseTaskRunner().executeAsync(() -> GradeDatabase.get(getApplicationContext()).gradeDao().selectByGradeId(grade_id), result -> {
                Date d = new Date(result.getGrade_date());
                input_create_grade_date.updateDate(d.getYear(), d.getMonth(), d.getDay());
                input_create_grade_name.setText(result.getGrade_name());
                input_create_grade_grade.setText(result.getGrade_grade() + "");
            });
        }

        button_create_grade_save.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.colorstate));
        label_create_grade_name_char_count = findViewById(R.id.label_create_grade_name_char_count);
        label_create_grade_name_char_count.setText(getString(R.string.maxChars, 0, maxLen));

        input_create_grade_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //TODO Validation
            }
        });
        new DatabaseTaskRunner().executeAsync(() -> GradeDatabase.get(getApplicationContext()).subjectDao().selectBySubjectId(subject_id), this::setSubject);

    }

    private void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public void save(View view) {
        final String name = input_create_grade_name.getText().toString().trim();
        long date = getDateFromDatePicker(input_create_grade_date).getTime();
        double gGrade = Double.parseDouble(input_create_grade_grade.getText().toString());
        if (grade_id == -1) {
            Grade grade = new Grade(name, date, gGrade, false, subject);

            new DatabaseTaskRunner().executeAsync(() -> {
                if (GradeDatabase.get(getApplicationContext()).gradeDao().doesGradeNameExistInSubject(name, subject_id) == null) {
                    GradeDatabase.get(getApplicationContext()).gradeDao().insertAll(grade);
                    finish();
                } else
                    throw new UnsupportedOperationException("Grade with name already registered for this Subject");
                return null;
            }, result -> {

            });
        }else {
            new DatabaseTaskRunner().executeAsync(new Callable<Grade>() {
                @Override
                public Grade call() throws Exception {
                    return GradeDatabase.get(getApplicationContext()).gradeDao().selectByGradeId(grade_id);
                }
            }, new DatabaseTaskRunner.Callback<Grade>() {
                @Override
                public void onComplete(Grade result) {
                    result.setGrade_name(name);
                    result.setGrade_date(date);
                    result.setGrade_grade(gGrade);
                    GradeDatabase.get(getApplicationContext()).gradeDao().update(result);
                    finish();
                }
            });
        }
    }

    @Override
    public void cancel(View view) {
        finish();
    }

    private static java.util.Date getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
}