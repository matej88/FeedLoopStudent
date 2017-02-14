package se.exjobb.feedloopstudent.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import se.exjobb.feedloopstudent.R;
import se.exjobb.feedloopstudent.adapters.CourseTabViewPagerAdapter;
import se.exjobb.feedloopstudent.models.Course;
import se.exjobb.feedloopstudent.models.Session;

public class CourseOverviewFragment extends Fragment {
    private static final String ARG_COURSE = "course";

    private CourseOverviewCallback mListener;
    private Course mCourse;

    //GUI
    private TextView courseName;
    private TextView courseCode;
    private TextView courseStatus;
    private boolean courseIsOnline;
    private RelativeLayout onOffLayout;


    // Stores two fragments on the main page
    private ViewPager mViewPagerSingleCourse;

    // Enables to swipe through the fragments on main page
    private TabLayout mTabLayoutSingleCourse;


    private DatabaseReference mDataRef;
    private DatabaseReference mCourseRef;

private String sessionKey;
    public CourseOverviewFragment() {
        // Required empty public constructor
    }

    public static CourseOverviewFragment newInstance(Course course) {
        CourseOverviewFragment fragment = new CourseOverviewFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_COURSE, course);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCourse = getArguments().getParcelable(ARG_COURSE);
        }


        //courseKey = SharedPreferencesUtils.getCurrentCourseKey(getContext());
        mDataRef = FirebaseDatabase.getInstance().getReference();

        //get the reference for the selected course
        mCourseRef = mDataRef.child("courses").child(mCourse.getKey());

        mCourseRef.addValueEventListener(new CourseValueEventListener());

    }


    private class CourseValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Course course = dataSnapshot.getValue(Course.class);
            mCourse = course;

                   updateGUI(course);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context context = getContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_overview, container, false);

        courseName = (TextView) view.findViewById(R.id.course_overview_name);
        courseCode = (TextView) view.findViewById(R.id.course_overview_code);
        courseStatus = (TextView) view.findViewById(R.id.course_overview_status);

        onOffLayout = (RelativeLayout) view.findViewById(R.id.onoff_layout);

        setupTabs(view);



        return view;
    }

    public void updateGUI(Course course){
        courseName.setText(course.getName());
        courseCode.setText(course.getCode());

        if(course.isOnline()){
            courseIsOnline = true;
            onOffLayout.setBackgroundColor(Color.parseColor("#66bb6a"));
            courseStatus.setText("Online");

        }else{
            courseIsOnline = false;
            onOffLayout.setBackgroundColor(Color.parseColor("#ef5350"));
            courseStatus.setText("Offline");

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        updateGUI(mCourse);

    }

    public void setupTabs(View view){
        mViewPagerSingleCourse = (ViewPager) view.findViewById(R.id.viewpager_single_course);
        //  if there are fragments added to ViewPager then set it up
        if (mViewPagerSingleCourse != null){
            setupViewPager(mViewPagerSingleCourse);
        }


        mTabLayoutSingleCourse = (TabLayout) view.findViewById(R.id.tabLayout_single_course);
        mTabLayoutSingleCourse.setupWithViewPager(mViewPagerSingleCourse);

        // What happens if user swipes through the fragments
        mTabLayoutSingleCourse.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // show the page that is swiped to
                mViewPagerSingleCourse.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    // Populate the adapter with fragments
    private void setupViewPager(ViewPager mViewPager){

        // call getChildFragmentManager when a fragment is inside another fragment
        CourseTabViewPagerAdapter adapter = new CourseTabViewPagerAdapter(getChildFragmentManager());
        Fragment fragment = SurveyListTabFragment.newInstance(mCourse.getKey());
        adapter.addFrag(fragment, "Surveys");
        Fragment fragment1 = FeedbackListTabFragment.newInstance(mCourse.getKey());
        adapter.addFrag(fragment1, "Feedbacks");


        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CourseOverviewCallback) {
            mListener = (CourseOverviewCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CourseOverviewCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface CourseOverviewCallback {
        void startSession();

    }

}
