

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;



/**
 * Created by Anup Dey on 11-Sep-17.
 */

public class NotificationUtils extends ContextWrapper {

    private NotificationManager mManager;
    private Context context;
    public static final String N_CHANNEL_ID = "com.adlabs.apps.icare.REMINDER";
    public static final String N_CHANNEL_NAME = "REMINDER CHANNEL";


    public NotificationUtils(Context base) {
        super(base);
        this.context = base;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            createChannels();
        }
    }


    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }


    public NotificationCompat.Builder getChannelNotificationBuilder(String title, String body ) {
        return new NotificationCompat.Builder(context, N_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_care)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);
    }



    @TargetApi(Build.VERSION_CODES.O)
    private void createChannels() {

        // create android channel
        NotificationChannel androidChannel = null;
        androidChannel = new NotificationChannel(N_CHANNEL_ID, N_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

        // Sets whether notifications posted to this channel should display notification lights
        androidChannel.enableLights(true);
        // Sets whether notification posted to this channel should vibrate.
        androidChannel.enableVibration(true);
        // Sets the notification light color for notifications posted to this channel
        androidChannel.setLightColor(Color.GREEN);
        // Sets whether notifications posted to this channel appear on the lockscreen or not
        androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(androidChannel);

    }

}
