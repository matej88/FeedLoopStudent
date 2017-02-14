package se.exjobb.feedloopstudent.models;

import android.os.Parcel;
import android.os.Parcelable;


import com.google.firebase.database.Exclude;

/**
 * Created by matej on 2017-01-06.
 */

public class Question implements Parcelable{

    @Exclude
    private String key;

    private String question;
    private String answerRef;

    public Question() {
    }

    public Question(String question, String answerRef) {
        this.question = question;
        this.answerRef = answerRef;
    }

    public Question(String question){
        this.question = question;
    }

    protected Question(Parcel in) {
        key = in.readString();
        question = in.readString();
        answerRef = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswerRef() {
        return answerRef;
    }

    public void setAnswerRef(String answerRef) {
        this.answerRef = answerRef;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static Creator<Question> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(question);
        parcel.writeString(answerRef);
    }
}
