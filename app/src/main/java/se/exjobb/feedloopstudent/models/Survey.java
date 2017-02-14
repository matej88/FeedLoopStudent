package se.exjobb.feedloopstudent.models;

import android.os.Parcel;
import android.os.Parcelable;


import com.google.firebase.database.Exclude;

import java.util.Map;

/**
 * Created by matej on 2017-01-05.
 */

public class Survey implements Parcelable {
public static final String COURSE_KEY = "refCode";

    @Exclude
    private String key;

    private String refCode;
    private String surveyName;
    private Map<String,Question> questions;
    private Map<String,Answer> answers;

    public Survey() {
    }

    public Survey(String refCode, String surveyName) {
        this.refCode = refCode;
        this.surveyName = surveyName;
    }


    protected Survey(Parcel in) {
        key = in.readString();
        refCode = in.readString();
        surveyName = in.readString();
    }

    public static final Creator<Survey> CREATOR = new Creator<Survey>() {
        @Override
        public Survey createFromParcel(Parcel in) {
            return new Survey(in);
        }

        @Override
        public Survey[] newArray(int size) {
            return new Survey[size];
        }
    };

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setName(String surveyName) {
        this.surveyName = surveyName;
    }

    public Map<String, Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Map<String, Question> questions) {
        this.questions = questions;
    }

    public Map<String, Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, Answer> answers) {
        this.answers = answers;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(refCode);
        parcel.writeString(surveyName);
    }
}
