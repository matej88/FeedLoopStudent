package se.exjobb.feedloopstudent.models;

import com.google.firebase.database.Exclude;

/**
 * Created by matej on 2017-01-06.
 */

public class Answer {


    @Exclude
    private String key;


    private String answer;
    private String questionKey;


    public Answer() {
    }

    public Answer(String answer, String questionKey) {
        this.questionKey = questionKey;
        this.answer = answer;
    }



    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getQuestionKey() {
        return questionKey;
    }

    public void setQuestionKey(String questionKey) {
        this.questionKey = questionKey;
    }


}
