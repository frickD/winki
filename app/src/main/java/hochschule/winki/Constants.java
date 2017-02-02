package hochschule.winki;

import android.net.Uri;
import com.google.*;
import com.google.android.gms.location.Geofence;


/**
 * Created by danielf on 16.01.2017.
 */

public class Constants {

    private Constants() {
    }

    public static final String TAG = "Winkigeofencing";

    // Request code um die Google Play services Verbindungsprobleme zu beheben.
    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    // Timeout für die Verbdinung zum GoogleApiClient (in millisekunden).
    public static final long CONNECTION_TIME_OUT_MS = 100;

    // Die Geofences in der App sind hardgecoded und sollten niemals 'enden'
    public static final long GEOFENCE_EXPIRATION_TIME = Geofence.NEVER_EXPIRE;


    // Geofence parameter für den roten Würfel der HM
    public static final String REDCUBE_BUILDING_ID = "1";
    public static final double REDCUBE_BUILDING_LATITUDE = 48.154709;
    public static final double REDCUBE_BUILDING_LONGITUDE = 11.557163;
    public static final float REDCUBE_BUILDING_RADIUS_METERS = 50.0f;

    // Geofence parameter für die Zentralbiblothek der HM
    public static final String LIBRARY_HM_ID = "2";
    public static final double LIBRARY_HM_LATITUDE = 48.007927;
    public static final double LIBRARY_HM_LONGITUDE = 11.333095;
    public static final float LIBRARY_HM_RADIUS_METERS = 50.0f;

    // Geofence parameter
    public static final String GEOFENCE_DATA_ITEM_PATH = "/geofenceid";
    public static final Uri GEOFENCE_DATA_ITEM_URI =
            new Uri.Builder().scheme("wear").path(GEOFENCE_DATA_ITEM_PATH).build();
    public static final String KEY_GEOFENCE_ID = "geofence_id";

}

