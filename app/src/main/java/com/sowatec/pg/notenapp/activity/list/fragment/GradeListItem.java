package com.sowatec.pg.notenapp.activity.list.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sowatec.pg.notenapp.room.entity.Grade;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GradeListItem extends AbstractListItem<Grade> {
    public GradeListItem(Grade entity, Context context) {
        super(entity, context);
        init();
    }

    @SuppressLint("SetTextI18n")
    private void init() {

        setGravity(Gravity.CENTER_VERTICAL);

        TextView grade = new TextView(getContext());
        grade.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
        grade.setTextAlignment(TEXT_ALIGNMENT_VIEW_END);
        grade.setTextSize(17);

        TextView name = new TextView(getContext());
        name.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        name.setTextSize(19);
        name.setTextColor(Color.BLACK);

        TextView date = new TextView(getContext());
        date.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        date.setTextSize(17);

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        name.setText(getEntity().getGrade_name());

        Date d = new Date(getEntity().getGrade_date());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String str = simpleDateFormat.format(d);
        date.setText(str);

        grade.setText(getEntity().getGrade_grade() + "");
        layout.addView(name);
        layout.addView(date);
        addView(layout);
        addView(grade);
    }
}
