package com.example.ashclean;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission_group.CAMERA;

public class BarCodeActivity extends Activity implements ZXingScannerView.ResultHandler {
    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private boolean a=false;
    long n = 0;
    long p = 0;
//    private ArrayList<String> company;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(BarCodeActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted

            mAuth = FirebaseAuth.getInstance();

            scannerView = new ZXingScannerView(this);
//            Log.d("FlashChat", scannerView.toString());
            setContentView(scannerView);
        }else{
            ActivityCompat.requestPermissions(BarCodeActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
    }



    private boolean checkPermission(){
        return(ContextCompat.checkSelfPermission(BarCodeActivity.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED);
    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String permission[], int grantResult[]){
        switch (requestCode){
            case REQUEST_CAMERA:
                if(grantResult.length>0){
                    boolean cameraAccepted = grantResult[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted){
                        Toast.makeText(BarCodeActivity.this, "Permission Granted", Toast.LENGTH_LONG);
                    }else{
                        Toast.makeText(BarCodeActivity.this, "Permission Denied", Toast.LENGTH_LONG);




                        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                            if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                                DisplayAlertMessage("You need to allow access to your camera", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                    }
                                });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                if(scannerView == null){
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }else{
                requestPermission();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void DisplayAlertMessage(String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(BarCodeActivity.this)
                .setMessage(message)
                .setPositiveButton("Ok", listener)
                .setNegativeButton("Cancel", null)
                .create().show();

    }

    @Override
    public void handleResult(Result result) {
        mDatabase = FirebaseDatabase.getInstance().getReference();


        final String scanResult = result.getText();
        addScannedCode(scanResult, mDatabase);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scanned Item id");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                scannerView.resumeCameraPreview(BarCodeActivity.this);
            }
        });
        builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scanResult));
                startActivity(intent);
            }
        });
        builder.setMessage(scanResult);
        AlertDialog alert = builder.create();
        alert.show();

//        Log.d("FlashChat","This is what I am looking for: "+ String.valueOf(checkIfAlreadyExist(scanResult, mDatabase)));

//        if(!checkIfAlreadyExist(scanResult,mDatabase)){

//        }else{
//            Toast.makeText(BarCodeActivity.this, "This item has been scanned", Toast.LENGTH_LONG).show();
//        }

    }

    public void addScannedCode(String scanCode, final DatabaseReference mDatabase){


        mDatabase.child("scannedCode").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                n = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        mDatabase.child("scannedCode").child(scanCode)

        UsedCode mUsedCode = new UsedCode(scanCode);
        mDatabase.child("scannedCode").child(scanCode)
                .setValue(mUsedCode).addOnCompleteListener(new OnCompleteListener<Void>() {
//            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            @Override

            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        User user = new User();
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean control = true;
//                            if(control){
                            user = dataSnapshot.child("users").child(mAuth.getCurrentUser().getUid()).getValue(User.class);
                            p = dataSnapshot.child("scannedCode").getChildrenCount();

                            if(p>n){
                                double newCredit = user.getUserCredit()+0.2;
                                newCredit = Math.round(newCredit * 100.0) / 100.0;
                                user.setUserCredit(newCredit);
                            }
                            mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).setValue(user);
//                                control = false;
                            Intent intent = new Intent(BarCodeActivity.this, BinActivity.class);
                            startActivity(intent);
                            }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });


                    Toast.makeText(BarCodeActivity.this, "Scan successful", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(BarCodeActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

//    public boolean checkIfAlreadyExist(final String scanCode, DatabaseReference mDatabase){
//        Log.d("FlashChat", "This is the function" );
//        final String[] response = {"false"};
//
//        mDatabase.addValueEventListener( new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot mDataSnapshot) {
//                Log.d("FlashChat", mDataSnapshot.child("scannedCode").toString() );
//                for(DataSnapshot d: mDataSnapshot.child("scannedCode").getChildren()){
//                    String id = d.getKey();
//                    UsedCode myUsedCode = d.getValue(UsedCode.class);
//                    Log.d("FlashChat", "getScannedCode "+ myUsedCode.getmScannedCode());
//                    Log.d("FlashChat", "ScanCode"+ scanCode);
//                    Log.d("FlashChat", "Comparaison "+ (myUsedCode.getmScannedCode()== scanCode));
//                    if(myUsedCode.getmScannedCode()==scanCode){
//                        response[0] = "true";
//                        a = true;
//
//                    }
//                    Log.d("FlashChat", "onDataChange: "+ myUsedCode.toString());
//
//                }
//
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.d("FlashChat",databaseError.toString());
//
//            }
//        });
//        Log.d("FlashChat", "Response ........."+ a);
//
////        Log.d("FlashChat", response[0]);
////        Log.d("FlashChat", "Response ........."+ response[0]);
//
////        if(response[0]=="true"){
////            return true;
////        }else{
////            return false;
////        }
//
//        Log.d("FlashChat","This is what I am looking for: "+ String.valueOf(a));
//
//        return a;
//    }
}
