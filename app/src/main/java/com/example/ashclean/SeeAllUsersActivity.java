package com.example.ashclean;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SeeAllUsersActivity extends Activity {
    int[] IMAGES = {R.drawable.profile, R.drawable.profile, R.drawable.profile};
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    ArrayList<User> userList;
    ArrayAdapter<String> userAdapter;
    User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_all_users_layout);
        user = new User();

        final ListView listView= findViewById(R.id.listView);



        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userList = new ArrayList<>();
//        userAdapter = new ArrayAdapter<String>(this, R.layout.custom_layout, R.id.AllUserNameView, userList);


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.child("users").getChildren()){
                    user = ds.getValue(User.class);
                    userList.add(user);
                }
                CustomerAdepter customerAdepter = new CustomerAdepter();
                listView.setAdapter(customerAdepter);
                Log.d("listView","hello"+ userList.toString());
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    class CustomerAdepter extends BaseAdapter{

        @Override
        public int getCount() {
            return userList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.custom_layout, null);
            final ImageView imageView = convertView.findViewById(R.id.imageView2);
            final TextView textView_allUserName = convertView.findViewById(R.id.AllUserNameView);
            final TextView textView_credit = convertView.findViewById(R.id.allUserCredit);

            imageView.setImageResource(R.drawable.profile);
            textView_allUserName.setText(userList.get(position).getUserName());
            textView_credit.setText(String.valueOf(userList.get(position).getUserCredit()));

            return convertView;
        }
    }
}
