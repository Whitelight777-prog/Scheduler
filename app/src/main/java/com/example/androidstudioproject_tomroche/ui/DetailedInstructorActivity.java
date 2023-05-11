package com.example.androidstudioproject_tomroche.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidstudioproject_tomroche.dao.Database;
import com.example.androidstudioproject_tomroche.entities.Course;
import com.example.androidstudioproject_tomroche.entities.CourseInstructor;
import com.example.androidstudioproject_tomroche.entities.Instructor;
import com.example.androidstudioproject_tomroche.R;

import java.util.List;

public class DetailedInstructorActivity extends AppCompatActivity {

    Database database = null;
    Course course = null;
    Instructor instructor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_instructor_view);
        course = (Course) getIntent().getSerializableExtra(Course.COURSE_ACCESS);
        database = Room.databaseBuilder(getApplicationContext(), Database.class, Database.DATABASE_NAME).allowMainThreadQueries().build();

        TextView instructorName = findViewById(R.id.instructor_instr_name);
        TextView instructorPhone = findViewById(R.id.detailed_instructor_view_phone_tf);
        TextView instructorEmail = findViewById(R.id.detailed_instructor_view_email_tf);
        Button saveButton = findViewById(R.id.instructor_save_btn);

        instructor = getInstructor();

        if (instructor != null){
            instructorName.setText(instructor.getName());
            instructorEmail.setText(instructor.getEmail());
            instructorPhone.setText(instructor.getPhone());
        }

        saveButton.setOnClickListener(v -> {
            boolean newInstructor = false;

            if (instructor == null){
                instructor = new Instructor("", "", "", 0);
                newInstructor = true;
            }

            instructor.setEmail(instructorEmail.getText().toString());
            instructor.setPhone(instructorPhone.getText().toString());
            instructor.setName(instructorName.getText().toString());
            instructor.setCourseId(course.getCourseId());

            if (newInstructor){
                database.instructorDAO().insertAll(instructor);
            }
            else{
                database.instructorDAO().update(instructor);
            }

            finish();

        });

        setAppBar();
    }

    private void setAppBar() {
        Toolbar toolbar = findViewById(R.id.instructor_tb);
        toolbar.setTitle(course.getName() + " Instructor");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private Instructor getInstructor() {
        if (database == null || !database.isOpen()){
            database = Room.databaseBuilder(getApplicationContext(), Database.class, Database.DATABASE_NAME).allowMainThreadQueries().build();
        }

        List<CourseInstructor> courseInstructors = database.courseInstructorDAO().getCourseInstructors();
        for (CourseInstructor ci : courseInstructors) {
            if (ci.course.getCourseId() == course.getCourseId()) {
                return ci.instructor;
            }
        }

        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (database != null) {
            if (database.isOpen()) {
                database.close();
            }
        }
    }
}