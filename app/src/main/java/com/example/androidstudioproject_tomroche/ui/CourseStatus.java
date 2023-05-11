package com.example.androidstudioproject_tomroche.ui;

public enum CourseStatus {
    IN_PROGRESS, COMPLETED, DROPPED, PLAN_TO_TAKE, UNKNOWN;

    public static String getByInt(int pos){
        for(CourseStatus status:CourseStatus.values()){
            if(status.ordinal() == pos){
                return status.name();
            }
        }
        return null;

    }

    public static int fromString(String courseStatus){
        return CourseStatus.valueOf(courseStatus).ordinal();
    }

}
