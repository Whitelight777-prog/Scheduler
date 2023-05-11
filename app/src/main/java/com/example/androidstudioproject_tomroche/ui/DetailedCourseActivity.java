package com.example.androidstudioproject_tomroche.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.androidstudioproject_tomroche.R;
import com.example.androidstudioproject_tomroche.dao.Database;
import com.example.androidstudioproject_tomroche.entities.Assessment;
import com.example.androidstudioproject_tomroche.entities.Course;
import com.example.androidstudioproject_tomroche.entities.CourseAssessments;
import com.example.androidstudioproject_tomroche.entities.CourseInstructor;
import com.example.androidstudioproject_tomroche.entities.Instructor;
import com.example.androidstudioproject_tomroche.entities.Term;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DetailedCourseActivity extends AppCompatActivity implements AssessmentListAdapter.AssessmentClickHandler, AdapterView.OnItemSelectedListener {

    Database database = null;
    Course course = null;
    AssessmentListAdapter assessmentListAdapter = null;
    RecyclerView recyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_course);
        Term term = (Term) getIntent().getSerializableExtra(Term.TERM_ACCESS);
        course = (Course) getIntent().getSerializableExtra(Course.COURSE_ACCESS);

        TextView termTextView = findViewById(R.id.detailed_course_term_tf);
        TextView startDateTextView = findViewById(R.id.detailed_course_startdate_tf);
        TextView endDateTextView = findViewById(R.id.detailed_course_enddate_tf);
        TextView instructorTextView = findViewById(R.id.detailed_course_instructor_tf);
        recyclerView = findViewById(R.id.detailed_course_assmnt_rv);
        Button assmntBtn = findViewById(R.id.detailed_course_assmnt_btn);
        Button editInstructorBtn = findViewById(R.id.detailed_course_instructor_edit_btn);
        Spinner courseStatusSpinner = findViewById(R.id.detailed_course_status_sp);
        Button courseNotesBtn = findViewById(R.id.detailed_course_notes_edit_btn);

        termTextView.setText(term.getName());
        startDateTextView.setText(course.getStartDate());
        endDateTextView.setText(course.getEndDate());

        database = Room.databaseBuilder(getApplicationContext(), Database.class, Database.DATABASE_NAME).allowMainThreadQueries().build();

        Instructor instructor = getInstructor();
        if (getIntent().hasExtra(Instructor.INSTRUCTOR_ACCESS)){
            instructor = (Instructor) getIntent().getSerializableExtra(Instructor.INSTRUCTOR_ACCESS);
        }

        if (instructor == null) {
            editInstructorBtn.setText("Add");
        } else {
            editInstructorBtn.setText("Edit");
            instructorTextView.setText(instructor.getName());
        }

        List<CourseAssessments> courseAssessments = database.courseAssDAO().getCourseAssessments();
        if (courseAssessments == null || courseAssessments.size() == 0) {
            assessmentListAdapter = new AssessmentListAdapter(new ArrayList<>());
        } else {
            List<Assessment> assessments = new ArrayList<>();
            for (CourseAssessments ca : courseAssessments) {
                if (ca.course.getCourseId() == course.getCourseId()) {
                    assessments = ca.assessments;
                }
            }
            assessmentListAdapter = new AssessmentListAdapter(assessments);
        }

        assessmentListAdapter.addAssessmentClickHandler(this);
        recyclerView.setAdapter(assessmentListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        assmntBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditAssessmentActivity.class);
            intent.putExtra(Course.COURSE_ACCESS, course);
            startActivity(intent);
        });

        editInstructorBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, DetailedInstructorActivity.class);
            intent.putExtra(Course.COURSE_ACCESS, course);
            startActivity(intent);
        });

        courseStatusSpinner.setOnItemSelectedListener(this);
        courseStatusSpinner.setAdapter(new CourseStatusSpinnerAdapter(this, android.R.layout.simple_spinner_item, Arrays.asList(CourseStatus.values())));
        CourseStatus status = CourseStatus.valueOf(course.getStatus());
        if (status != null && course != null){
            courseStatusSpinner.setSelection(status.ordinal());
        }
        else {
            courseStatusSpinner.setSelection(0);
        }


        courseNotesBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, NotesActivity.class);
            intent.putExtra(Course.COURSE_ACCESS, course);
            startActivity(intent);
        });

        setAppBar();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (database == null || !database.isOpen()){
            database = Room.databaseBuilder(getApplicationContext(), Database.class, Database.DATABASE_NAME).allowMainThreadQueries().build();
        }

        long courseId = ((Course) getIntent().getSerializableExtra(Course.COURSE_ACCESS)).getCourseId();
        course = database.courseDAO().getCourseById(courseId);

        TextView instructorTextView = findViewById(R.id.detailed_course_instructor_tf);
        Button editInstructorBtn = findViewById(R.id.detailed_course_instructor_edit_btn);
        Spinner courseStatusSpinner = findViewById(R.id.detailed_course_status_sp);

        courseStatusSpinner.setOnItemSelectedListener(this);
        courseStatusSpinner.setAdapter(new CourseStatusSpinnerAdapter(this, android.R.layout.simple_spinner_item, Arrays.asList(CourseStatus.values())));

        courseStatusSpinner.setSelection(CourseStatus.fromString(course.getStatus()));

        List<CourseAssessments> courseAssessments = database.courseAssDAO().getCourseAssessments();
        if (courseAssessments == null || courseAssessments.size() == 0) {
            assessmentListAdapter = new AssessmentListAdapter(new ArrayList<>());
        } else {
            List<Assessment> assessments = new ArrayList<>();
            for (CourseAssessments ca : courseAssessments) {
                if (ca.course.getCourseId() == course.getCourseId()) {
                    assessments = ca.assessments;
                }
            }
            assessmentListAdapter = new AssessmentListAdapter(assessments);
        }

        Instructor instructor = getInstructor();

        if (instructor == null) {
            Log.e(getClass().getName(), "Did NOT find instructor!");
            editInstructorBtn.setText("Add");
        } else {
            Log.e(getClass().getName(), "Found instructor: " +
                    instructor.getName() + " " +
                    instructor.getEmail() + " " +
                    instructor.getPhone());
            editInstructorBtn.setText("Edit");
            instructorTextView.setText(instructor.getName());
        }

        assessmentListAdapter.addAssessmentClickHandler(this);
        recyclerView = findViewById(R.id.detailed_course_assmnt_rv);
        recyclerView.setAdapter(assessmentListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setAppBar();
    }

    private void setAppBar() {
        Toolbar toolbar = findViewById(R.id.course_tb);
        toolbar.setTitle(course.getName() + " Course");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
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

    @Override
    public void onAssessmentClicked(Assessment assessment) {
        Intent intent = new Intent(this, DetailedAssessmentActivity.class);
        intent.putExtra(Assessment.ASS_ACCESS, assessment);
        intent.putExtra(Course.COURSE_ACCESS, course);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        course.setStatus(CourseStatus.getByInt(position));
        database.courseDAO().update(course);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private Instructor getInstructor() {
        database = getDatabase();
        List<CourseInstructor> courseInstructors = database.courseInstructorDAO().getCourseInstructors();
        for (CourseInstructor ci : courseInstructors) {
            if (ci.course.getCourseId() == course.getCourseId()) {
                if (ci.instructor != null) {
                    return ci.instructor;
                }
            }
        }

        return null;
    }

    public Database getDatabase(){
        if (database == null || !database.isOpen())
        {
            database = Room
                    .databaseBuilder(getApplicationContext(), Database.class, Database.DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }

        return database;
    }
}
