package com.example.androidstudioproject_tomroche.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TermCourses {
    @Embedded
    public Term term;
    @Relation(entity = Course.class, parentColumn = "termId", entityColumn = "termId")
    public List<Course> courses;
}
