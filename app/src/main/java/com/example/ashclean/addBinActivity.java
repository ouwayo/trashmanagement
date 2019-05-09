package com.example.ashclean;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class addBinActivity extends Activity {
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    ArrayList<Bin> theListOfBins;
    Button addBinButton;
    AutoCompleteTextView location;
    AutoCompleteTextView capacity;
    Bin myBin;
    String mCapacity;
    String mLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bin_layout);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        addBinButton = findViewById(R.id.addBinButton);
        location = findViewById(R.id.bin_location);
        capacity = findViewById(R.id.binCapacity);

        addBinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mLocation = location.getText().toString();
                mCapacity = capacity.getText().toString();
                int mIntCapacity = Integer.parseInt(mCapacity);

                myBin = new Bin( mLocation,mIntCapacity);
                mDatabase.child("bin").push().setValue(myBin).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(addBinActivity.this, "Successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(addBinActivity.this, HomePageActivity.class);
                            finish();
                            startActivity(intent);
                        }else{
                            Toast.makeText(addBinActivity.this, "Something went wrong try again!", Toast.LENGTH_LONG).show();
                        }

                    }

                });
            }
        });



    }
}
