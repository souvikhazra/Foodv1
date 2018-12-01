package android.eurecom.fr.foodv1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;


public class RegisterFragment extends Fragment implements OnSignUpListener{

    //defining view objects
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;


    private ProgressDialog progressDialog;


    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        editTextEmail = (EditText) rootView.findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) rootView.findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText) rootView.findViewById(R.id.editTextConfirmPassword);


        progressDialog = new ProgressDialog(getActivity());

        return rootView;
    }

    @Override
    public void signUp() {
        //getting email and password from edit texts
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        //checking if email and passwords are empty and valid
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "Please enter an email.", Toast.LENGTH_LONG).show();
            return;
        }

        //checks email input - valid email?
        if (!isValidEmailAddress(email)) {
            Toast.makeText(getActivity(), "Please enter an valid email.", Toast.LENGTH_LONG).show();
            return;
        }


        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Please enter a password.", Toast.LENGTH_LONG).show();
            return;
        }

        if(!validPassword(password)){
            createAlertDialog(getResources().getString(R.string.password_guidelines),getResources().getString(R.string.register));
            return;
        }

        //check if password inputs are equal
        if(!password.equals(confirmPassword)){
            Toast.makeText(getActivity(), "Please enter two equal passwords.", Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering. Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            //display some message here
                            Toast.makeText(getActivity(), R.string.registration_success, Toast.LENGTH_LONG).show();
                        } else {
                            //display some message here
                            Toast.makeText(getActivity(), R.string.registration_error, Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    //checks if user input is valid email
    public static boolean isValidEmailAddress(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    //Checks if password has a valid format - should of course not be done in the client ;)
    private static boolean validPassword(String password)
    {
        // The password should be at least six characters long.
        // The password should contain at least one letter.
        // The password should have at least one digit.
        if ((password.length() > 5) &&
                (password.matches(("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]+$"))))
            return true;
        else
            return false;
    }

    private void createAlertDialog(String message,String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setTitle(title);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
