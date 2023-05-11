package com.example.androidstudioproject_tomroche.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.room.Room;

import com.example.androidstudioproject_tomroche.dao.Database;
import com.example.androidstudioproject_tomroche.entities.Assessment;
import com.example.androidstudioproject_tomroche.entities.Course;

import java.util.List;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent notificationIntent = null;
            Database database = Room.databaseBuilder(context, Database.class, Database.DATABASE_NAME).allowMainThreadQueries().build();
            List<Course> courses = database.courseDAO().getAll();
            int req = 0;
            List<Assessment> assessments = database.assessmentDAO().getAll();
            for(Course course :courses){
                notificationIntent = new Intent(context, AlarmReceiver.class);
                notificationIntent.putExtra(Course.COURSE_ACCESS, course);
                PendingIntent contentIntent = PendingIntent.getActivity(context,++req, notificationIntent, 0);
            }
            for(Assessment assessment :assessments){
                notificationIntent = new Intent(context, AlarmReceiver.class);
                notificationIntent.putExtra(Assessment.ASS_ACCESS, assessment);
                PendingIntent contentIntent = PendingIntent.getActivity(context,++req, notificationIntent, 0);
            }
        }
    }
}
