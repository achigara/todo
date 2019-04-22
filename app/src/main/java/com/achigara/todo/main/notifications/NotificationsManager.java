package com.achigara.todo.main.notifications;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.achigara.todo.R;
import com.achigara.todo.main.model.TodoItem;
import com.google.gson.Gson;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationsManager {

    static final String EXTRA_NOTIFICATION_TODO_ITEM = "extraNotificationTodoItem";
    static final String CANCELABLE_ALARM = "cancelableAlarm";
    static final String CANCEL_NOTIFICATION_ALARM = "cancelNotificationAlarm";
    private static NotificationsManager notificationsManager;
    private final String INTENT_CATEGORY_CANCEL = "cancel";
    private Application application;

    private NotificationsManager(Application application) {
        this.application = application;
    }

    public static NotificationsManager getInstance(Application application) {
        if (notificationsManager == null) {
            notificationsManager = new NotificationsManager(application);
        }
        return notificationsManager;
    }

    void showNotification(TodoItem item, boolean cancelable) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(application);
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(application, application.getString(R.string.default_notification_channel));

        if (cancelable) {
            Intent notificationIntent = new Intent(application, NotificationAlarmReceiver.class);
            notificationIntent.addCategory(INTENT_CATEGORY_CANCEL);
            notificationIntent.putExtra(CANCEL_NOTIFICATION_ALARM, true);
            notificationIntent.putExtra(EXTRA_NOTIFICATION_TODO_ITEM, new Gson().toJson(item));

            PendingIntent cancelAlarmIntent = PendingIntent.getBroadcast(application, (int) (item.getId() % Integer.MAX_VALUE), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.addAction(R.drawable.ic_cancel, application.getString(R.string.cancel), cancelAlarmIntent);
        }

        Notification notification = builder.setContentTitle(item.getTitle())
                .setContentText(item.getDescription())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)
                .build();

        notificationManager.notify((int) (item.getId() % Integer.MAX_VALUE), notification);
    }

    public void scheduleNotification(TodoItem item) {

        if (item.hasAlarm()) {
            AlarmManager alarmManager = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);

            Intent notificationIntent = new Intent(application, NotificationAlarmReceiver.class);
            notificationIntent.putExtra(EXTRA_NOTIFICATION_TODO_ITEM, new Gson().toJson(item));
            if (item.repeatAlarm()) {
                notificationIntent.putExtra(CANCELABLE_ALARM, true);
            }

            PendingIntent alarmIntent = PendingIntent.getBroadcast(application, (int) (item.getId() % Integer.MAX_VALUE), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (item.repeatAlarm()) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, item.getAlarmTime(), item.getAlarmInterval(), alarmIntent);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, item.getAlarmTime(), alarmIntent);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, item.getAlarmTime(), alarmIntent);
                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, item.getAlarmTime(), alarmIntent);
                    }
                }
            }
        }
    }

    void hideNotification(int id) {
        NotificationManagerCompat.from(application).cancel(id);
    }
}
