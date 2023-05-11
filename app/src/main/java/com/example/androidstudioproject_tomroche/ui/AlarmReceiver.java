package com.example.androidstudioproject_tomroche.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.androidstudioproject_tomroche.entities.Assessment;
import com.example.androidstudioproject_tomroche.entities.Course;

import java.security.Permissions;
import java.util.UUID;

public class AlarmReceiver extends BroadcastReceiver {
    static String NOTIFICATION_ID = "Notification ID";
    static int ID_NUM = 0;
    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.hasExtra(Course.COURSE_ACCESS)) {
            NotificationManagerCompat nm = NotificationManagerCompat.from(context);
            NotificationChannel notificationChannel = new NotificationChannel(intent.getStringExtra(EditCourseActivity.CHANNEL_ID), "Course Notification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(" ");
            nm.createNotificationChannel(notificationChannel);

            Notification notification = new NotificationCompat.Builder(context, notificationChannel.getId())
                    .setChannelId(intent.getStringExtra(EditCourseActivity.CHANNEL_ID))
                            .setContentTitle(intent.getStringExtra(EditCourseActivity.CONTENT_TITLE))
                                    .setContentText(intent.getStringExtra(EditCourseActivity.CONTENT_TEXT))
                                            .setSmallIcon(intent.getIntExtra(EditCourseActivity.SMALL_ICON, android.R.drawable.btn_default))
                                                    .build();

            nm.notify((int)System.currentTimeMillis(), notification);
        }

//        if(intent.hasExtra(Assessment.ASS_ACCESS)){
//            String assessment = intent.getStringExtra(Assessment.ASS_ACCESS);
//            Notification notification = new NotificationCompat.Builder(context, NotificationChannel.DEFAULT_CHANNEL_ID)
//                    .setContentTitle("Assessment Start Alert")
//                    .setContentText(assessment + " starts tomorrow")
//                    .setPriority(0)
//                    .setSmallIcon(android.R.drawable.sym_def_app_icon)
//                    .setChannelId("Assessment Notification")
//                    .build();
//            NotificationManagerCompat nm = NotificationManagerCompat.from(context);
//            NotificationChannel notificationChannel = new NotificationChannel("Assessment Notification", "Assessment Notification", NotificationManager.IMPORTANCE_DEFAULT);
//            notificationChannel.setDescription(" ");
//            nm.createNotificationChannel(notificationChannel);
//
//            nm.notify(UUID.randomUUID().variant(), notification);
//        }
    }
}
