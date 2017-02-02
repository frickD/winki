package hochschule.winki;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class LibaryOpenerActivity extends AppCompatActivity {

    /*
     * Falls die Notification angeklickt wird, so wird diese Klasse geöffnet, die gleich zum Browser weiterleitet
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://fhmoz3.bib-bvb.de/InfoGuideClient.fhmsis/start.do?Login=wofhmze&device=mobile"));
        startActivity(browserIntent);
    }
}
