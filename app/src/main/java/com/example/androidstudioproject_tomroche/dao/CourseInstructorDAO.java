package com.example.androidstudioproject_tomroche.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.androidstudioproject_tomroche.entities.CourseInstructor;

import java.util.List;

@Dao
public interface CourseInstructorDAO {
    @Transaction
    @Query("select * from courses")
    List<CourseInstructor> getCourseInstructors();
}
