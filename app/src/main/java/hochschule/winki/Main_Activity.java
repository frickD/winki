package hochschule.winki;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;

import static hochschule.winki.Constants.TAG;
import static hochschule.winki.Constants.REDCUBE_BUILDING_ID;
import static hochschule.winki.Constants.REDCUBE_BUILDING_LATITUDE;
import static hochschule.winki.Constants.REDCUBE_BUILDING_LONGITUDE;
import static hochschule.winki.Constants.REDCUBE_BUILDING_RADIUS_METERS;
import static hochschule.winki.Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST;
import static hochschule.winki.Constants.GEOFENCE_EXPIRATION_TIME;
import static hochschule.winki.Constants.LIBRARY_HM_ID;
import static hochschule.winki.Constants.LIBRARY_HM_LATITUDE;
import static hochschule.winki.Constants.LIBRARY_HM_LONGITUDE;
import static hochschule.winki.Constants.LIBRARY_HM_RADIUS_METERS;

public class Main_Activity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private ListView lv;
    private HashMap<String, String[]> objectMap;
    public static String ArticleTitleBundleKey = "Title";
    private LinearLayout searchLayout;
    private boolean isBackOnSemester = true;
    private String[] backArray;
    private String backString;
    private String backSemester;

    // Internal List of Geofence objects. In a real app, these might be provided by an API based on
    // locations within the user's proximity.
    ArrayList<Geofence> mGeofenceList;

    // These will store hard-coded geofences in this sample app.
    private SimpleGeofence redCubeBuildingGeofence;
    private SimpleGeofence libraryHMGeofence;

    // Persistent storage for geofences.
    private SimpleGeofenceStore mGeofenceStorage;

    private LocationServices mLocationService;
    // Stores the PendingIntent used to request geofence monitoring.
    private PendingIntent mGeofenceRequestIntent;
    private GoogleApiClient mApiClient;

    // Defines the allowable request types (in this example, we only add geofences).
    private enum REQUEST_TYPE {
        ADD
    }

    private REQUEST_TYPE mRequestType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("  ");
        setSupportActionBar(toolbar);
        searchLayout = (LinearLayout) findViewById(R.id.search_layout);
        setSearchButtonListener();
        objectMap = Subjects.getObjectMap();

        if (!isGooglePlayServicesAvailable()) {
            Log.e(TAG, "Google Play services unavailable.");
            Toast.makeText(this, "Google Play services unavailable", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isOnline()){
            setContentView(R.layout.offline);
        }

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mApiClient.connect();

        // Instantiate a new geofence storage area.
        mGeofenceStorage = new SimpleGeofenceStore(this);
        // Instantiate the current List of geofences.
        mGeofenceList = new ArrayList<Geofence>();
        createGeofences();
    }

    /**
     * In this sample, the geofences are predetermined and are hard-coded here. A real app might
     * dynamically create geofences based on the user's location.
     */
    public void createGeofences() {
        // Create internal "flattened" objects containing the geofence data.
        redCubeBuildingGeofence = new SimpleGeofence(
                REDCUBE_BUILDING_ID,                // geofenceId.
                REDCUBE_BUILDING_LATITUDE,
                REDCUBE_BUILDING_LONGITUDE,
                REDCUBE_BUILDING_RADIUS_METERS,
                GEOFENCE_EXPIRATION_TIME,
                Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT
        );
        libraryHMGeofence = new SimpleGeofence(
                LIBRARY_HM_ID,                // geofenceId.
                LIBRARY_HM_LATITUDE,
                LIBRARY_HM_LONGITUDE,
                LIBRARY_HM_RADIUS_METERS,
                GEOFENCE_EXPIRATION_TIME,
                Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT
        );

        // Store these flat versions in SharedPreferences and add them to the geofence list.
        mGeofenceStorage.setGeofence(REDCUBE_BUILDING_ID, redCubeBuildingGeofence);
        mGeofenceStorage.setGeofence(LIBRARY_HM_ID, libraryHMGeofence);
        mGeofenceList.add(redCubeBuildingGeofence.toGeofence());
        mGeofenceList.add(libraryHMGeofence.toGeofence());
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // If the error has a resolution, start a Google Play services activity to resolve it.
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG, "Exception while resolving connection error.", e);
            }
        } else {
            int errorCode = connectionResult.getErrorCode();
            Log.e(TAG, "Connection to Google Play services failed with error code " + errorCode);
        }
    }

    /**
     * Once the connection is available, send a request to add the Geofences.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Get the PendingIntent for the geofence monitoring request.
        // Send a request to add the current geofences.
        mGeofenceRequestIntent = getGeofenceTransitionPendingIntent();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getString(R.string.no_gps_permission), Toast.LENGTH_SHORT).show();
            return;
        }
        LocationServices.GeofencingApi.addGeofences(mApiClient, mGeofenceList,
                mGeofenceRequestIntent);
        Toast.makeText(this, getString(R.string.start_geofence_service), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (null != mGeofenceRequestIntent) {
            LocationServices.GeofencingApi.removeGeofences(mApiClient, mGeofenceRequestIntent);
        }
    }


    /**
     * Checks if Google Play services is available.
     * @return true if it is.
     */
    private boolean isGooglePlayServicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "Google Play services is available.");
            }
            return true;
        } else {
            Log.e(TAG, "Google Play services is unavailable.");
            return false;
        }
    }

    /**
     * Checks if Mobile has internet Connection.
     * @return true if it is.
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Create a PendingIntent that triggers GeofenceTransitionIntentService when a geofence
     * transition occurs.
     */
    private PendingIntent getGeofenceTransitionPendingIntent() {
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void onReload(View view) {
        Intent myIntent = new Intent(Main_Activity.this, Main_Activity.class);
        startActivity(myIntent);
    }

    public void onFirstSemester(View view) {
        backString = "1. Semester";
        backSemester = backString;
        openList(Subjects.firstSemester);
        setHeadline(backString);
    }

    public void onSecondSemester (View view) {
        backString = "2. Semester";
        backSemester = backString;
        openList(Subjects.secondSemester);
        setHeadline(backString);
    }

    public void onThirdSemester (View view) {
        backString = "3. Semester";
        backSemester = backString;
        openList(Subjects.thirdSemester);
        setHeadline(backString);
    }

    public void onFourthSemester (View view) {
        backString = "4. Semester";
        backSemester = backString;
        openList(Subjects.fourthSemester);
        setHeadline(backString);
    }

    public void onFifthSemester (View view) {
        Intent myIntent = new Intent(Main_Activity.this, WikipediaItem.class);
        Bundle b = new Bundle();
        b.putString(ArticleTitleBundleKey, "Praxissemester");
        myIntent.putExtras(b);
        startActivity(myIntent);
    }

    public void onSixthSemester (View view) {
        backString = "6. Semester";
        backSemester = backString;
        openList(Subjects.sixthSemester);
        setHeadline(backString);
    }

    public void onSeventhSemester (View view) {
        Intent myIntent = new Intent(Main_Activity.this, WikipediaItem.class);
        Bundle b = new Bundle();
        b.putString(ArticleTitleBundleKey, "Bachelorarbeit");
        myIntent.putExtras(b);
        startActivity(myIntent);
    }

    public void onCloseSearch(View view) {
        searchLayout.setVisibility(View.GONE);
    }
    public void onWirtschaftSemester (View view) {
        backString = "Wirtschaft Wahlfächer";
        backSemester = backString;
        openList(Subjects.wpfgWirtschaft);
        setHeadline(backString);
    }

    public void onITSemester (View view) {
        backString = "IT Wahlfächer";
        backSemester = backString;
        openList(Subjects.wpfgIT);
        setHeadline(backString);
    }

    private void openList(final String[] givenSubject) {
        this.backArray = givenSubject;
        List adapter = setListView(givenSubject);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), givenSubject[position], Toast.LENGTH_SHORT).show();
                openSubject(objectMap.get(givenSubject[position]));
                backString = givenSubject[position];
                setHeadline(backString);
                isBackOnSemester = false;
            }
        });
        lv.setAdapter(adapter);
    }

    private void openSubject(final String[] givenSubject) {
        List adapter = setListView(givenSubject);
        lv.setOnItemClickListener((new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent myIntent = new Intent(Main_Activity.this, WikipediaItem.class);
                        Bundle b = new Bundle();
                        b.putString(ArticleTitleBundleKey, givenSubject[position]);
                        myIntent.putExtras(b);
                        startActivity(myIntent);
                    }
                }));
        lv.setAdapter(adapter);
    }

    private List setListView(final String[] givenSubject) {
        setContentView(R.layout.list);
        List adapter = new List(Main_Activity.this, givenSubject);
        lv = (ListView) findViewById(R.id.listview);
        return adapter;
    }

    @Override
    public void onBackPressed() {
        if(this.isBackOnSemester) {
            setContentView(R.layout.activity_main);
            setSearchButtonListener();
        }
        else{
            openList(backArray);
            setHeadline(backSemester);
            isBackOnSemester = true;
        }
    }

    private void setSearchButtonListener() {
        searchLayout = (LinearLayout) findViewById(R.id.search_layout);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Geben Sie Ihren Suchbegriff ein", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                searchLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void onSearch(View view) {
        EditText searchEditText = (EditText) findViewById(R.id.searchInput);
        String searchInput = searchEditText.getText().toString();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://de.m.wikipedia.org/wiki/" + searchInput));
        startActivity(browserIntent);
    }

    private void setHeadline (String clickedItem) {
        TextView headline = (TextView) findViewById(R.id.header);
        headline.setText(clickedItem);
    }
}
