package space.kheyrati.nanowatch;

import static com.google.firebase.messaging.RemoteMessage.PRIORITY_HIGH;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FcmService extends FirebaseMessagingService implements LifecycleObserver {

    private static final String TAG = "MyFirebaseMsgService";
    private boolean isAppInBackground;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "onMessageReceived: " + remoteMessage);

        if (remoteMessage.getNotification() != null && remoteMessage.getData().size() == 0)
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        else if (remoteMessage.getData().size() > 0 && remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        } else if (remoteMessage.getData().get("title") != null && remoteMessage.getData().get("body") != null)
            sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));

    }

    @Override
    public void onNewToken(@NotNull String token) {
        MSharedPreferences.saveFcmToken(getApplication(), token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        new KheyratiRepository().sendToken(MSharedPreferences.getInstance().getTokenHeader(getApplication()), token);
    }

    public void sendNotification(String title, String messageBody) {

        Intent intent = new Intent();

        if (isAppInBackground) {
            PackageManager pm = getPackageManager();
            intent = pm.getLaunchIntentForPackage(getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }

        String channelId = "karteam_channel_general";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_fingerprint_primary)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_fingerprint_primary))
                        .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title).setSummaryText(messageBody))
                        .setPriority(PRIORITY_HIGH)
                        .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "default_channel_name",
                    NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }


        if (notificationManager != null) {
            notificationManager.notify(createID()/* ID of notification */, notificationBuilder.build());
        }
    }

    public int createID() {
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.US).format(now));
        return id;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void appInResumeState() {
        isAppInBackground = false;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void appInPauseState() {
        isAppInBackground = true;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void appInStopState() {
        isAppInBackground = true;
    }
}
