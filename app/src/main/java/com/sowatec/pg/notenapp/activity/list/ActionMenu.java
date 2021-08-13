package com.sowatec.pg.notenapp.activity.list;

public interface ActionMenu {
    void menuActionRefresh();

    default void menuActionEmail() {

    }

    void menuActionEdit();

    void menuActionDelete();
}
