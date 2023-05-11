package com.example.androidstudioproject_tomroche.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CourseAssessments {
    @Embedded
    public Course course;

    @Relation(entity = Assessment.class, parentColumn = "courseId", entityColumn = "courseId")
    public List<Assessment> assessments;

}
