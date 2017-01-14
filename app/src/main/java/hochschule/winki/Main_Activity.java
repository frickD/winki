package hochschule.winki;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;

public class Main_Activity extends AppCompatActivity {
    private ListView lv;
    private HashMap<String, String[]> objectMap;
    private HashMap<String, String> wikiTitleMap;
    public static String WikiTitleBundleKey = "wikiTitle";
    public static String ArticleTitleBundleKey = "Title";
    private LinearLayout searchLayout;
    private boolean isBackOnSemester = true;
    private String[] backArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("  ");
        setSupportActionBar(toolbar);
        searchLayout = (LinearLayout) findViewById(R.id.search_layout);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sie durchsuchen die Bib der HM", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                 searchLayout.setVisibility(View.VISIBLE);
            }
        });
        objectMap = Subjects.getObjectMap();
        wikiTitleMap = Subjects.getWikipediaTitle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Settings ausgewählt", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onFirstSemester(View view) {
        openList(Subjects.firstSemester);
    }

    public void onSecondSemester (View view) {
        openList(Subjects.secondSemester);
    }

    public void onThirdSemester (View view) {
        openList(Subjects.thirdSemester);
    }

    public void onFourthSemester (View view) {
        openList(Subjects.fourthSemester);
    }

    public void onFifthSemester (View view) {
        Intent myIntent = new Intent(Main_Activity.this, WikipediaItem.class);
        Bundle b = new Bundle();
        b.putString(WikiTitleBundleKey, "Praxissemester");
        b.putString(ArticleTitleBundleKey, "Praxissemester");
        myIntent.putExtras(b);
        startActivity(myIntent);
    }

    public void onSixthSemester (View view) {
        openList(Subjects.sixthSemester);
    }

    public void onSeventhSemester (View view) {
        Intent myIntent = new Intent(Main_Activity.this, WikipediaItem.class);
        Bundle b = new Bundle();
        b.putString(WikiTitleBundleKey, "Bachelorarbeit");
        b.putString(ArticleTitleBundleKey, "Bachelorarbeit");
        myIntent.putExtras(b);
        startActivity(myIntent);
    }

    public void onCloseSearch(View view) {
        searchLayout.setVisibility(View.GONE);
    }
    public void onWirtschaftSemester (View view) {
        openList(Subjects.wpfgWirtschaft);
    }

    public void onITSemester (View view) {
        openList(Subjects.wpfgIT);
    }

    private void openList(final String[] givenSubject) {
        this.backArray = givenSubject;
        List adapter = setListView(givenSubject);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), givenSubject[position], Toast.LENGTH_SHORT).show();
                openSubject(objectMap.get(givenSubject[position]));
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
                        b.putString(WikiTitleBundleKey, wikiTitleMap.get(givenSubject[position]));
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
        }
        else{
            openList(backArray);
            isBackOnSemester = true;
        }
    }
}
