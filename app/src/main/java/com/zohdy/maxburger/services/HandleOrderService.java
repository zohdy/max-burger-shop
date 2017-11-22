package com.zohdy.maxburger.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.zohdy.maxburger.R;
import com.zohdy.maxburger.activities.MainActivity;
import com.zohdy.maxburger.common.Common;
import com.zohdy.maxburger.interfaces.Constants;

/**
 * Created by peterzohdy on 16/11/2017.
 */


public class HandleOrderService extends JobService {

    private Notification notification;
    private NotificationManager notificationManager;
    private final static int NOTIFICATION_ID = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onStartJob(JobParameters job) {
        showNotification();
        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false; // Answers the question: "Should this job be retried?"
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showNotification(){

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
        // define sound URI, the sound to be played when there's a notification
        // this is it, we'll build the notification!
        // in the addAction method, if you don't want any icon, just set the first param to 0
            notification = new Notification.Builder(this)
                    .setChannelId(Constants.NOTIFICATIONS_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_logo)
                    .setAutoCancel(true)
                    .setOngoing(false)
                    .setContentTitle(Common.currenUser.getName() + " Din bestilling er klar!")
                    .setContentText("Henvend dig til skranken for at få udleveret din mad!")
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0))
                    .build();
            notification.flags |= Notification.FLAG_NO_CLEAR;

                setSound(true);
                // If you want to hide the notification after it was selected, do the code below
                // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public void cancelNotification(int notificationId){

        if (Context.NOTIFICATION_SERVICE!=null) {
            String notificationService = Context.NOTIFICATION_SERVICE;
            notificationManager.cancel(notificationId);
            setSound(false);
        }
    }

    private void setSound(boolean notificationIsActive) {
        Uri soundUri =  RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), soundUri);

        if(notificationIsActive) {
            ringtone.play();
        } else {
            ringtone.stop();
        }
    }

    private void createChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    Constants.NOTIFICATIONS_CHANNEL_ID,
                    Constants.NOTIFICATIONS_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
