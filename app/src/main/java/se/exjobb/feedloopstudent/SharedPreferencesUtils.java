package se.exjobb.feedloopstudent;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by matej on 2017-01-15.
 */

public class SharedPreferencesUtils {

    public static String getCurrentCourseKey(Context context){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        return prefs.getString(Constants.COURSE_KEY, "");
    }

    public static void setCurrentCourseKey(Context context, String courseKey){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.COURSE_KEY, courseKey);
        editor.commit();
    }

    public static String getCurrentStudentName (Context context){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        return prefs.getString(Constants.STUDENT_NAME, "");
    }


    public static void setCurrentStudentName(Context context, String teacherName){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.STUDENT_NAME, teacherName);
        editor.commit();
    }

    public static String getCurrentStudentUid (Context context){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        return prefs.getString(Constants.STUDENT_UID, "");
    }

    public static void setCurrentStudentUid(Context context, String studentUID){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.STUDENT_UID, studentUID);
        editor.commit();
    }

    public static String getCurrentSurveyKey (Context context){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        return prefs.getString(Constants.SURVEY_KEY, "");
    }

    public static void setCurrentSurveyKey(Context context, String surveyKey){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.SURVEY_KEY, surveyKey);
        editor.commit();
    }

    public static String getCurrentSurveyName (Context context){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        return prefs.getString(Constants.SURVEY_NAME, "");
    }

    public static void setCurrentSurveyName(Context context, String surveyKey){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.SURVEY_NAME, surveyKey);
        editor.commit();
    }


    public static String getCurrentSession (Context context){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        return prefs.getString(Constants.SESSION_KEY, "");
    }

    public static void setIsTeacher(Context context, boolean b){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("Constants.IS_TEACHER", b);
        editor.commit();
    }
    public static boolean getIsTeacher (Context context){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        return prefs.getBoolean("Constants.IS_TEACHER", false);
    }

    public static void setCurrentSessionKey(Context context, String sessionKey){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.SESSION_KEY, sessionKey);
        editor.commit();
    }
    public static boolean getIsOnline (Context context){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        return prefs.getBoolean("Constants.IS_ONLINE", false);
    }

    public static void setIsOnline(Context context, boolean b){
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("Constants.IS_ONLINE", false);
        editor.commit();
    }
}
