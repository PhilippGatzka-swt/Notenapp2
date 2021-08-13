package com.sowatec.pg.notenapp.activity.list.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.LinearLayout;

@SuppressLint("ViewConstructor")
public class AbstractListItem<T> extends LinearLayout {
    private final T entity;

    public AbstractListItem(T entity, Context context) {
        super(context);
        this.entity = entity;
        setup();
    }

    private void setup() {
        setPadding(30, 30, 30, 30);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    public T getEntity() {
        return entity;
    }
}
