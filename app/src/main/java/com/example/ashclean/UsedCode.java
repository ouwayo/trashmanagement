package com.example.ashclean;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.Objects;

public class UsedCode {
    private String mScannedCode;
    public UsedCode (){

    }

    public UsedCode(String mScannedCode){
        this.mScannedCode= mScannedCode;
    }
    public String getmScannedCode(){
        return this.mScannedCode;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsedCode usedCode = (UsedCode) o;
        return Objects.equals(mScannedCode, usedCode.mScannedCode);
    }


    @Override
    public String toString() {
        return "UsedCode{" +
                "mScannedCode='" + mScannedCode + '\'' +
                '}';
    }
}
