package com.example.ashclean;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Objects;

public class Bin {
    private String location;
    private int capacity;
    private int currentOccupation;

    public Bin(){

    }

    public Bin(String location, int capacity) {

        this.location = location;
        this.capacity = capacity;
    }



    public String getLocation() {
        return location;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrentOccupation() {
        return currentOccupation;
    }



    public void setLocation(String location) {
        this.location = location;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setCurrentOccupation() {
        this.currentOccupation ++;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bin bin = (Bin) o;
        return capacity == bin.capacity &&
                currentOccupation == bin.currentOccupation &&
                Objects.equals(location, bin.location);
    }

    @Override
    public String toString() {
        return "Bin{" +
                ", location='" + location + '\'' +
                ", capacity=" + capacity +
                ", currentOccupation=" + currentOccupation +
                '}';
    }
}
