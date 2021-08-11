package com.sowatec.pg.notenapp.activity.abstract_;

import android.view.View;

public interface AbstractListActivity extends AbstractActivity {

    void populateList();

    void viewElement(View view);

    void editElement(View view);

    void createElement(View view);

}
