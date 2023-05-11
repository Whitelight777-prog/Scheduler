package com.example.androidstudioproject_tomroche.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Context;
import android.util.Log;

import java.io.Serializable;

public class NotificationExtra implements Serializable {
    String contentTitle;
    String contentText;
    int priority;
    int smallIcon;
    String channelId;

    public NotificationExtra(String contentTitle, String contentText, int priority, int smallIcon, String channelId) {
        this.contentTitle = contentTitle;
        this.contentText = contentText;
        this.priority = priority;
        this.smallIcon = smallIcon;
        this.channelId = channelId;
    }

    public Notification convert(Context context){
        if (context == null){
            Log.e(getClass().getSimpleName(), "Context IS NULL");
        }
        else {
            Log.e(getClass().getSimpleName(), "Context is NOT null");
        }
        return new Notification.Builder(context, channelId)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setChannelId(channelId)
                .setPriority(priority)
                .setSmallIcon(smallIcon)
                .build();
    }
}
