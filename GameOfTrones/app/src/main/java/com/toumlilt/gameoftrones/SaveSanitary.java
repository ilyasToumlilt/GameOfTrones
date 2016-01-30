package com.toumlilt.gameoftrones;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class SaveSanitary extends SQLiteOpenHelper {

    private static final String TAG = "SaveSanitary";

    public static final String DATABASE_NAME = "ppm.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_SANITARY = "sanitary";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_REMAINING_LIFE = "remaining_life";

    private static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_SANITARY + "(" +
                    COLUMN_LATITUDE + " REAL not null, " +
                    COLUMN_LONGITUDE + " REAL not null, " +
                    COLUMN_REMAINING_LIFE + " INTEGER not null, " +
                    "PRIMARY KEY ("+COLUMN_LATITUDE+", "+COLUMN_LONGITUDE+") "+
                    "ON CONFLICT IGNORE "+
                    ");";

    private static final String DATABASE_DROP =
            "DROP TABLE IF EXISTS " + TABLE_SANITARY;
    private static final String SANITARY_COUNT =
            "SELECT COUNT(*) FROM" + TABLE_SANITARY;
    private static final String SANITARY_ALL =
            "SELECT "+COLUMN_LATITUDE+ ", " + COLUMN_LONGITUDE + ", "+ COLUMN_REMAINING_LIFE + " "+
                "FROM " + TABLE_SANITARY;

    public SaveSanitary(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, DATABASE_CREATE);
        db.execSQL(this.DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "De " + oldVersion + " vers " + newVersion);
        db.execSQL(DATABASE_DROP);
        this.onCreate(db);
    }

    public int count(){
        SQLiteDatabase db;
        Cursor mCount;
        int count=0;

        db = this.getReadableDatabase();
        mCount=db.rawQuery(SANITARY_COUNT, null);
        mCount.moveToFirst();
        mCount.getInt(0);
        mCount.close();

        return count;
    }

    public ArrayList<Sanitary> getAll()
    {
        ArrayList<Sanitary> sas;
        SQLiteDatabase db;
        Cursor mAll;
        Sanitary curSan;

        sas = new ArrayList<>();
        db = this.getReadableDatabase();
        mAll=db.rawQuery(SANITARY_ALL, null);
        mAll.moveToFirst();

        while(!mAll.isAfterLast())
        {
            curSan = new Sanitary(
                    mAll.getDouble(0),
                    mAll.getDouble(1),
                    mAll.getInt(2)
            );

            sas.add(curSan);
            mAll.moveToNext();
        }

        return sas;
    }

    boolean insert(Sanitary s)
    {
        ContentValues cv;
        SQLiteDatabase db;
        boolean ret;

        db =  this.getWritableDatabase();
        cv = new ContentValues();

        cv.put(COLUMN_LATITUDE, s.getLatitude());
        cv.put(COLUMN_LONGITUDE, s.getLongitude());
        cv.put(COLUMN_REMAINING_LIFE, s.getRemainingLife());

        ret = db.insert(TABLE_SANITARY, null, cv) != -1;
        db.close();

        return ret;
    }
}