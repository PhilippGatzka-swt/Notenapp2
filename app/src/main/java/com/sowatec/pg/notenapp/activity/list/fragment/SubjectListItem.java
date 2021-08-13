package com.sowatec.pg.notenapp.activity.list.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sowatec.pg.notenapp.R;
import com.sowatec.pg.notenapp.room.DatabaseTaskRunner;
import com.sowatec.pg.notenapp.room.GradeDatabase;
import com.sowatec.pg.notenapp.room.entity.Subject;

public class SubjectListItem extends AbstractListItem<Subject> {
    public SubjectListItem(Subject entity, Context context) {
        super(entity, context);
        init();
    }

    @SuppressLint("SetTextI18n")
    private void init() {

        setGravity(Gravity.CENTER_VERTICAL);

        TextView average = new TextView(getContext());
        average.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
        average.setTextAlignment(TEXT_ALIGNMENT_VIEW_END);
        average.setTextSize(17);

        TextView name = new TextView(getContext());
        name.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        name.setTextSize(19);
        name.setTextColor(Color.BLACK);

        TextView elements = new TextView(getContext());
        elements.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        elements.setTextSize(17);

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        name.setText(getEntity().getSubject_name());
        new DatabaseTaskRunner().executeAsync(() -> GradeDatabase.get(getContext()).subjectDao().selectSubjectGradeCount(getEntity().getSubject_id()), result -> {
            int str = result == 1 ? R.string.element : R.string.elements;
            elements.setText(getContext().getString(str, result));
        });
        new DatabaseTaskRunner().executeAsync(() -> GradeDatabase.get(getContext()).subjectDao().selectSubjectAverage(getEntity().getSubject_id()), result -> average.setText(result + ""));
        layout.addView(name);
        layout.addView(elements);
        addView(layout);
        addView(average);
    }
}
