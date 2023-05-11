package com.example.androidstudioproject_tomroche.ui;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.androidstudioproject_tomroche.entities.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseSpinnerAdapter extends ArrayAdapter<Course> {

    public CourseSpinnerAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.setDropDownViewResource(android.R.layout.simple_spinner_item);
    }

    @Override
    public void add(Course course){
        super.add(course);
    }

    public boolean contains(String courseName){
        for (int i = 0; i < getCount(); i++){
            if (courseName.equals(getItem(i).getName())){
                return true;
            }
        }
        return false;
    }

    public List<Course> getAll(){
        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < getCount(); i++){
            courses.add(getItem(i));
        }
        return courses;
    }

}
