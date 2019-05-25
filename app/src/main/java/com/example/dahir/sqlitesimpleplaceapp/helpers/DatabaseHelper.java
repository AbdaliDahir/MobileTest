package com.example.dahir.sqlitesimpleplaceapp.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="placemanager";

    //Category || Tag table and columns
    public static final String TABLE_TAG_NAME="tags";
    public static final String COL_TAG_ID="tag_id";
    public static final String COL_TAG_TITLE="tag_title";

    //places table and columns
    public static final String TABLE_place_NAME="places";
    public static final String COL_place_ID="place_id";
    public static final String COL_place_TITLE="place_title";
    public static final String COL_place_CONTENT="place_content";
    public static final String COL_place_TAG="place_tag";
    public static final String COL_place_DATE="place_date";
    public static final String COL_DEFAULT_STATUS="pending";

    //forcing foreign key
    public static final String FORCE_FOREIGN_KEY="PRAGMA foreign_keys=ON";

    //creating tags table query
    private static final String CREATE_TAGS_TABLE="CREATE TABLE IF NOT EXISTS " + TABLE_TAG_NAME+"("+
            COL_TAG_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
            COL_TAG_TITLE+" TEXT NOT NULL UNIQUE"+")";

    //creating places table query
    private static final String CREATE_places_TABLE="CREATE TABLE IF NOT EXISTS " + TABLE_place_NAME+"("+COL_place_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+ COL_place_TITLE+" TEXT NOT NULL,"+ COL_place_CONTENT+" TEXT NOT NULL,"+ COL_place_TAG +" INTEGER NOT NULL,"+ COL_place_DATE+" TEXT NOT NULL,"+ COL_DEFAULT_STATUS+",FOREIGN KEY("+COL_place_TAG+") REFERENCES "+TABLE_TAG_NAME+"("+COL_TAG_ID+") ON UPDATE CASCADE ON DELETE CASCADE"+")";

    //dropping tags table
    private static final String DROP_TAGS_TABLE="DROP TABLE IF EXISTS " + TABLE_TAG_NAME;
    //dropping places table
    private static final String DROP_places_TABLE="DROP TABLE IF EXISTS " + TABLE_place_NAME;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TAGS_TABLE);
        sqLiteDatabase.execSQL(CREATE_places_TABLE);
        sqLiteDatabase.execSQL(FORCE_FOREIGN_KEY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TAGS_TABLE);
        sqLiteDatabase.execSQL(DROP_places_TABLE);
        onCreate(sqLiteDatabase);
    }
}
