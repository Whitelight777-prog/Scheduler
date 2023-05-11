package com.example.androidstudioproject_tomroche.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "assessments",  foreignKeys = {@ForeignKey(

        entity = Course.class,
        parentColumns = "courseId",
        childColumns = "courseId",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE)})
    public class Assessment implements Serializable {
    public static final String ASS_ACCESS = "assessment";

    @ColumnInfo(name = "name")
    private String name;    @ColumnInfo(name = "start_date")
    private String startDate;
    @ColumnInfo(name = "end_date")
    private String endDate;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "assId")
    private int assId;
    @ColumnInfo(name = "courseId", index = true)
    private long courseId;
    @ColumnInfo(name = "assessmentType")
    private String assessmentType;

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getAssId() {
        return assId;
    }

    public void setAssId(int assId) {
        this.assId = assId;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public Assessment(String name, String startDate, String endDate, long courseId, String assessmentType){
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.courseId = courseId;
        this.assessmentType = assessmentType;
    }

    public String toString(){
        return name;
    }

}
