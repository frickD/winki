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

    // Request code to attempt to resolve Google Play services connection failures.
    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    // Timeout for making a connection to GoogleApiClient (in milliseconds).
    public static final long CONNECTION_TIME_OUT_MS = 100;

    // For the purposes of this demo, the geofences are hard-coded and should not expire.
    // An app with dynamically-created geofences would want to include a reasonable expiration time.
    public static final long GEOFENCE_EXPIRATION_TIME = Geofence.NEVER_EXPIRE;


    // Geofence parameters for the Android building on Google's main campus in Mountain View.
    public static final String REDCUBE_BUILDING_ID = "1";
    public static final double REDCUBE_BUILDING_LATITUDE = 48.154709;
    public static final double REDCUBE_BUILDING_LONGITUDE = 11.557163;
    public static final float REDCUBE_BUILDING_RADIUS_METERS = 50.0f;

    // Geofence parameters for the Yerba Buena Gardens near the Moscone Center in San Francisco.
    public static final String LIBRARY_HM_ID = "2";
    public static final double LIBRARY_HM_LATITUDE = 48.006644;
    public static final double LIBRARY_HM_LONGITUDE = 11.331235;
    public static final float LIBRARY_HM_RADIUS_METERS = 50.0f;


    // The constants below are less interesting than those above.

    // Path for the DataItem containing the last geofence id entered.
    public static final String GEOFENCE_DATA_ITEM_PATH = "/geofenceid";
    public static final Uri GEOFENCE_DATA_ITEM_URI =
            new Uri.Builder().scheme("wear").path(GEOFENCE_DATA_ITEM_PATH).build();
    public static final String KEY_GEOFENCE_ID = "geofence_id";

    // Keys for flattened geofences stored in SharedPreferences.
    public static final String KEY_LATITUDE = "com.example.wearable.geofencing.KEY_LATITUDE";
    public static final String KEY_LONGITUDE = "com.example.wearable.geofencing.KEY_LONGITUDE";
    public static final String KEY_RADIUS = "com.example.wearable.geofencing.KEY_RADIUS";
    public static final String KEY_EXPIRATION_DURATION =
            "com.example.wearable.geofencing.KEY_EXPIRATION_DURATION";
    public static final String KEY_TRANSITION_TYPE =
            "com.example.wearable.geofencing.KEY_TRANSITION_TYPE";
    // The prefix for flattened geofence keys.
    public static final String KEY_PREFIX = "com.example.wearable.geofencing.KEY";

    // Invalid values, used to test geofence storage when retrieving geofences.
    public static final long INVALID_LONG_VALUE = -999l;
    public static final float INVALID_FLOAT_VALUE = -999.0f;
    public static final int INVALID_INT_VALUE = -999;

}

