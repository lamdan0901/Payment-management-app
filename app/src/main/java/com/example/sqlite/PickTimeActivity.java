package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sqlite.dal.SQLiteHelper;

public class PickTimeActivity extends AppCompatActivity {
    private TimePicker timePicker;
    private Button btSet, btCancel, btAbort;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private SQLiteHelper db;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_time);
        btCancel = findViewById(R.id.btCancel);
        btSet = findViewById(R.id.btSet);
        btAbort = findViewById(R.id.btAbort);
        timePicker = findViewById(R.id.timePicker);
        db = new SQLiteHelper(this);
        intent = new Intent(PickTimeActivity.this, AlarmReceiver.class);

        btSet.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
            calendar.set(Calendar.MINUTE, timePicker.getMinute());

            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            String Hour = String.valueOf(hour);
            String Minute = String.valueOf(minute);
            String amPm="AM";

            if (hour > 11){
                if(hour>12){
                    Hour = String.valueOf(hour - 12);
                }
                amPm="PM";
            }
            if (minute < 10){
                Minute = "0" + minute;
            }

            String time = Hour + ":" + Minute + " "+ amPm;
            Toast.makeText(this, "Đã chọn thời gian nhắc nhở vào " + time, Toast.LENGTH_SHORT).show();

            intent.setAction("send alarm");
            intent.putExtra("time", time);

            pendingIntent = PendingIntent.getBroadcast(PickTimeActivity.this, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            db.setReminderTime(time);
            finish();
        });

        btAbort.setOnClickListener(view -> {
            if (!db.getReminderTime().equals("")) {
                db.setReminderTime("");
                Toast.makeText(this, "Đã hủy bỏ", Toast.LENGTH_SHORT).show();

                pendingIntent = PendingIntent.getBroadcast(PickTimeActivity.this, 0,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);
                pendingIntent.cancel();
            }
            else{
                Toast.makeText(this, "Chưa có giờ thời gian nhắc nhở nào!", Toast.LENGTH_SHORT).show();
            }
            finish();
        });

        btCancel.setOnClickListener(view -> {
            finish();
        });
    }
}