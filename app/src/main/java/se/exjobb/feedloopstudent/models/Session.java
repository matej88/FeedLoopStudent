package se.exjobb.feedloopstudent.models;


import com.google.firebase.database.Exclude;


public class Session {

    private int sessionId;
    private boolean finished;

    @Exclude
    private String sessionKey;



    public Session() {
    }

    public Session(int sessionId, boolean finished) {
        this.sessionId = sessionId;
        this.finished = finished;

    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

}
