package com.application.android.edthelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by android on 09/03/2016.
 */
public class EventSql extends SQLiteOpenHelper {

    public static final String TABLE = "EDT";
    public static final String ID = "ID";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String LIEU = "LIEU";
    public static final String DEBUT = "DEBUT";
    public static final String FIN = "FIN";

    public static final int NUMID = 0;
    public static final int NUMDESCRIPTION = 1;
    public static final int NUMLIEU = 2;
    public static final int NUMDEBUT = 3;
    public static final int NUMFIN = 4;

    private static final String CREATEBDD = "CREATE TABLE " +
            TABLE + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DESCRIPTION + " VARCHAR(255), " +
            LIEU + " VARCHAR(255), " +
            DEBUT + " DATETIME, " +
            FIN + " DATETIME);";

    public EventSql(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATEBDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE);
        onCreate(db);
    }

}
