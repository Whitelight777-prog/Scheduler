package com.example.androidstudioproject_tomroche.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidstudioproject_tomroche.dao.Database;
import com.example.androidstudioproject_tomroche.entities.Course;
import com.example.androidstudioproject_tomroche.entities.Term;
import com.example.androidstudioproject_tomroche.entities.TermCourses;
import com.example.androidstudioproject_tomroche.R;

import java.util.ArrayList;
import java.util.List;

public class DetailedTermActivity extends AppCompatActivity implements CourseListAdapter.CourseClickHandler {

    Database database = null;
    Term term = null;
    CourseListAdapter courseListAdapter = null;
    RecyclerView recyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_term);

        term = (Term) getIntent().getSerializableExtra(Term.TERM_ACCESS);

        TextView startDateTextView = findViewById(R.id.term_detailed_view_startdate_tf);
        TextView endDateTextView = findViewById(R.id.term_detailed_view_enddate_tf);
        recyclerView = findViewById(R.id.recyclerView);

        startDateTextView.setText(term.getStartDate());
        endDateTextView.setText(term.getEndDate());

        database = Room.databaseBuilder(getApplicationContext(), Database.class, Database.DATABASE_NAME).allowMainThreadQueries().build();
        List<TermCourses> courses = database.termCourseDAO().getTermsWithCourses();

        if(courses == null || courses.size() <= 0){
            courseListAdapter = new CourseListAdapter(new ArrayList<>());
        }
        else{
            List<Course> courseList = new ArrayList<>();
            for (TermCourses tc : courses){
                if (tc.term.getTermId() == term.getTermId()){
                    courseList = tc.courses;
                    break;
                }
            }
            courseListAdapter = new CourseListAdapter(courseList);
        }

        courseListAdapter.addCourseClickHandler(this);
        recyclerView.setAdapter(courseListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button manageButton = findViewById(R.id.term_detailed_view_edit_courses_btn);
        Term finalTerm = term;
        manageButton.setOnClickListener(view ->{
            Intent intent = new Intent(this, EditCourseActivity.class);
            intent.putExtra(Term.TERM_ACCESS, finalTerm);
            startActivity(intent);
        });

        setAppBar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (database != null){
            if (database.isOpen()){
                database.close();
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume(){
        super.onResume();
        List<TermCourses> courses = database.termCourseDAO().getTermsWithCourses();
        List<Course> courseList = new ArrayList<>();

        for (TermCourses tc : courses){
            if (tc.term.getTermId() == term.getTermId()){
                courseList = tc.courses;
                break;
            }
        }

        courseListAdapter = new CourseListAdapter(courseList);
        courseListAdapter.notifyDataSetChanged();

        courseListAdapter.addCourseClickHandler(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(courseListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setAppBar();
    }

    @Override
    public void onCourseClicked(Course course) {
        Intent intent = new Intent(this, DetailedCourseActivity.class);
        intent.putExtra(Course.COURSE_ACCESS,course);
        intent.putExtra(Term.TERM_ACCESS,term);
        startActivity(intent);
    }

    private void setAppBar() {
        Toolbar toolbar = findViewById(R.id.term_tb);
        toolbar.setTitle(term.getName() + " Term");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }
}