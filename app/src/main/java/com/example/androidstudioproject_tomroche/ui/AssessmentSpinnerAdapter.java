package com.example.androidstudioproject_tomroche.ui;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.androidstudioproject_tomroche.entities.Assessment;

import java.util.ArrayList;
import java.util.List;

public class AssessmentSpinnerAdapter extends ArrayAdapter<Assessment> {

    public AssessmentSpinnerAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.setDropDownViewResource(android.R.layout.simple_spinner_item);
    }

    public List<Assessment> getAll(){
        List<Assessment> assessments = new ArrayList<>();
        for (int i = 0; i < getCount(); i++){
            assessments.add(getItem(i));
        }
        return assessments;
    }

    public boolean contains(String name){
        for(Assessment ass : getAll()){
            if (ass.getName().equals(name)){
                return true;
            }
        }
        return false;
    }
}