package se.exjobb.feedloopstudent.models;

import com.google.firebase.database.Exclude;

/**
 * Created by matej on 2017-01-04.
 */

public class Feedback {

    private String username;
    private long timestamp;
    private String feedback;
    private int rating;
    private String sessionKey;
    private String courseKey;
    private boolean isTeacher;
    boolean isReplied;
    private String answer;

    @Exclude
    String feedbackKey;

    public Feedback(){

    }
    public Feedback(String feedback, int rating, long timestamp, String sessionKey, String courseKey, boolean isTeacher) {
        this.feedback = feedback;
        this.rating = rating;
        this.timestamp = timestamp;
        this.sessionKey = sessionKey;
        this.courseKey = courseKey;
        this.isTeacher = isTeacher;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getCourseKey() {
        return courseKey;
    }

    public void setCourseKey(String courseKey) {
        this.courseKey = courseKey;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }

    public boolean isReplied() {
        return isReplied;
    }

    public void setReplied(boolean replied) {
        isReplied = isReplied;
    }

    public String getFeedbackKey() {
        return feedbackKey;
    }

    public void setFeedbackKey(String feedbackKey) {
        this.feedbackKey = feedbackKey;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
