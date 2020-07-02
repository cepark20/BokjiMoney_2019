package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;
import androidx.core.app.NotificationCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public MyFirebaseMessagingService() {
    }

    @Override
    public void onCreate() {
        PushUtils.acquireWakeLock(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("onMessageReceived","포그라운드 호출됨.");
        String from = remoteMessage.getFrom();
        Map<String,String> data = remoteMessage.getData();
        String msg = data.get("body"); //우리 서버가 데이터를 보낼 때 body라는 키값으로 보낼 것
        String click = data.get("clickAction");

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE); //알림 설정
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "one-channel";
            String channelName = "My Channel One";
            String channelDescription = "My Channel One Description";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channelDescription);
            manager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, channelId);
        } else {
            builder = new NotificationCompat.Builder(this);
        }
        builder.setSmallIcon(R.drawable.bird);
        builder.setContentTitle("복지머니");
        builder.setWhen(System.currentTimeMillis());
        builder.setContentText(msg);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        PushUtils.releaseWakeLock();
        manager.notify(222, builder.build());
    }
}
