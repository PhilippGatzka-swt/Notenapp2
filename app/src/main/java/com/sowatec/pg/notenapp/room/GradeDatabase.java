package com.sowatec.pg.notenapp.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.sowatec.pg.notenapp.room.dao.GradeDao;
import com.sowatec.pg.notenapp.room.dao.SemesterDao;
import com.sowatec.pg.notenapp.room.dao.SubjectDao;
import com.sowatec.pg.notenapp.room.entity.Grade;
import com.sowatec.pg.notenapp.room.entity.Semester;
import com.sowatec.pg.notenapp.room.entity.Subject;

@Database(entities = {Semester.class, Subject.class, Grade.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class GradeDatabase extends RoomDatabase {

    public abstract SemesterDao semesterDao();

    public abstract SubjectDao subjectDao();

    public abstract GradeDao gradeDao();

    private static GradeDatabase database;

    public static GradeDatabase get(Context context) {
        if (database == null)
            database = Room.databaseBuilder(context, GradeDatabase.class, "grade-database").build();
        return database;
    }

    public static GradeDatabase get() {
        if (database == null) throw new NullPointerException();
        return database;
    }
}
