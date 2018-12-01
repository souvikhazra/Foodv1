package android.eurecom.fr.foodv1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private TextView forgotPassword;


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
        forgotPassword = rootView.findViewById(R.id.forgot_password);
        forgotPassword.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                showForgotPasswordDialog();
            }
            return false;
        });


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


    private void showForgotPasswordDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.reset_password);
        alertDialog.setMessage(R.string.enter_email);

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        //alertDialog.setIcon(R.drawable.key);

        alertDialog.setPositiveButton("YES",
                (dialog, which) -> {
                    restorePassword(input.getText().toString().trim());
                });

        alertDialog.setNegativeButton(R.string.cancel,
                (dialog, which) -> dialog.cancel());

        alertDialog.show();
    }

    private void restorePassword(String email){
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener((OnSuccessListener) result -> Toast.makeText(getContext(),
                        R.string.email_password_rest, Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(getContext(),
                                R.string.error_reset_password, Toast.LENGTH_SHORT).show());
    }

}
