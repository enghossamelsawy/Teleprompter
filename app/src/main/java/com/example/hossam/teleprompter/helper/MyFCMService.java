package com.example.hossam.teleprompter.helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.hossam.teleprompter.R;
import com.example.hossam.teleprompter.TextRotation;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Mina William on 5/18/17.
 * Xdigital Group company
 */

public class MyFCMService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);



            //Displaying data in log
            //It is optional
            Log.d("test1", "From: " + remoteMessage.getFrom());
            //Log.d("text", "Notification Message Body: " + remoteMessage.getNotification().getBody());

            //Calling method to generate notification
            sendNotification(remoteMessage.getNotification().getBody());
        }

        //This method is only generating push notification
        //It is same as we did in earlier posts
        private void sendNotification(String messageBody) {
            Intent intent = new Intent(this, TextRotation.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("Firebase Push Notification")
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
        }


}

