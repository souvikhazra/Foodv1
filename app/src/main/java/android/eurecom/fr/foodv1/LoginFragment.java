package android.eurecom.fr.foodv1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment implements OnLoginListener {
    OnLoginFragmentListener mCallback;

    public void setOnHeadlineSelectedListener(OnLoginFragmentListener activity) {
        mCallback = activity;
    }

    // Container Activity must implement this interface
    public interface OnLoginFragmentListener {
        void onLoginSuccessful();
    }


    public LoginFragment() {
    }

    private EditText editTextEmail;
    private EditText editTextPassword;


    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        return true;
                    }
                }
                return false;
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) rootView.findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) rootView.findViewById(R.id.editTextPassword);


        progressDialog = new ProgressDialog(getActivity());

        return rootView;
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.login_fragment, someFragment);

        transaction.addToBackStack(null);

        transaction.commit();

    }


    @Override
    public void login() {
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getActivity(),"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(getActivity(),R.string.enter_password,Toast.LENGTH_LONG).show();
            return;
        }

        //displaying a progress dialog

        progressDialog.setMessage(getString(R.string.login_message));
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //if the task is successfull
                        if(task.isSuccessful()){
                            mCallback.onLoginSuccessful();
                            Fragment fragment = null;
                            fragment = new ProfileFragment();

                            replaceFragment(fragment);
                        }else{
                            //TODO: nice looking AlertDialog?
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage(R.string.login_failed)
                                    .setTitle(R.string.login);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });
    }
}
