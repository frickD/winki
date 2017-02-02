package hochschule.winki;

/**
 * Created by danielf on 16.01.2017.
 */

import com.google.android.gms.location.Geofence;

/**
 * Ein einzelenes Geofence Object, definiert durch die Location und Radius
 */
public class SimpleGeofence {

    // Instance variables
    private final String mId;
    private final double mLatitude;
    private final double mLongitude;
    private final float mRadius;
    private long mExpirationDuration;
    private int mTransitionType;

    /**
     * @param geofenceId Die Geofence ID
     * @param latitude Breitengrad des Geofences Objekts
     * @param longitude Längengrad des Geofences Objekts
     * @param radius Radius vom Geofence Object
     * @param expiration Dauer des Geofences
     * @param transition Type des Geofences
     */
    public SimpleGeofence(String geofenceId, double latitude, double longitude, float radius,
                          long expiration, int transition) {
        // Setzen der Werte
        this.mId = geofenceId;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mRadius = radius;
        this.mExpirationDuration = expiration;
        this.mTransitionType = transition;
    }

    // Getter der Werte
    public String getId() {
        return mId;
    }
    public double getLatitude() {
        return mLatitude;
    }
    public double getLongitude() {
        return mLongitude;
    }
    public float getRadius() {
        return mRadius;
    }
    public long getExpirationDuration() {
        return mExpirationDuration;
    }
    public int getTransitionType() {
        return mTransitionType;
    }

    /**
     * Erzeugung eines Geofence Objekts durch Location Services
     * @return Geofence Objekt
     */
    public Geofence toGeofence() {
        // Erstellen eines neuen Geofences Objekts
        return new Geofence.Builder()
                .setRequestId(mId)
                .setTransitionTypes(mTransitionType)
                .setCircularRegion(mLatitude, mLongitude, mRadius)
                .setExpirationDuration(mExpirationDuration)
                .build();
    }

}
