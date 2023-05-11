package com.example.androidstudioproject_tomroche.dao;

import androidx.room.RoomDatabase;

import com.example.androidstudioproject_tomroche.entities.Assessment;
import com.example.androidstudioproject_tomroche.entities.Course;
import com.example.androidstudioproject_tomroche.entities.Instructor;
import com.example.androidstudioproject_tomroche.entities.Term;

@androidx.room.Database(
        entities = {Term.class, Course.class, Assessment.class, Instructor.class},
        exportSchema = true, version = 1)
public abstract class Database extends RoomDatabase {
    public static final String DATABASE_NAME = "terms_db18";
    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract TermCourseDAO termCourseDAO();
    public abstract CourseAssDAO courseAssDAO();
    public abstract AssessmentDAO assessmentDAO();
    public abstract InstructorDAO instructorDAO();
    public abstract CourseInstructorDAO courseInstructorDAO();

}
