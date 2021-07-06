package com.example.reminders;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Reminders_4.db";
    private static final String TABLE_NAME = "rem_list";
    private static final String COL_HEAD = "HEAD";
    private static final String COL_ID = "ID";
    private static final String COL_DATE = "DATE";
    private static final String COL_TIME = "TIME";
    private static final String COL_MSG = "MSG";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ TABLE_NAME+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_HEAD+" TEXT, "+COL_MSG+" TEXT, "+COL_DATE+" TEXT, "+COL_TIME+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertDataDB (String head, String msg, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_HEAD, head);
        contentValues.put(COL_MSG, msg);
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_TIME, time);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean updateDataDB (String id, String head, String msg, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_HEAD, head);
        contentValues.put(COL_MSG, msg);
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_TIME, time);

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[] { id+"" });
        return true;
    }

    public int deleteDataDB (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {id + ""});
    }


    public Cursor getDataInACursorById(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE ID =?", new String[] {id+""});
    }

    public Cursor getDataInACursor() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
    }

    public ArrayList<RemindersItems> getAllData () {
        ArrayList<RemindersItems> remList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);

        if(cursor == null) {
            return null;
        }

        while (cursor.moveToNext()) {
            String id = String.valueOf(cursor.getInt(cursor.getColumnIndex(COL_ID)));
            String head = cursor.getString(cursor.getColumnIndex(COL_HEAD));
            String msg = cursor.getString(cursor.getColumnIndex(COL_MSG));
            String date = cursor.getString(cursor.getColumnIndex(COL_DATE));
            String time = cursor.getString(cursor.getColumnIndex(COL_TIME));

            RemindersItems remindersItems = new RemindersItems(id,head, msg, date, time);
            remList.add(remindersItems);
        }
        return remList;
    }

}
