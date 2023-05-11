package com.example.androidstudioproject_tomroche.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidstudioproject_tomroche.entities.Course;

import java.util.List;

@Dao
public interface CourseDAO {
    @Insert(entity = Course.class)
    void insertAll(Course...courses);

    @Delete(entity = Course.class)
    void delete(Course course);

    @Query("select * from courses")
    List<Course> getAll();

    @Update(entity = Course.class)
    void update(Course...courses);

    @Query("select * from courses where name=:courseName")
    Course getCourseByName(String courseName);

    @Query("select * from courses where courseId =:courseId")
    Course getCourseById(long courseId);

}
