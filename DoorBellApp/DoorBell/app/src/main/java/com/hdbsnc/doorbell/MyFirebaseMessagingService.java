package com.hdbsnc.doorbell;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.messaging.RemoteMessage;

import static android.content.Context.AUDIO_SERVICE;


public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //추가한것
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE );
        PowerManager.WakeLock wakeLock = pm.newWakeLock( PowerManager.SCREEN_DIM_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG" );
        wakeLock.acquire(3000);

        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        sendNotification(title, body);
        wakeLock.release();
    }

    private void sendNotification(String title, String body) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        AudioManager audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_doorbell)
                .setContentTitle("현관문 벨이 울렸습니다.") // 이부분은 어플 켜놓은 상태에서 알림 메세지 받으면 저 텍스트를 띄움
                //.setContentText(messageBody)
                .setContentText("카메라로 방문자를 확인하세요.")
                .setColor(getColor(R.color.colorAccent))
                .setVibrate(new long[]{1000, 1000, 500, 1000, 500, 1000, 500, 1000, 500})
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if(audioManager.getRingerMode()== AudioManager.RINGER_MODE_NORMAL)
        {
            notificationBuilder.setVibrate(null);
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
}


