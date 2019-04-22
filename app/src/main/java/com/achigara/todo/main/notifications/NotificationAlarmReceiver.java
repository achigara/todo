package com.achigara.todo.main.notifications;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.achigara.todo.main.model.TodoItem;
import com.google.gson.Gson;

public class NotificationAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null && intent.hasExtra(NotificationsManager.EXTRA_NOTIFICATION_TODO_ITEM)) {
            TodoItem item = new Gson().fromJson(intent.getStringExtra(NotificationsManager.EXTRA_NOTIFICATION_TODO_ITEM), TodoItem.class);

            if (item != null) {
                if (intent.getBooleanExtra(NotificationsManager.CANCEL_NOTIFICATION_ALARM, false)) {
                    Intent notificationIntent = new Intent(context.getApplicationContext(), NotificationAlarmReceiver.class);
                    PendingIntent alarmIntent = PendingIntent.getBroadcast(context.getApplicationContext(), (int) (item.getId() % Integer.MAX_VALUE), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).cancel(alarmIntent);
                    NotificationsManager.getInstance((Application) context.getApplicationContext())
                            .hideNotification((int) (item.getId() % Integer.MAX_VALUE));
                } else {
                    boolean cancelable = intent.getBooleanExtra(NotificationsManager.CANCELABLE_ALARM, false);

                    NotificationsManager.getInstance((Application) context.getApplicationContext())
                            .showNotification(item, cancelable);
                }
            }
        }
    }
}
