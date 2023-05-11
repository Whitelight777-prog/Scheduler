package com.example.androidstudioproject_tomroche.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.androidstudioproject_tomroche.dao.Database;
import com.example.androidstudioproject_tomroche.entities.Course;
import com.example.androidstudioproject_tomroche.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotesActivity extends AppCompatActivity {
    Course course = null;
    Database database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        database = Room.databaseBuilder(getApplicationContext(), Database.class, Database.DATABASE_NAME).allowMainThreadQueries().build();

        String courseName = ((Course)(getIntent().getSerializableExtra(Course.COURSE_ACCESS))).getName();
        course = database.courseDAO().getCourseByName(courseName);

        EditText notesText = findViewById(R.id.notes_multiline_tf);
        TextView notesCourse = findViewById(R.id.notes_course_tf);
        Button notesButton = findViewById(R.id.save_btn);
        FloatingActionButton shareBtn = findViewById(R.id.notes_share_notes_fabtn);

        notesText.setText(course.getNotes());
        notesCourse.setText(course.getName());
        notesButton.setOnClickListener(v -> {
            String notes = notesText.getText().toString();
            course.setNotes(notes);
            database.courseDAO().update(course);
        });

        shareBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, courseName + " Notes");
            intent.putExtra(Intent.EXTRA_TEXT, notesText.getText().toString());
            startActivity(Intent.createChooser(intent, "Share Via"));
        });

        setAppBar();
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
    public void onResume() {
        super.onResume();
        if (database == null || !database.isOpen()){
            database = Room.databaseBuilder(getApplicationContext(), Database.class, Database.DATABASE_NAME).allowMainThreadQueries().build();
        }

        if (course == null){
            long courseId = ((Course) getIntent().getSerializableExtra(Course.COURSE_ACCESS)).getCourseId();
            course = database.courseDAO().getCourseById(courseId);
        }

        EditText notesText = findViewById(R.id.notes_multiline_tf);
        TextView notesCourse = findViewById(R.id.notes_course_tf);
        Button notesButton = findViewById(R.id.save_btn);

        notesCourse.setText(course.getName());
        notesButton.setOnClickListener(v -> {
            String notes = String.valueOf(notesText.getText());
            course.setNotes(notes);
            database.courseDAO().update(course);
        });

        setAppBar();
    }

    private void setAppBar() {
        Toolbar toolbar = findViewById(R.id.notes_tb);
        setSupportActionBar(toolbar);
        toolbar.setTitle(course.getName() + " Notes");
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }
}