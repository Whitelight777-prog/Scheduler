package com.example.androidstudioproject_tomroche.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidstudioproject_tomroche.entities.Assessment;

import java.util.List;

@Dao
public interface AssessmentDAO {
    @Insert(entity = Assessment.class)
    void insertAll(Assessment...assessments);

    @Delete(entity = Assessment.class)
    void delete(Assessment assessment);

    @Query("select * from assessments")
    List<Assessment> getAll();

    @Update(entity = Assessment.class)
    void update(Assessment...assessments);

    @Query("select * from assessments where name=:assName")
    Assessment getAssessmentByName(String assName);
}
