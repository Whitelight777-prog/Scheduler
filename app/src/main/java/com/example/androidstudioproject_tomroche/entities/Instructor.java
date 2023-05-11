package com.example.androidstudioproject_tomroche.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//TODO: Might have to add the foreign key business to the @Entity annotation but
//      the docs don't even mention it at all for this type of implementation
//      Receiving "Foreign Key relation" error, hoping this stops that
@Entity (tableName = "instructors")
public class Instructor implements Serializable {
    public static final String INSTRUCTOR_ACCESS = "Instructor";

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "phone")
    private String phone;
    @ColumnInfo(name = "email")
    private String email;
    @PrimaryKey(autoGenerate = true)
    private int instructorId;
    @ColumnInfo(name = "courseId")
    private long courseId;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public Instructor(String name, String phone, String email, long courseId) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.courseId = courseId;
    }
}
