package com.example.ashclean;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class BinActivity extends Activity {
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    ArrayList<Bin> theListOfBins;
    Bin myBin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bin_list_view);

        final ListView BinlistView= findViewById(R.id.binList);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        theListOfBins = new ArrayList<>();

        mDatabase.addValueEventListener(new ValueEventListener() {
            ArrayList<String> ids = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.child("bin").getChildren()){
                    myBin = ds.getValue(Bin.class);
                    theListOfBins.add(myBin);
                    ids.add(ds.getKey());
                }
                BinCustomAddapter customerAdepter = new BinCustomAddapter();
                BinlistView.setAdapter(customerAdepter);
                BinlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bin clickBin = theListOfBins.get(position);
                        clickBin.setCurrentOccupation();
                        Log.d("Tony", "ids: "+ ids.toString());
                        mDatabase.child("bin").child(ids.get(position)).setValue(clickBin);
                        Intent intent = new Intent(BinActivity.this, HomePageActivity.class);
                        startActivity(intent);
                        Toast.makeText(BinActivity.this, "Thank you for protecting the environment", Toast.LENGTH_LONG).show();
                    }
                });
                Log.d("listView","hello"+ theListOfBins.toString());

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        theListOfBins.setOnitemClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }


    class BinCustomAddapter extends BaseAdapter {

        @Override
        public int getCount() {
            return theListOfBins.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.bin_custom_layout, null);
            final TextView textView_location = convertView.findViewById(R.id.binLocation);
//            final TextView textView_capacity = convertView.findViewById(R.id.binCapacity);

            textView_location.setText(theListOfBins.get(position).getLocation());
//            textView_capacity.setText(String.valueOf(theListOfBins.get(position).getCapacity()));

            return convertView;
        }
    }
}
