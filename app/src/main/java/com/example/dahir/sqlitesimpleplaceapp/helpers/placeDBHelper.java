package com.example.dahir.sqlitesimpleplaceapp.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.dahir.sqlitesimpleplaceapp.models.PendingplaceModel;

import java.util.ArrayList;

public class placeDBHelper {
    private Context context;
    private DatabaseHelper databaseHelper;

    public placeDBHelper(Context context){
        this.context=context;
        databaseHelper=new DatabaseHelper(context);
    }

    //add new places into the database
    public boolean addNewplace(PendingplaceModel pendingplaceModel){
        SQLiteDatabase sqLiteDatabase=this.databaseHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DatabaseHelper.COL_place_TITLE,pendingplaceModel.getplaceTitle());
        contentValues.put(DatabaseHelper.COL_place_CONTENT,pendingplaceModel.getplaceContent());
        contentValues.put(DatabaseHelper.COL_place_TAG,pendingplaceModel.getplaceTag());
        contentValues.put(DatabaseHelper.COL_place_DATE,pendingplaceModel.getplaceDate());
        sqLiteDatabase.insert(DatabaseHelper.TABLE_place_NAME,null,contentValues);
        sqLiteDatabase.close();
        return true;
    }

    //count places from the database
    public int countplaces(){
        SQLiteDatabase sqLiteDatabase=this.databaseHelper.getReadableDatabase();
        String count="SELECT " + DatabaseHelper.COL_place_ID + " FROM " + DatabaseHelper.TABLE_place_NAME;
        Cursor cursor=sqLiteDatabase.rawQuery(count,null);
        return cursor.getCount();
    }

    //fetch all the places from the database
    public ArrayList<PendingplaceModel> fetchAllplaces(){
        SQLiteDatabase sqLiteDatabase=this.databaseHelper.getReadableDatabase();
        ArrayList<PendingplaceModel> pendingplaceModels=new ArrayList<>();
        String query="SELECT * FROM " + DatabaseHelper.TABLE_place_NAME+" INNER JOIN " + DatabaseHelper.TABLE_TAG_NAME+" ON " + DatabaseHelper.TABLE_place_NAME+"."+DatabaseHelper.COL_place_TAG+"="+
                DatabaseHelper.TABLE_TAG_NAME+"."+DatabaseHelper.COL_TAG_ID + " ORDER BY " + DatabaseHelper.TABLE_place_NAME+"."+DatabaseHelper.COL_place_ID + " ASC";
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        while (cursor.moveToNext()){
            PendingplaceModel pendingplaceModel=new PendingplaceModel();
            pendingplaceModel.setplaceID(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_place_ID)));
            pendingplaceModel.setplaceTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_place_TITLE)));
            pendingplaceModel.setplaceContent(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_place_CONTENT)));
            pendingplaceModel.setplaceTag(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TAG_TITLE)));
            pendingplaceModel.setplaceDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_place_DATE)));
            pendingplaceModels.add(pendingplaceModel);
        }
        cursor.close();
        sqLiteDatabase.close();
        return pendingplaceModels;
    }

    //update places according to the places id
    public boolean updateplace(PendingplaceModel pendingplaceModel){
        SQLiteDatabase sqLiteDatabase=this.databaseHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DatabaseHelper.COL_place_TITLE,pendingplaceModel.getplaceTitle());
        contentValues.put(DatabaseHelper.COL_place_CONTENT,pendingplaceModel.getplaceContent());
        contentValues.put(DatabaseHelper.COL_place_TAG,pendingplaceModel.getplaceTag());
        contentValues.put(DatabaseHelper.COL_place_DATE,pendingplaceModel.getplaceDate());
        sqLiteDatabase.update(DatabaseHelper.TABLE_place_NAME,contentValues,DatabaseHelper.COL_place_ID+"=?",new String[]{String.valueOf(pendingplaceModel.getplaceID())});
        sqLiteDatabase.close();
        return true;
    }

    //remove places according to the places id
    public boolean removeplace(int placeID){
        SQLiteDatabase sqLiteDatabase=this.databaseHelper.getReadableDatabase();
        sqLiteDatabase.delete(DatabaseHelper.TABLE_place_NAME,DatabaseHelper.COL_place_ID+"=?",new String[]{String.valueOf(placeID)});
        sqLiteDatabase.close();
        return true;
    }


    //fetch places title from the database according the places id
    public String fetchplaceTitle(int placeID){
        SQLiteDatabase sqLiteDatabase=this.databaseHelper.getReadableDatabase();
        String query="SELECT " + DatabaseHelper.COL_place_TITLE + " FROM " + DatabaseHelper.TABLE_place_NAME + " WHERE " + DatabaseHelper.COL_place_ID+"=?";
        Cursor cursor=sqLiteDatabase.rawQuery(query,new String[]{String.valueOf(placeID)});
        String title="";
        if(cursor.moveToFirst()){
            title=cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_place_TITLE));
        }
        cursor.close();
        sqLiteDatabase.close();
        return title;
    }

    //fetch places content from the database according the places id
    public String fetchplaceContent(int placeID){
        SQLiteDatabase sqLiteDatabase=this.databaseHelper.getReadableDatabase();
        String query="SELECT " + DatabaseHelper.COL_place_CONTENT + " FROM " + DatabaseHelper.TABLE_place_NAME + " WHERE " + DatabaseHelper.COL_place_ID+"=?";
        Cursor cursor=sqLiteDatabase.rawQuery(query,new String[]{String.valueOf(placeID)});
        String content="";
        if(cursor.moveToFirst()){
            content=cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_place_CONTENT));
        }
        cursor.close();
        sqLiteDatabase.close();
        return content;
    }

    //fetch places date from the database according the places id
    public String fetchplaceDate(int placeID){
        SQLiteDatabase sqLiteDatabase=this.databaseHelper.getReadableDatabase();
        String query="SELECT " + DatabaseHelper.COL_place_DATE + " FROM " + DatabaseHelper.TABLE_place_NAME + " WHERE " + DatabaseHelper.COL_place_ID+"=?";
        Cursor cursor=sqLiteDatabase.rawQuery(query,new String[]{String.valueOf(placeID)});
        String date="";
        if(cursor.moveToFirst()){
            date=cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_place_DATE));
        }
        cursor.close();
        sqLiteDatabase.close();
        return date;
    }
}
