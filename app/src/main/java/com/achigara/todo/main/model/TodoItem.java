package com.achigara.todo.main.model;

public class TodoItem {

    private long id;
    private String title;
    private String description;
    private short type;
    private long alarmTime;
    private long alarmInterval;

    public TodoItem(long id, String title, String description, short type, long alarmTime, long alarmInterval) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.alarmTime = alarmTime;
        this.alarmInterval = alarmInterval;
    }

    public TodoItem(String title, String description, short type, long alarmTime, long alarmInterval) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.alarmTime = alarmTime;
        this.alarmInterval = alarmInterval;
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

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
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
}
