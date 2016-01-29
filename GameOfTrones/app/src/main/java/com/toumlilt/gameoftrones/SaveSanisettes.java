package com.toumlilt.gameoftrones;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SaveSanisettes extends SQLiteOpenHelper {

    private static final String TAG = "SaveSanisettes";

    public static final String DATABASE_NAME = "ppm.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_SANISETTES = "sanisettes";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";

    private static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_SANISETTES + "(" +
                    COLUMN_LATITUDE + " REAL not null, " +
                    COLUMN_LONGITUDE + " REAL not null, " +
                    "PRIMARY KEY ("+COLUMN_LATITUDE+", "+COLUMN_LONGITUDE+") "+
                    "ON CONFLICT IGNORE "+
                    ");";

    public SaveSanisettes(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, DATABASE_CREATE);
        db.execSQL(this.DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "De " + oldVersion + " vers " + newVersion );
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SANISETTES);
        this.onCreate(db);
    }

    public int getNbrSanisettes(){
        SQLiteDatabase db;
        Cursor mCount;
        int count=0;

        db = this.getReadableDatabase();
        mCount=db.rawQuery("select count(*) from " + TABLE_SANISETTES, null);
        mCount.moveToFirst();
        mCount.getInt(0);
        mCount.close();

        return count;
    }

    boolean insert(Sanisette s)
    {
        ContentValues cv;
        SQLiteDatabase db;
        boolean ret;

        ret = false;
        db =  this.getWritableDatabase();
        cv = new ContentValues();

        cv.put(COLUMN_LATITUDE, s.getLatitude());
        cv.put(COLUMN_LONGITUDE, s.getLongitude());

        ret = db.insert(TABLE_SANISETTES, null, cv) != -1;
        db.close();

        return ret;
    }
}