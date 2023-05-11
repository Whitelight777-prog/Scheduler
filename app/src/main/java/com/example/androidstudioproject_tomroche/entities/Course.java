package com.example.androidstudioproject_tomroche.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.androidstudioproject_tomroche.ui.CourseStatus;

import java.io.Serializable;

@Entity (tableName = "courses", foreignKeys = {
        @ForeignKey(
                entity = Term.class,
                parentColumns = "termId",
                childColumns = "termId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        )
})

public class Course implements Serializable {
    public static final String COURSE_ACCESS = "course";

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "start_date")
    private String startDate;
    @ColumnInfo(name = "end_date")
    private String endDate;
    @PrimaryKey(autoGenerate = true)
    private int courseId;
    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "notes")
    private String notes;
    @ColumnInfo(name = "termId", index = true)
    private long termId;

    public long getTermId() {
        return termId;
    }

    public void setTermId(long termId) {
        this.termId = termId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Course(String name, String startDate, String endDate, long termId, String status) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.termId = termId;
        this.status = status;
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

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
