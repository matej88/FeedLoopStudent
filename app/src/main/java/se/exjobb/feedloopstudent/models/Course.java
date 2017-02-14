package se.exjobb.feedloopstudent.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by matej on 2017-02-14.
 */

public class Course implements Parcelable {

    private String name;
    private String code;
    private String teacher;
    private String teacherUid;
    private boolean isOnline;
    private Map<String, Boolean> subscribers;


    @Exclude
    private String key;


    public Course() {
    }

    public Course(String name, String code, String teacher, String teacherUid) {
        this.name = name;
        this.code = code;
        this.teacher = teacher;
        this.teacherUid = teacherUid;
        isOnline = false;
        subscribers = new HashMap<>();
    }


    protected Course(Parcel in) {
        name = in.readString();
        code = in.readString();
        teacher = in.readString();
        teacherUid = in.readString();
        isOnline = in.readByte() != 0;
        key = in.readString();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getTeacherUid() {
        return teacherUid;
    }

    public void setTeacherUid(String teacherUid) {
        this.teacherUid = teacherUid;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, Boolean> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Map<String, Boolean> subscribers) {
        this.subscribers = subscribers;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(code);
        parcel.writeString(teacher);
        parcel.writeString(teacherUid);
        parcel.writeByte((byte) (isOnline ? 1 : 0));
        parcel.writeString(key);
    }
}