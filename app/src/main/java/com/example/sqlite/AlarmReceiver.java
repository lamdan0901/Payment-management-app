package com.example.sqlite;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.sqlite.dal.SQLiteHelper;

import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    private final String CHANNEL_ID = "100";
    private SQLiteHelper db;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("send alarm")) {
            String time = intent.getStringExtra("time");
            db = new SQLiteHelper(context);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel100", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("channel desc");
            notificationManager.createNotificationChannel(channel);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle("Nhắc nhở")
                    .setContentText("Bây giờ là " + time + ". Nhớ " + db.getReminder())
                    .setSmallIcon(R.drawable.ic_logo3)
                    .setColor(Color.rgb(1, 135, 134))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_REMINDER);
            notificationManager.notify(getNotiId(), builder.build());
        }
    }

    private int getNotiId() {
        return (int) new Date().getTime();
    }
}
