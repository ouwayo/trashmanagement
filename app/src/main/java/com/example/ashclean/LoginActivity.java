package com.example.ashclean;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashclean.MainChatActivity;
import com.example.ashclean.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    // TODO: Add member variables here:
    private FirebaseAuth mAuth;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mUserName;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.login_email);
        mPasswordView = (EditText) findViewById(R.id.login_password);
        mUserName = (EditText) findViewById(R.id.register_username);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        // TODO: Grab an instance of FirebaseAuth

        mAuth = FirebaseAuth.getInstance();

    }

    // Executed when Sign in button pressed
    public void signInExistingUser(View v)   {
        // TODO: Call attemptLogin() here
        attemptLogin();

    }

    // Executed when Register button pressed
    public void registerNewUser(View v) {
        Intent intent = new Intent(this, com.example.ashclean.RegisterActivity.class);
        finish();
        startActivity(intent);
    }

    // TODO: Complete the attemptLogin() method
    private void attemptLogin() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();


        if(email==null || password==null){
            return;
        }else{
            Toast.makeText(this,"login in progress...",Toast.LENGTH_SHORT).show();
        }

        // TODO: Use FirebaseAuth to sign in with email & password

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("FlashChat", "SignInOncomplete() " + task.isSuccessful());
                if (!task.isSuccessful()) {
                    Log.d("FlashChat", "Problem signing in " + task.getException());
                    showErrorDialog("Login issue... retry");
                }else{
                    Log.d("FlashChat", "Current user Id"+ mAuth.getCurrentUser().getUid());
                    mDatabase = FirebaseDatabase.getInstance().getReference();

                    mDatabase.addValueEventListener( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot mDataSnapshot) {
                            for(DataSnapshot ds: mDataSnapshot.getChildren()){
                                User m = ds.child(mAuth.getCurrentUser().getUid()).getValue(User.class);
//                                Log.d("FlashChat", "onDataChange: "+ m.getUserName());
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });
//                    private void showData(DataSnapshot dataSnapshot){
//
//                    }
//                    mDatabase.addListenerForSingleValueEvent(mData);

                    Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    // TODO: Show error on screen with an alert dialog

    private void showErrorDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("OOps")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }



}