package com.sowatec.pg.notenapp.activity.create;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sowatec.pg.notenapp.R;
import com.sowatec.pg.notenapp.activity.abstract_.AbstractCreateActivity;
import com.sowatec.pg.notenapp.room.DatabaseTaskRunner;
import com.sowatec.pg.notenapp.room.GradeDatabase;
import com.sowatec.pg.notenapp.room.entity.Semester;
import com.sowatec.pg.notenapp.room.entity.Subject;

public class ActivityCreateSubject extends AppCompatActivity implements AbstractCreateActivity {

    private final int maxLen = 20;
    private EditText input_create_subject_name;
    private Button button_create_subject_save;
    private TextView label_create_subject_name_char_count;
    private Semester semester;
    private int semester_id;
    private int subject_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_subject);
        semester_id = getIntent().getIntExtra("semester_id", -1);
        subject_id = getIntent().getIntExtra("subject_id", -1);
        init();
    }

    @Override
    public void init() {
        label_create_subject_name_char_count = findViewById(R.id.label_create_subject_name_char_count);
        label_create_subject_name_char_count.setText(getString(R.string.maxChars, 0, maxLen));

        button_create_subject_save = findViewById(R.id.button_create_subject_save);
        button_create_subject_save.setEnabled(false);
        button_create_subject_save.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.colorstate));

        input_create_subject_name = findViewById(R.id.input_create_subject_name);
        if (subject_id != -1)
            new DatabaseTaskRunner().executeAsync(() -> GradeDatabase.get(getApplicationContext()).subjectDao().selectBySubjectId(subject_id), result -> input_create_subject_name.setText(result.getSubject_name()));

        input_create_subject_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int len = s.toString().length();
                label_create_subject_name_char_count.setText(getString(R.string.maxChars, len, maxLen));
                label_create_subject_name_char_count.setTextColor(-1979711488);
                if (len > maxLen)
                    label_create_subject_name_char_count.setTextColor(Color.RED);
                if (s.toString().equals("") || len > maxLen) {
                    button_create_subject_save.setEnabled(false);
                    return;
                }
                if (!s.toString().equals("")) {
                    button_create_subject_save.setEnabled(true);
                }
            }
        });
        new DatabaseTaskRunner().executeAsync(() -> GradeDatabase.get(getApplicationContext()).semesterDao().selectBySemesterId(semester_id), this::setSemester);
    }

    private void setSemester(Semester semester) {
        this.semester = semester;
    }

    @Override
    public void save(View view) {
        final String name = input_create_subject_name.getText().toString().trim();
        if (subject_id == -1) {
            Subject subject = new Subject(name, semester);
            new DatabaseTaskRunner().executeAsync(() -> {
                GradeDatabase.get(getApplicationContext()).subjectDao().insertAll(subject);
                finish();
                return null;
            }, result -> {

            });
        } else {
            new DatabaseTaskRunner().executeAsync(() -> GradeDatabase.get(getApplicationContext()).subjectDao().selectBySubjectId(subject_id), result -> {
                result.setSubject_name(name);
                GradeDatabase.get(getApplicationContext()).subjectDao().update(result);
                finish();
            });
        }
    }

    @Override
    public void cancel(View view) {
        finish();
    }
}