package se.exjobb.feedloopstudent.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import se.exjobb.feedloopstudent.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private onRegisterListener mListener;

    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;

    private Button mRegisterButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    private ProgressDialog mProgressDialog;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users/teachers");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //hide the appbar/toolbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_register, container, false);
        mProgressDialog = new ProgressDialog(view.getContext());


        mName = (EditText) view.findViewById(R.id.register_name);
        mEmail = (EditText) view.findViewById(R.id.register_email);
        mPassword = (EditText) view.findViewById(R.id.register_password);

        mRegisterButton = (Button) view.findViewById(R.id.register_button);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mName.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                View focusView = null;

                if(TextUtils.isEmpty(name)){
                    mName.setError("Field required");
                    focusView = mName;
                }
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Field required");
                    focusView = mEmail;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Field required");
                    focusView = mPassword;
                }
                else{


                    mListener.onRegisterClicked(name,email,password);

                }

            }


        });



        return view;
    }


    public void onButtonPressed(String name, String email, String password) {
        if (mListener != null) {
            mListener.onRegisterClicked(name,email,password);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onRegisterListener) {
            mListener = (onRegisterListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onRegisterListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface onRegisterListener {

        void onRegisterClicked(String name, String email, String password);
    }
}
