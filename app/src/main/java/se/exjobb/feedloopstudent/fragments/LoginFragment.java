package se.exjobb.feedloopstudent.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import se.exjobb.feedloopstudent.R;


public class LoginFragment extends Fragment {
    private OnLoginListener mListener;

    private EditText mEmail;
    private EditText mPassword;
    private Button mLoginButton;
    private Button mRegisterButton;
    private boolean mLoggingIn;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoggingIn = false;
    }

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //hide the appbar/toolbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mEmail = (EditText) view.findViewById(R.id.login_email_field);
        mPassword = (EditText) view.findViewById(R.id.login_password_field);
        mLoginButton = (Button) view.findViewById(R.id.login_button);
        mRegisterButton = (Button) view.findViewById(R.id.login_register_button);


        // if user pressed enter on keyboard on Email field, then go to the next
        // EditText in fragment, which is to enter the password

        mEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if(id == EditorInfo.IME_ACTION_NEXT) {
                    mPassword.requestFocus();
                    return true;
                }

                return false;
            }
        });


        // if user presses enter on keyboard on Password field then execute login
        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if(id == EditorInfo.IME_NULL){
                    login();
                    return true;
                }
                return false;
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onRegisterMe();
            }
        });

        return view;
    }


    public void login(){

        // stop executing if already loging in
        if(mLoggingIn){
            return;
        }

        mEmail.setError(null);
        mPassword.setError(null);

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        boolean cancelLogin = false;
        View focusView = null;

        if(!TextUtils.isEmpty(password) && !validPassword(password)){
            mPassword.setError("Invalid password");
            focusView = mPassword;
            cancelLogin = true;
        }

        if(TextUtils.isEmpty(email)){
            mEmail.setError("Field required");
            focusView = mEmail;
            cancelLogin = true;
        } else if(!validEmail(email)){
            mEmail.setError("Invalid email");
            focusView = mEmail;
            cancelLogin = true;
        }


        if(cancelLogin){
            focusView.requestFocus();
        }else{

            //show progress spinner and start background task to login
            //showProgress(true);
            mLoggingIn = true;
            mListener.onLogin(email,password);
            hideKeyboard();
        }
    }

    private boolean validEmail(String email) {
        return email.contains("@");
    }

    private boolean validPassword(String password) {
        return password.length() > 4;
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEmail.getWindowToken(), 0);
    }

    public void onLoginError(String message) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.login_error))
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();

        //showProgress(false);
        mLoggingIn = false;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginListener) {
            mListener = (OnLoginListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnLoginListener {

        void onLogin(String email, String passwrod);

        void onRegisterMe();
        void onLogout();
    }
}
