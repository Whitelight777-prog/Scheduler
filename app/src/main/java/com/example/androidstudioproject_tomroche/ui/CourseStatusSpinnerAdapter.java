package com.example.androidstudioproject_tomroche.ui;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

public class CourseStatusSpinnerAdapter extends ArrayAdapter<CourseStatus> {

    public CourseStatusSpinnerAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.setDropDownViewResource(android.R.layout.simple_spinner_item);
    }

    public void add(CourseStatus...status){
        this.addAll(status);
    }

}
