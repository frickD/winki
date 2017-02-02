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

    //Die Hashmap, die dafür verantwortlich ist, dass das die richtigen Begriffe dargestellt werden
    private HashMap<String, String[]> objectMap;

    public static String ArticleTitleBundleKey = "Title";

    //Suchzeile
    private LinearLayout searchLayout;

    //Zwischenspeicherung der ausgewählten Sachen
    private boolean isBackOnSemester = true;
    private String[] backArray;
    private String backString;
    private String backSemester;

    // Liste von Geofence Objekten
    ArrayList<Geofence> mGeofenceList;

    // Geofence Objekte in der App
    private SimpleGeofence redCubeBuildingGeofence;
    private SimpleGeofence libraryHMGeofence;

    private LocationServices mLocationService;
    // PendingIntent, das benutzt wird um die Geofences zu überwachen
    private PendingIntent mGeofenceRequestIntent;
    private GoogleApiClient mApiClient;

    // Definiert die möglichen Anfragetypen, jedoch fügen wir die Geofences nur hinzu.
    private enum REQUEST_TYPE {
        ADD
    }

    private REQUEST_TYPE mRequestType;

    /**  Erstellung der MaiActivity:
     *   -Öffnen des Layouts
     *   -Herholen der Hashmap
     *   -Überprüfung, ob man eine Verbindung zu den Google Play Services hat
     *   -Überprüfung, ob man eine Internetverbindung hat (wenn nicht dann wird das 'offline' Layout geladen
     *   -Herstellung der Verbindung zu Google API
     */
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
            Log.e(TAG, getString(R.string.no_googleplay_service));
            Toast.makeText(this, getString(R.string.no_googleplay_service), Toast.LENGTH_SHORT).show();
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
        mGeofenceList = new ArrayList<Geofence>();
        createGeofences();
    }

    /**
     * Erzeugen der Geofences (Roter Würfel und die Bib)
     */
    public void createGeofences() {
        // Erzeugen der Geofence Objekte mit den gegebenen Werten
        redCubeBuildingGeofence = new SimpleGeofence(
                REDCUBE_BUILDING_ID,
                REDCUBE_BUILDING_LATITUDE,
                REDCUBE_BUILDING_LONGITUDE,
                REDCUBE_BUILDING_RADIUS_METERS,
                GEOFENCE_EXPIRATION_TIME,
                Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT
        );
        libraryHMGeofence = new SimpleGeofence(
                LIBRARY_HM_ID,
                LIBRARY_HM_LATITUDE,
                LIBRARY_HM_LONGITUDE,
                LIBRARY_HM_RADIUS_METERS,
                GEOFENCE_EXPIRATION_TIME,
                Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT
        );

        // Die Geofences werden der Liste hinzugefügt
        mGeofenceList.add(redCubeBuildingGeofence.toGeofence());
        mGeofenceList.add(libraryHMGeofence.toGeofence());
    }


    /**
     * Falls die Verbindung fehl schlägt, so wird nach der möglichen Ursache geschaut
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
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
     * Wenn es eine Verbindung gibt, so wird ein Request rausgeschickt, um die Geofences hinzuzufügen
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Das PendingIntent für die Geofencing Überwachungsabfrage wird geholt
        // Request wird versendet und die aktuellen Geofences (in der Liste) werden übergeben
        mGeofenceRequestIntent = getGeofenceTransitionPendingIntent();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getString(R.string.no_gps_permission), Toast.LENGTH_SHORT).show();
            return;
        }
        LocationServices.GeofencingApi.addGeofences(mApiClient, mGeofenceList,
                mGeofenceRequestIntent);
        Toast.makeText(this, getString(R.string.start_geofence_service), Toast.LENGTH_SHORT).show();
    }

    /**
     * Wird die Verbindung aufgehoben, so werden die Geofences entfernt
     */
    @Override
    public void onConnectionSuspended(int i) {
        if (null != mGeofenceRequestIntent) {
            LocationServices.GeofencingApi.removeGeofences(mApiClient, mGeofenceRequestIntent);
        }
    }


    /**
     * Überprüft, ob die Google Play Services erreichbar sind
     * @return true, wenn sie es sind
     */
    private boolean isGooglePlayServicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, getString(R.string.googleplay_service_available));
            }
            return true;
        } else {
            Log.e(TAG, getString(R.string.no_googleplay_service));
            return false;
        }
    }

    /**
     *  Überprüft, ob das Gerät eine aktive Internetverbindung hat
     * @return true, wenn es das ist
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Erstellt ein PendingIntent, das überwacht wenn sich was im Geofencing passiert (z.B. User betritt die Zone)
     * @return Das PendingIntent
     */
    private PendingIntent getGeofenceTransitionPendingIntent() {
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Neuladen der Activity Klasse
     */
    public void onReload(View view) {
        Intent myIntent = new Intent(Main_Activity.this, Main_Activity.class);
        startActivity(myIntent);
    }

    /**
     * onClick Methoden, falls der User z.B. das erste Semester öffnet
     * so wird auch zwischengespeichert auf was er geklickt hat (im Fall, dass er zurückklickt)
     * Die Liste mit dem Array firstSemester wird geöffnet
     * und die Überschrift wird auf das gesetzt, was er angeklickt hat
     */
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

    /**
     * Im fünften und im siebten Semester wird gleich die WikipediaItem Activty aufgerufen,
     * da es hier keine Kurse gibt
     * Der Wikipedia Artivel wird geladen.
     */
    public void onFifthSemester (View view) {
        Intent myIntent = new Intent(Main_Activity.this, WikipediaItem.class);
        Bundle b = new Bundle();
        b.putString(ArticleTitleBundleKey, getString(R.string.praxissemester));
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
        b.putString(ArticleTitleBundleKey, getString(R.string.bachelor));
        myIntent.putExtras(b);
        startActivity(myIntent);
    }

    public void onWirtschaftSemester (View view) {
        backString = getString(R.string.economics);
        backSemester = backString;
        openList(Subjects.wpfgWirtschaft);
        setHeadline(backString);
    }

    public void onITSemester (View view) {
        backString = getString(R.string.it);
        backSemester = backString;
        openList(Subjects.wpfgIT);
        setHeadline(backString);
    }

    /**
     * onClick Methode, um die Suchleiste wieder zu 'schließen'
     */
    public void onCloseSearch(View view) {
        searchLayout.setVisibility(View.GONE);
    }


    /**
     * Öffnen der Liste der Fächer:
     * Das Array, das vorher mitgeben wurde, wird zwischen gespeichert.
     * Der onItemClickListener wird aktiviert.
     * Beim Auswählen eines Faches wird im übergebenen Array geschaut, was genau angeklickt wurde
     * dies wird nun in der Hashmap durchsucht und die Hashmap liefert ein Array mit den Begriffen des Faches.
     * Dieses Array der Hashmap wird nun für die neue Liste weitergeben, um die Begriffe darzustellen
     * Auch hier wird das angeklickte Fach zwischen gespeichert, und die Headline wird gesetzt
     */
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

    /**
     * Öffnen der Liste der Begriffe:
     * Der onItemClickListener wird aktiviert.
     * Beim Anklicken eines Begriffes wird die Wikipedia Item Activity geladen,
     * dabei wird nun der ausgewählt Begriff an die Klasse mit übergeben.
     */
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

    /**
     * Das eigentliche öffnen der Liste bzw. Laden des Listen Layouts
     * Als input wird hier ein String Array verwendet.
     * Dieses String Array ist sozusagen der Content der Liste
     * @return der Listadapter wird zurückgegeben
     */
    private List setListView(final String[] givenSubject) {
        setContentView(R.layout.list);
        List adapter = new List(Main_Activity.this, givenSubject);
        lv = (ListView) findViewById(R.id.listview);
        return adapter;
    }

    /**
     * Falls der User zurück klickt,
     * so wird geschaut, wo er sich befindet, ob er nun die Fächer oder die Startseite sehen will
     * Wenn er zur Startseite will, so wird das MainActivity Layout geladen
     * Wenn er die Fächer sehen will, so wird die Liste erneut geladen mit dem zwischen gespeicherten Array
     */
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

    /**
     * Der SearchButton Listener
     * Falls der User suchen will, so wird die Suchzeile dargestellt und der User benachrichtigt
     */
    private void setSearchButtonListener() {
        searchLayout = (LinearLayout) findViewById(R.id.search_layout);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.start_searching), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                searchLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * onClick Methode
     * Wenn der User die Suche starten will,
     * so wird der Browser geöffnet mit dem Wikipedia Link zu dem eingegeben Input
     */
    public void onSearch(View view) {
        EditText searchEditText = (EditText) findViewById(R.id.searchInput);
        String searchInput = searchEditText.getText().toString();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://de.m.wikipedia.org/wiki/" + searchInput));
        startActivity(browserIntent);
    }

    /**
     * Setzen der Überschrift
     */
    private void setHeadline (String clickedItem) {
        TextView headline = (TextView) findViewById(R.id.header);
        headline.setText(clickedItem);
    }
}
