package se.exjobb.feedloopstudent.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import se.exjobb.feedloopstudent.R;
import se.exjobb.feedloopstudent.adapters.SurveyAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class SurveyListTabFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COURSEKEY = "current_course_key";


    private String mCurrentCourseKey;
    private SurveyAdapter mAdapter;
    private OnSurveyClickListener mListener;
    private TextView mNoSurveys;




    public SurveyListTabFragment() {
        // Required empty public constructor
    }


    public static SurveyListTabFragment newInstance(String courseKey) {
        SurveyListTabFragment fragment = new SurveyListTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COURSEKEY, courseKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentCourseKey = getArguments().getString(ARG_COURSEKEY);

        }


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_survey_list_tab, container, false);
        mNoSurveys = (TextView) view.findViewById(R.id.list_survey_no_surveys);
        setText(0);




        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.survey_tab_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        registerForContextMenu(recyclerView);
        mAdapter = new SurveyAdapter(this, mCurrentCourseKey, mListener);
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    public void setText(int b){
        if(b == 0){
            mNoSurveys.setText("There are no surveys");
        }else{
            mNoSurveys.setText("");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSurveyClickListener) {
            mListener = (OnSurveyClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSurveyClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSurveyClickListener {
        void onSurveyClicked();
        void onAddSurveyClicked(String courseKey);
    }


}
