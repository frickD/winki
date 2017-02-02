package hochschule.winki;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.concurrent.TimeUnit;

import static hochschule.winki.Constants.TAG;
import static hochschule.winki.Constants.CONNECTION_TIME_OUT_MS;
import static hochschule.winki.Constants.GEOFENCE_DATA_ITEM_PATH;
import static hochschule.winki.Constants.GEOFENCE_DATA_ITEM_URI;
import static hochschule.winki.Constants.KEY_GEOFENCE_ID;

/**
 * Created by danielf on 16.01.2017.
 */

public class GeofenceTransitionsIntentService extends IntentService {



    public GeofenceTransitionsIntentService() {
        super(GeofenceTransitionsIntentService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * @param intent Verarbeitung des Intents, dass vom Location Service geschickt wird.
     * Wenn der User den Geofenceraum betritt, so wird eine Notification los geschickt
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geoFenceEvent = GeofencingEvent.fromIntent(intent);
        if (geoFenceEvent.hasError()) {
            int errorCode = geoFenceEvent.getErrorCode();
            Log.e("GEOFENCE", "Location Services error: " + errorCode);
        } else {

            int transitionType = geoFenceEvent.getGeofenceTransition();
            if (Geofence.GEOFENCE_TRANSITION_ENTER == transitionType) {
                Log.e("GEOFENCE", "Enter the Zone");
                showNotification(this);
                Toast.makeText(this, getString(R.string.entering_geofence),
                        Toast.LENGTH_SHORT).show();
            } else if (Geofence.GEOFENCE_TRANSITION_EXIT == transitionType) {
                Log.e("GEOFENCE", "Leaving the Zone");
                showToast(this, R.string.exiting_geofence);
            }
        }
    }

    /**
     * Methode für einen Toast
     */
    private void showToast(final Context context, final int resourceId) {
        Handler mainThread = new Handler(Looper.getMainLooper());
        mainThread.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, context.getString(resourceId), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Notification Darstellung
     */
    private void showNotification(Context context) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, LibaryOpenerActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.hmlogo)
                        .setContentTitle(getString(R.string.welcome_at_HM))
                        .setContentText(getString(R.string.notifation_text));
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }
}