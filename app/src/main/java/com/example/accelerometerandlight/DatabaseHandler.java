package com.example.accelerometerandlight;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(Context context){
        super(context, "Database1", null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
String CreateTable = "create table Table1 (xValue INTEGER, yValue DOUBLE)";
db.execSQL(CreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void insertDate(long ValX, double ValY){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("xValue", ValX);
        contentValues.put("yValue", ValY);
        database.insert("Table1", null, contentValues);


    }
}
