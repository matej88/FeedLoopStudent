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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import se.exjobb.feedloopstudent.R;
import se.exjobb.feedloopstudent.SharedPreferencesUtils;
import se.exjobb.feedloopstudent.fragments.CourseListFragment;
import se.exjobb.feedloopstudent.models.Course;
import se.exjobb.feedloopstudent.models.Student;


public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private final CourseListFragment mCourseListFragment;

    private final CourseListFragment.OnCourseSelectedListener mCourseSelectedListener;
    private DatabaseReference mDataRef;
    private DatabaseReference mStudentRef;
    private DatabaseReference mCoursesRef;
    private ArrayList<Course> mCourses = new ArrayList<>();
    private String mStudentUid;
    private ArrayList<String> courseKeys = new ArrayList<>();


    public CourseAdapter(CourseListFragment courseListFragment,
                         CourseListFragment.OnCourseSelectedListener listener,
                         String studentUid){


        mCourseListFragment = courseListFragment;
        mCourseSelectedListener = listener;
        mDataRef = FirebaseDatabase.getInstance().getReference();
        mCoursesRef = mDataRef.child("courses");
        mStudentRef = mDataRef.child("users").child("students");
        mStudentUid = studentUid;

        getStudentCourses();

    }


    class CoursesChildEventListener implements ChildEventListener{

        private void add(DataSnapshot dataSnapshot){


            Course course = dataSnapshot.getValue(Course.class);
            course.setKey(dataSnapshot.getKey());

            mCourses.add(course);

        }

        private int remove (String key) {
            for(Course course : mCourses){
                if(course.getKey().equals(key)){
                    int foundPos = mCourses.indexOf(course);
                    mCourses.remove(course);
                    return foundPos;

                }
            }
            return -1;
        }
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            remove(dataSnapshot.getKey());
            add(dataSnapshot);
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
    private void getTheCourse(){
        for(int i = 0 ; i < courseKeys.size() ; i++) {
            String courseKey = courseKeys.get(i);
            Query courseQuery = mCoursesRef.orderByChild("code").equalTo(courseKey);
            courseQuery.addChildEventListener(new CoursesChildEventListener());
        }
    }

        private void getCourseKey(Map<String,Boolean> map){

            if(map != null) {
                for (String key : map.keySet()) {
                    courseKeys.add(key);
                }

                getTheCourse();
                //mCourseListFragment.printKeys(courseKeys);
            }

            }

    private void getStudentCourses(){
        mStudentRef.child(mStudentUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Student s = dataSnapshot.getValue(Student.class);
                getCourseKey(s.getSubscribedto());


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_course, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CourseAdapter.ViewHolder holder, int position) {
        holder.mCourseNameTextView.setText(mCourses.get(position).getName());
        holder.mCourseCodeTextView.setText(mCourses.get(position).getCode());


    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mCourseNameTextView;
        private TextView mCourseCodeTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCourseNameTextView = (TextView) itemView.findViewById(R.id.card_course_name);
            mCourseCodeTextView = (TextView) itemView.findViewById(R.id.card_course_code);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            SharedPreferencesUtils.setCurrentCourseKey(mCourseListFragment.getContext(), mCourses.get(getAdapterPosition()).getKey());
            SharedPreferencesUtils.setCurrentCourseKey(mCourseListFragment.getContext(), mCourses.get(getAdapterPosition()).getKey());
            Course course = mCourses.get(getAdapterPosition());
            mCourseSelectedListener.onCourseSelected(course);
        }
    }
}
