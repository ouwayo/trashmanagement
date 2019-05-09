package com.example.ashclean;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.ashclean.R.layout.activity_register;
import static com.example.ashclean.R.layout.home_page;


public class RegisterActivity extends AppCompatActivity {

    // Constants
    public static final String CHAT_PREFS = "ChatPrefs";
    public static final String DISPLAY_NAME_KEY = "username";

    // TODO: Add member variables here:
    // UI references.
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private Button checkedAdmin;
    private boolean isAdmin;

    // Firebase instance variables

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_register);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mConfirmPasswordView = (EditText) findViewById(R.id.register_confirm_password);
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.register_username);
        checkedAdmin = findViewById(R.id.adminCheckBox);

        checkedAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox)v).isChecked();

                Log.d("CheckBox", "Is checked? : "+ checked);
                if(checked){
                    isAdmin = true;
                }else {
                    isAdmin = false;
                }
            }
        });

        // Keyboard sign in action
        mConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.register_form_finished || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });


        // TODO: Get hold of an instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();


    }

    // Executed when Sign Up button is pressed.
    public void signUp(View v) {
        attemptRegistration();

    }

    private void attemptRegistration() {

        // Reset errors displayed in the form.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // TODO: Call create FirebaseUser() here
            createFirebaseUser();

        }
    }

    private boolean isEmailValid(String email) {
        // You can add more checking logic here.
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Add own logic to check for a valid password (minimum 6 characters)
        String confirmPassword = mConfirmPasswordView.getText().toString();
        return confirmPassword.equals(password) && password.length()>4;
    }

    // TODO: Create a Firebase user
    private void createFirebaseUser(){
        final String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        final String userName = mUsernameView.getText().toString();



        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(!task.isSuccessful()){
                    Log.d("FlashChat", "User creation failed");
//                    showErrorDialog("Registration attempt failed");
                }else{
                    Log.d("FlashChat", "User creation successful");
                    Log.d("FlashChat", task.getResult().toString());
//                    saveDisplayName();

                    User user = new User(
                            userName,
                            email,
                            isAdmin
                    );
                    /*Adding the user's name and email to the database to the database*/

                    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


                    if(isAdmin){
                        mDatabase.child("admin")
                                .child(FirebaseAuth.getInstance().getCurrentUser()
                                        .getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            @Override

                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                                    String a = mDatabase.child("admin")
                                            .child(FirebaseAuth.getInstance().getCurrentUser()
                                                    .getUid()).getKey();
                                    Log.d("FlashChat", "Admin "+ a);
                                    finish();
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(RegisterActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }else{

                        mDatabase.child("users")
                                .child(FirebaseAuth.getInstance().getCurrentUser()
                                        .getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            @Override

                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                                    String a = mDatabase.child("users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser()
                                                    .getUid()).getKey();
                                    Log.d("FlashChat", a);
//                                    finish();
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(RegisterActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }



                }
            }
        });
    }



    // TODO: Save the display name to Shared Preferences
    private void saveDisplayName(){
        String displayName = mUsernameView.getText().toString();

        SharedPreferences prefs = getSharedPreferences(CHAT_PREFS, 0);
        prefs.edit().putString(DISPLAY_NAME_KEY, displayName).apply();

    }


    // TODO: Create an alert dialog to show in case registration failed

    private void showErrorDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
}
