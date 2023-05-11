package com.example.androidstudioproject_tomroche.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

public class CourseInstructor {
    @Embedded
    public Course course;
    @Relation(
            parentColumn = "courseId",
            entityColumn = "courseId"
    )
    public Instructor instructor;
}
