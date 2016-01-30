package com.toumlilt.gameoftrones;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public abstract class GotDbHelper extends SQLiteOpenHelper {

    protected final String TAG = "GotDbHelper";

    public static final String DATABASE_NAME = "ppm.db";
    private static final int DATABASE_VERSION = 1;

    public GotDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public abstract void onCreate(SQLiteDatabase db);
    @Override
    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}