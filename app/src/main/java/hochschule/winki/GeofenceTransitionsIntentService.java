package hochschule.winki;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

public class GeofenceTransitionsIntentService extends IntentService
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;

    public GeofenceTransitionsIntentService() {
        super(GeofenceTransitionsIntentService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    /**
     * Handles incoming intents.
     * @param intent The Intent sent by Location Services. This Intent is provided to Location
     * Services (inside a PendingIntent) when addGeofences() is called.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geoFenceEvent = GeofencingEvent.fromIntent(intent);
        if (geoFenceEvent.hasError()) {
            int errorCode = geoFenceEvent.getErrorCode();
            Log.e(TAG, "Location Services error: " + errorCode);
        } else {

            int transitionType = geoFenceEvent.getGeofenceTransition();
            if (Geofence.GEOFENCE_TRANSITION_ENTER == transitionType) {
                // Connect to the Google Api service in preparation for sending a DataItem.
                mGoogleApiClient.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                // Get the geofence id triggered. Note that only one geofence can be triggered at a
                // time in this example, but in some cases you might want to consider the full list
                // of geofences triggered.
                String triggeredGeoFenceId = geoFenceEvent.getTriggeringGeofences().get(0)
                        .getRequestId();
                // Create a DataItem with this geofence's id. The wearable can use this to create
                // a notification.
                PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(GEOFENCE_DATA_ITEM_PATH);
                putDataMapRequest.getDataMap().putString(KEY_GEOFENCE_ID, triggeredGeoFenceId);
                putDataMapRequest.setUrgent();
                if (mGoogleApiClient.isConnected()) {
                    Wearable.DataApi.putDataItem(
                            mGoogleApiClient, putDataMapRequest.asPutDataRequest()).await();
                } else {
                    Log.e(TAG, "Failed to send data item: " + putDataMapRequest
                            + " - Client disconnected from Google Play Services");
                }
                Toast.makeText(this, getString(R.string.entering_geofence),
                        Toast.LENGTH_SHORT).show();
                mGoogleApiClient.disconnect();
            } else if (Geofence.GEOFENCE_TRANSITION_EXIT == transitionType) {
                // Delete the data item when leaving a geofence region.
                mGoogleApiClient.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                Wearable.DataApi.deleteDataItems(mGoogleApiClient, GEOFENCE_DATA_ITEM_URI).await();
                showToast(this, R.string.exiting_geofence);
                mGoogleApiClient.disconnect();
            }
        }
    }

    /**
     * Showing a toast message, using the Main thread
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

    @Override
    public void onConnected(Bundle connectionHint) {
    }

    @Override
    public void onConnectionSuspended(int cause) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
    }

}