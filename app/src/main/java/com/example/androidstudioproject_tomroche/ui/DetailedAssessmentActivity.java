package com.example.androidstudioproject_tomroche.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.androidstudioproject_tomroche.R;
import com.example.androidstudioproject_tomroche.dao.Database;
import com.example.androidstudioproject_tomroche.entities.Assessment;
import com.example.androidstudioproject_tomroche.entities.Course;

public class DetailedAssessmentActivity extends AppCompatActivity {
    Database database;
    Course course;
    Assessment assessment;
    TextView startDate;
    TextView endDate;
    TextView courseName;
    TextView assessmentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_assessment);
        course = (Course) getIntent().getSerializableExtra(Course.COURSE_ACCESS);
        assessment = (Assessment) getIntent().getSerializableExtra(Assessment.ASS_ACCESS);

        setTextFields();

        Toolbar toolbar = findViewById(R.id.assessment_tb);
        toolbar.setTitle(assessment.getName() + " Assessment");

        setAppBar();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (course == null) {
            course = (Course) getIntent().getSerializableExtra(Course.COURSE_ACCESS);
        }

        if (assessment == null) {
            assessment = (Assessment) getIntent().getSerializableExtra(Assessment.ASS_ACCESS);
        }

        setTextFields();
        setAppBar();
    }

    private void setAppBar() {
        Toolbar toolbar = findViewById(R.id.assessment_tb);
        toolbar.setTitle(assessment.getName() + " Assessment");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void setTextFields() {
        courseName = findViewById(R.id.detailed_assessment_coursename_tf);
        startDate = findViewById(R.id.detailed_assessment_startdate_tf);
        endDate = findViewById(R.id.detailed_assessment_enddate_tf);
        assessmentType = findViewById(R.id.assessment_type_rb);

        courseName.setText(course.getName());
        startDate.setText(assessment.getStartDate());
        endDate.setText(assessment.getEndDate());
        assessmentType.setText(assessment.getAssessmentType());
    }
}