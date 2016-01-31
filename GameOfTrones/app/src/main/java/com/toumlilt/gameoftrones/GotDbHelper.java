package com.toumlilt.gameoftrones;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.toumlilt.gameoftrones.model.Sanitary;

import java.util.ArrayList;

/**
 * Classe Controlleur pour le CRUD Sanitary
 */
public class GotDbHelper extends SQLiteOpenHelper {

    protected final String TAG = "GotDbHelper";

    //Base de donnée
    public static final String DATABASE_NAME = "ppm.db";
    private static final int DATABASE_VERSION = 1;

    //Noms des champs et de la table
    private static final String TABLE_SANITARY = "sanitary";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_REMAINING_LIFE = "remaining_life";

    /**
     * Table Sanitary pour enregister les sanisettes.
     * Latitude, longitude et vie restante.
     * La clef primaire est composite sur (latitude, longitude).
     * Les conflits ne sont pas insérés, ils sont ignorés.
     * */
    private static final String SANITARY_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_SANITARY + "(" +
                    COLUMN_LATITUDE + " REAL not null, " +
                    COLUMN_LONGITUDE + " REAL not null, " +
                    COLUMN_REMAINING_LIFE + " INTEGER not null, " +
                    "PRIMARY KEY ("+COLUMN_LATITUDE+", "+COLUMN_LONGITUDE+") "+
                    "ON CONFLICT IGNORE "+
                    ");";

    private static final String SANITARY_DROP =
            "DROP TABLE IF EXISTS " + TABLE_SANITARY;
    private static final String SANITARY_ALL =
            "SELECT "+COLUMN_LATITUDE+ ", " + COLUMN_LONGITUDE + ", "+ COLUMN_REMAINING_LIFE + " "+
                    "FROM " + TABLE_SANITARY;

    /***********************************************************************************************
     * Constructors
     **********************************************************************************************/
    public GotDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, SANITARY_CREATE);
        db.execSQL(SANITARY_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "De " + oldVersion + " vers " + newVersion);
        db.execSQL(SANITARY_DROP);
        this.onCreate(db);
    }

    /**
     * Retourne le nombre de Sanitary dans la table
     * */
    public long count(){
        SQLiteDatabase db;
        long count;

        db = this.getReadableDatabase();
        count  = DatabaseUtils.queryNumEntries(db, TABLE_SANITARY);
        db.close();

        return count;
    }

    /**
     * Retourne tous les Sanitary.
     * @return une collection de Sanitary
     * */
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

        mAll.close();
        db.close();
        return sas;
    }

    /**
     * Insert un nouveau Sanitary dans la table.
     * Si un Sanitary avec une même longitude et longitude existe, on l'ignore.
     * @return vrai si l'insertion a eu lieu, false sinon
     * */
    public boolean insert(Sanitary s)
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

    /**
     * Met-à-jour la vie restante du Sanitary frappé.
     * */
    public void update(Sanitary s){
        ContentValues cv;
        SQLiteDatabase db;

        db =  this.getWritableDatabase();
        cv = new ContentValues();
        cv.put(COLUMN_REMAINING_LIFE, s.getRemainingLife());
        db.update(
                TABLE_SANITARY,
                cv,
                COLUMN_LATITUDE + "=? " + " AND " + COLUMN_LONGITUDE + " =? ",
                new String[]{String.valueOf(s.getLatitude()), String.valueOf(s.getLongitude())}
        );
    }
}