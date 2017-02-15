package se.exjobb.feedloopstudent;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import se.exjobb.feedloopstudent.adapters.CourseAdapter;
import se.exjobb.feedloopstudent.fragments.CourseListFragment;
import se.exjobb.feedloopstudent.fragments.CourseOverviewFragment;
import se.exjobb.feedloopstudent.fragments.FeedbackListTabFragment;
import se.exjobb.feedloopstudent.fragments.LoginFragment;
import se.exjobb.feedloopstudent.fragments.RegisterFragment;
import se.exjobb.feedloopstudent.fragments.SurveyListTabFragment;
import se.exjobb.feedloopstudent.fragments.SurveyOverviewFragment;
import se.exjobb.feedloopstudent.models.Answer;
import se.exjobb.feedloopstudent.models.Course;
import se.exjobb.feedloopstudent.models.Feedback;
import se.exjobb.feedloopstudent.models.Question;

public class MainActivity extends AppCompatActivity
        implements LoginFragment.OnLoginListener,
                    RegisterFragment.onRegisterListener,
                    CourseListFragment.OnCourseSelectedListener,
                    CourseOverviewFragment.CourseOverviewCallback,
        SurveyListTabFragment.OnSurveyClickListener,
        FeedbackListTabFragment.FeedbackTabCallback,
SurveyOverviewFragment.OnSurveyQuestionClickedListener{

    private DatabaseReference mDataRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private OnCompleteListener mOnCompleteListener;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        mAuth = FirebaseAuth.getInstance();
        mDataRef = FirebaseDatabase.getInstance().getReference();
        mDataRef.keepSynced(true);


        switchToLoginFragment();

        initializeListeners();

    }

    public void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle.setText("Feedloop");
    }


    // ---------------------------- Firebase user authentication -----------------------------------


    // Initialize listeners
    private void initializeListeners() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    final String studentUid = user.getUid();
                    SharedPreferencesUtils.setIsTeacher(getApplicationContext(), false);
                    SharedPreferencesUtils.setCurrentStudentUid(getApplicationContext(), studentUid);
                    mDataRef.child("users/students/" + user.getUid() + "/name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String studentName = dataSnapshot.getValue(String.class);
                            // Save current students name in SharedPreferences
                            SharedPreferencesUtils.setCurrentStudentName(getApplicationContext(), studentName);

                            // Save current students uid in SharedPreferences
                            SharedPreferencesUtils.setCurrentStudentUid(getApplicationContext(), studentUid);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    // if user exists go to CourseListFragment
                   switchToCourseListFragment(studentUid);

                } else {
                    switchToLoginFragment();
                }
            }
        };

        // if Login is not complete then show AlertDialog error
        mOnCompleteListener = new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (!task.isSuccessful()) {
                    showLoginError("Login Failed");
                }

            }
        };
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private void switchToCourseListFragment(String studentUid) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        CourseListFragment fragment = CourseListFragment.newInstance(studentUid);
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    // Get the error message for failed login from LoginFragment
    private void showLoginError(String message) {
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("Login");
        loginFragment.onLoginError(message);
    }


    // Check and connect user to a database when Login pressed in LoginFragment
    @Override
    public void onLogin(String email, String passwrod) {

        mAuth.signInWithEmailAndPassword(email, passwrod)
                .addOnCompleteListener(mOnCompleteListener);
    }

    @Override
    public void onRegisterMe() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        RegisterFragment fragment = new RegisterFragment();
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack("back_to_login");
        ft.commit();
    }

    // Sign out when user signs out
    @Override
    public void onLogout() {
        mAuth.signOut();
    }




    @Override
    public void onBackPressed() {
         super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            onLogout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchToLoginFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new LoginFragment(), "Login");
        ft.commit();
    }

    @Override
    public void onRegisterClicked(String name, String email, String password) {
        final String userName = name;
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userUid = mAuth.getCurrentUser().getUid();
                    DatabaseReference currentUserRef = mDataRef.child("users/students/" + userUid);
                    currentUserRef.child("name").setValue(userName);
                    //switchToCourseListFragment(userUid);
                    switchToLoginFragment();
                    Toast.makeText(MainActivity.this, "Registration completed " + userName, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onCourseSelected(Course c) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        CourseOverviewFragment fragment = CourseOverviewFragment.newInstance(c);
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack("back_to_course_list");
        ft.commit();
    }

    @Override
    public void onAddCourse(String courseCode, String studentUid, String courseKey) {

        DatabaseReference studentSubscribeToCourse = mDataRef.child("users").child("students").child(studentUid).child("subscribedto");
        studentSubscribeToCourse.child(courseCode).setValue(true);
        DatabaseReference courseRef = mDataRef.child("courses").child(courseKey).child("subscribers");
        courseRef.child(studentUid).setValue(true);
        Toast.makeText(this, "Subscribed to" + courseCode, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDeleteCourse(Course c) {
        String courseCode = c.getCode();
        String studentUid = SharedPreferencesUtils.getCurrentStudentUid(getApplicationContext());
        String courseKey = SharedPreferencesUtils.getCurrentCourseKey(getApplicationContext());

        DatabaseReference studentSubscribeToCourse = mDataRef.child("users").child("students").child(studentUid).child("subscribedto");
        studentSubscribeToCourse.child(courseCode).removeValue();

        DatabaseReference courseRef = mDataRef.child("courses").child(courseKey).child("subscribers");
        courseRef.child(studentUid).removeValue();

        Toast.makeText(this, "Unsubscribed from " + courseCode, Toast.LENGTH_SHORT).show();


    }


    @Override
    public void startSession() {

    }

    @Override
    public void onSurveyClicked() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        SurveyOverviewFragment fragment = new SurveyOverviewFragment();
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack("back_to_survey_overview");
        ft.commit();
    }

    @Override
    public void onAddSurveyClicked(String courseKey) {

    }

    @Override
    public void questionReply(String answer, String feedbackKey) {

    }

    @Override
    public void onQuestionClicked(final Question question) {
        final String surveyKey = SharedPreferencesUtils.getCurrentSurveyKey(getApplicationContext());
        final DatabaseReference surveyRef = mDataRef.child("surveys").child(surveyKey).child("answers");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = this.getLayoutInflater().inflate(R.layout.dialog_answer_survey_question, null);
        final EditText surveyAnswer = (EditText) view.findViewById(R.id.add_sur_answer_et);
        final TextView surQuestion = (TextView) view.findViewById(R.id.question_text);
        surQuestion.setText(question.getQuestion());
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String answer = surveyAnswer.getText().toString();
                Answer ans = new Answer(answer, question.getKey());
                String pushKey = surveyRef.push().getKey();
                surveyRef.child(pushKey).setValue(ans);
                Toast.makeText(MainActivity.this, "Answer submitted", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(android.R.string.cancel, null);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void addFeedback(Feedback f) {
            DatabaseReference feedRef = mDataRef.child("feedbacks");
            feedRef.push().setValue(f);
    }
}
