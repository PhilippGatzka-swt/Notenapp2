package com.sowatec.pg.notenapp.activity.abstract_;

import android.view.View;

import com.sowatec.pg.notenapp.activity.list.ActionMenu;

public interface AbstractListActivity extends AbstractActivity, ActionMenu {

    void populateList();

    void viewElement(View view);

    void editElement(View view);

    void createElement(View view);

}
