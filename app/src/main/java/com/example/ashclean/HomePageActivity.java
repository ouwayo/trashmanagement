package com.example.ashclean;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePageActivity extends Activity {

    public TextView nameView;
    public TextView userCreditView;
    public TextView moneyMessage;
    public TextView cedisignHome;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String name = "";
    private String userCredit;
    private Button scanButton;
    private Button allUserButton;
    private Button viewBins;
    private Button addBins;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        nameView = findViewById(R.id.home_username);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        scanButton = findViewById(R.id.scanBar);
        allUserButton = findViewById(R.id.allUsersButton);
        userCreditView = findViewById(R.id.amount);
        moneyMessage = findViewById(R.id.home_credit);
        cedisignHome = findViewById(R.id.CedisSignHome);
        viewBins = findViewById(R.id.viewBins);
        addBins = findViewById(R.id.addBinFromHome);

        addBins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, addBinActivity.class);
                finish();
                startActivity(intent);
            }
        });
        viewBins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, BinActivity.class);
                finish();
                startActivity(intent);
            }
        });

        /*Reading the user's name from the database to the profile*/
        mDatabase.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot mDataSnapshot) {
                Log.d("FlashChat", mDataSnapshot.child("users").child(mAuth.getCurrentUser().getUid()).toString());

                User m = mDataSnapshot.child("users").child(mAuth.getCurrentUser().getUid()).getValue(User.class);
                if(m==null){
                    m = mDataSnapshot.child("admin").child(mAuth.getCurrentUser().getUid()).getValue(User.class);

                }

                name = m.getUserName();
                userCredit = String.valueOf(m.getUserCredit());
                nameView.setText(m.getUserName());
                if(m.getAnAdmin()){
                    userCreditView.setVisibility(View.INVISIBLE);
                    allUserButton.setVisibility(View.VISIBLE);
                    scanButton.setVisibility(View.INVISIBLE);
                    moneyMessage.setVisibility(View.INVISIBLE);
                    cedisignHome.setVisibility(View.INVISIBLE);
                    addBins.setVisibility(View.VISIBLE);
                    viewBins.setVisibility(View.VISIBLE);
                }else{
                    userCreditView.setVisibility(View.VISIBLE);
                    userCreditView.setText(userCredit);
                    scanButton.setVisibility(View.VISIBLE);
                    allUserButton.setVisibility(View.INVISIBLE);
                    moneyMessage.setVisibility(View.VISIBLE);
                    cedisignHome.setVisibility(View.VISIBLE);
                }

                Log.d("FlashChat", "onDataChange: "+ m.getUserName());
//                for(DataSnapshot ds: mDataSnapshot.child("users").getChildren()){


//

//                    Log.d("FlashChat", ds.child(mAuth.getCurrentUser().getUid()).toString() );
//
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        scanButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, BarCodeActivity.class);
                startActivity(intent);
            }
        });

        allUserButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, SeeAllUsersActivity.class);
                startActivity(intent);
            }
        });
    }



}
