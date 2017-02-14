package se.exjobb.feedloopstudent.models;

import java.util.Map;

/**
 * Created by matej on 2017-02-14.
 */

public class Student {

    private String name;
    private Map<String,Boolean> subscribedto;

    public Student() {
    }

    public Student(String name, Map<String, Boolean> subscribedto) {
        this.name = name;
        this.subscribedto = subscribedto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Boolean> getSubscribedto() {
        return subscribedto;
    }

    public void setSubscribedto(Map<String, Boolean> subscribedto) {
        this.subscribedto = subscribedto;
    }
}
