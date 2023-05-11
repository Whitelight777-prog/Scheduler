package com.example.androidstudioproject_tomroche.ui;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidstudioproject_tomroche.entities.Course;
import com.example.androidstudioproject_tomroche.R;

import java.util.ArrayList;
import java.util.List;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseViewHolder>{
    ArrayList<Course> courses;
    ArrayList<CourseClickHandler> courseClickHandlers = new ArrayList<>();

    public CourseListAdapter(List<Course> courses){
        this.courses = (ArrayList<Course>) courses;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_list_item,parent,false);
        CourseViewHolder courseViewHolder = new CourseViewHolder(view);
        return courseViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        holder.courseText.setText(courses.get(position).getName());
        holder.layout.setOnClickListener(view -> {

            for(CourseClickHandler courseClickHandler:courseClickHandlers){
                courseClickHandler.onCourseClicked(courses.get(position));
            }

        });
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder{
        TextView courseText;
        ConstraintLayout layout;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseText = itemView.findViewById(R.id.item_name_txt);
            layout = itemView.findViewById(R.id.list_item);
        }
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public void addCourseClickHandler(CourseClickHandler courseClickHandler){
        courseClickHandlers.add(courseClickHandler);
    }

    public interface CourseClickHandler{
        void onCourseClicked(Course course);
    }
}
