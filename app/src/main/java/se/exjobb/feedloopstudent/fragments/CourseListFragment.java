package se.exjobb.feedloopstudent.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import se.exjobb.feedloopstudent.R;
import se.exjobb.feedloopstudent.SharedPreferencesUtils;
import se.exjobb.feedloopstudent.adapters.CourseAdapter;
import se.exjobb.feedloopstudent.models.Course;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseListFragment extends Fragment {


    private static final String ARG_STUDENTUID = "studentUid";

    private OnCourseSelectedListener mClickListener;
    private CourseAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private String mStudentUid;
    private boolean maybe;
    private DatabaseReference mDataRef;
    public CourseListFragment() {
        // Required empty public constructor
    }

    public static CourseListFragment newInstance(String studentUid) {
        CourseListFragment fragment = new CourseListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STUDENTUID, studentUid);


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStudentUid = getArguments().getString(ARG_STUDENTUID);

        }

        mDataRef = FirebaseDatabase.getInstance().getReference();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context context = getContext();
        //show the appbar/toolbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.course_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               showAddCourseDialog(null);
            }
        });

        fab.setVisibility(View.VISIBLE);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.course_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        registerForContextMenu(recyclerView);
       mAdapter = new CourseAdapter(this, mClickListener, mStudentUid);
        recyclerView.setAdapter(mAdapter);
        return view;

    }

    public void printKeys(ArrayList<String> keys){

        Toast.makeText(getContext(), "Key " + keys.get(0), Toast.LENGTH_SHORT).show();
    }
    public void printStudentName(String name){
        Toast.makeText(getContext(), "St name" + name, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCourseSelectedListener) {
            mClickListener = (OnCourseSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCourseSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mClickListener = null;
    }



    @SuppressLint("InflateParams")
    public void showAddCourseDialog(final Course course){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_course, null);

        final EditText courseCodeEditText = (EditText) view.findViewById(R.id.dialog_add_course_code);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String courseCode = courseCodeEditText.getText().toString();
                final String coureToUpperCase = courseCode.toUpperCase();

                Query courseExists = mDataRef.child("courses").orderByChild("code").equalTo(coureToUpperCase);
                courseExists.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        long yesNo = dataSnapshot.getChildrenCount();

                        if (yesNo > 0){
                            mClickListener.onAddCourse(coureToUpperCase, mStudentUid);
                        }else{
                            Toast.makeText(getContext(), "Course " + coureToUpperCase + " not found!" , Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public interface OnCourseSelectedListener {
        void onCourseSelected(Course c);
        void onAddCourse(String courseCode, String studentUid);
    }

}
