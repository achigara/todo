package com.achigara.todo.main.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "todo_items")
public class TodoItem implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String description;
    private int type;
    private long alarmTime;
    private long alarmInterval;

    public TodoItem(long id, String title, String description, int type, long alarmTime, long alarmInterval) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.alarmTime = alarmTime;
        this.alarmInterval = alarmInterval;
    }

    @Ignore
    public TodoItem(String title, String description, int type, long alarmTime, long alarmInterval) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.alarmTime = alarmTime;
        this.alarmInterval = alarmInterval;
    }

    @Ignore
    public TodoItem() {
        this.title = "";
        this.description = "";
        this.type = 1;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
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

    public boolean hasAlarm() {
        return (alarmTime > 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(type);
        dest.writeLong(alarmTime);
        dest.writeLong(alarmInterval);
    }

    private TodoItem(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
        type = in.readInt();
        alarmTime = in.readLong();
        alarmInterval = in.readLong();
    }

    public static final Creator<TodoItem> CREATOR = new Creator<TodoItem>() {
        @Override
        public TodoItem createFromParcel(Parcel in) {
            return new TodoItem(in);
        }

        @Override
        public TodoItem[] newArray(int size) {
            return new TodoItem[size];
        }
    };
}
