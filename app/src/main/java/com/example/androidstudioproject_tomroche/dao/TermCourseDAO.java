package com.example.androidstudioproject_tomroche.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.androidstudioproject_tomroche.entities.TermCourses;

import java.util.List;

@Dao
public interface TermCourseDAO {
    @Transaction
    @Query("select * from terms")
    List<TermCourses> getTermsWithCourses();

}
