package com.example.reminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper mainDB;
    int dataListSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainDB = new DatabaseHelper(this);

        Fragment listFragment = new ListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.view_fl, listFragment).commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void addDataInDB (String head, String msg, String date, String time) {

        boolean ifDataIsInserted =  mainDB.insertDataDB(head, msg, date, time);

        if (ifDataIsInserted) {
            Toast.makeText(this, "Data has been inserted in DB", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "unable to insert in DB", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<RemindersItems> getRemDataFromAct () {
        ArrayList<RemindersItems> arrayList;
        arrayList = mainDB.getAllData();
        return arrayList;
    }

    public String getRemDataFromActById (String id, String column) {
        Cursor DataInCursor = mainDB.getDataInACursorById(id);
        String DataInString = "";
        if (DataInCursor.getCount() > 0) {
            DataInCursor.moveToFirst();
            DataInString = DataInCursor.getString(DataInCursor.getColumnIndex(column));
        }
        return DataInString;
    }


    public void updateBDbyAct (String id, String head, String msg, String date, String time) {
        boolean DBUpdated = mainDB.updateDataDB(id,head,msg,date,time);

        if (DBUpdated) {
            Toast.makeText(this, "Reminder Updated", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Reminder update failed!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteDataDBbyAct (String id) {
        int deletedRows = mainDB.deleteDataDB(id);
        if (deletedRows > 0) {
            Toast.makeText(this, "Reminder deleted successfully", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.e("ADD","DATA DELETION FAILED WHEN IN ACT");
            Toast.makeText(this, "Reminder deletion failed!!", Toast.LENGTH_SHORT).show();
        }
    }


    //notification methods here
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void startAlarmByAct (Calendar calendar, String head, String msg, int id) {
        Log.e("main", String.valueOf(id));
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("head",head);
        intent.putExtra("msg",msg);
        intent.putExtra("notiID", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_ONE_SHOT);

        assert alarmManager != null;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public void cancelAlarm (String id) {
        int m = Integer.parseInt(id);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, m, intent, PendingIntent.FLAG_ONE_SHOT);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
    }


    public int DataListSize () {
        Cursor cursor = mainDB.getDataInACursor();
        cursor.moveToLast();
        String size = String.valueOf(cursor.getInt(cursor.getColumnIndex("ID")));
        this.dataListSize = Integer.parseInt(size);
        return this.dataListSize;
    }
}