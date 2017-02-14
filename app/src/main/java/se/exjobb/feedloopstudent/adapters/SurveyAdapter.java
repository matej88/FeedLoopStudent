package se.exjobb.feedloopstudent.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import se.exjobb.feedloopstudent.R;
import se.exjobb.feedloopstudent.SharedPreferencesUtils;
import se.exjobb.feedloopstudent.fragments.SurveyListTabFragment;
import se.exjobb.feedloopstudent.models.Survey;


/**
 * Created by matej on 2017-01-15.
 */

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.ViewHolder>{

    private final SurveyListTabFragment.OnSurveyClickListener mSurveySelectedListener;
    private String mCourseKey;
    private ArrayList<Survey> mSurveys = new ArrayList<>();
    private SurveyListTabFragment mSurveyListTabFragment;
    private DatabaseReference mSurveysRef;
    private DatabaseReference mDataRef;

    public SurveyAdapter(SurveyListTabFragment surveyListTabFragment,
                         String courseKey,
                         SurveyListTabFragment.OnSurveyClickListener surveySelectedListener){

        mSurveyListTabFragment = surveyListTabFragment;
        mSurveySelectedListener = surveySelectedListener;
        mCourseKey = courseKey;

        mDataRef = FirebaseDatabase.getInstance().getReference();
        mSurveysRef = mDataRef.child("surveys");
        Query surveysForCourseRef = mSurveysRef.orderByChild(Survey.COURSE_KEY).equalTo(courseKey);
        surveysForCourseRef.addChildEventListener(new SurveysChildEventListener());

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_survey_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {



            holder.mSurveyNameTextView.setText(mSurveys.get(position).getSurveyName());




        }


    @Override
    public int getItemCount() {
        return mSurveys.size();
    }

    private class SurveysChildEventListener implements ChildEventListener {

        private void add(DataSnapshot dataSnapshot){
            Survey survey = dataSnapshot.getValue(Survey.class);
            survey.setKey(dataSnapshot.getKey());
            mSurveys.add(survey);

        }

        private int remove(String key){
            for (Survey s : mSurveys){
                if(s.getKey().equals(key)){
                    int foundPos = mSurveys.indexOf(s);
                    mSurveys.remove(s);
                    return foundPos;
                }
            }
            return -1;
        }
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                add(dataSnapshot);
            mSurveyListTabFragment.setText(getItemCount());
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            remove(dataSnapshot.getKey());
            add(dataSnapshot);
            mSurveyListTabFragment.setText(getItemCount());
            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            int position = remove(dataSnapshot.getKey());
            if (position >= 0) {
                notifyItemRemoved(position);
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mSurveyNameTextView;

        private TextView mNoSurveys;


        public ViewHolder(View itemView) {
            super(itemView);
            mSurveyNameTextView = (TextView) itemView.findViewById(R.id.list_survey_name);

            mNoSurveys = (TextView) itemView.findViewById(R.id.list_survey_no_surveys);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            SharedPreferencesUtils.setCurrentSurveyKey(mSurveyListTabFragment.getContext(), mSurveys.get(getAdapterPosition()).getKey());
            SharedPreferencesUtils.setCurrentSurveyName(mSurveyListTabFragment.getContext(), mSurveys.get(getAdapterPosition()).getSurveyName());

            mSurveySelectedListener.onSurveyClicked();

        }
    }
}
