package com.achigara.todo.main.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "todo_items")
public class TodoItem {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String description;
    private long alarmTime;
    private long alarmInterval;

    public TodoItem(long id, String title, String description, long alarmTime, long alarmInterval) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.alarmTime = alarmTime;
        this.alarmInterval = alarmInterval;
    }

    @Ignore
    public TodoItem(String title, String description, long alarmTime, long alarmInterval) {
        this.title = title;
        this.description = description;
        this.alarmTime = alarmTime;
        this.alarmInterval = alarmInterval;
    }

    @Ignore
    public TodoItem() {
        this.title = "";
        this.description = "";
        this.alarmTime = 0;
        this.alarmInterval = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(long alarmTime) {
        this.alarmTime = alarmTime;
    }

    public long getAlarmInterval() {
        return alarmInterval;
    }

    public void setAlarmInterval(long alarmInterval) {
        this.alarmInterval = alarmInterval;
    }

    public boolean hasAlarm() {
        return (alarmTime > 0);
    }

    public boolean repeatAlarm() {
        return (hasAlarm() && alarmInterval > 0);
    }
}
