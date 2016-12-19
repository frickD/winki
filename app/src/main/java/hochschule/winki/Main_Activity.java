package hochschule.winki;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class Main_Activity extends AppCompatActivity {
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("  ");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sie durchsuchen die Bib der HM", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        setContentView(R.layout.semester);
        SemesterList adapter = new SemesterList(Main_Activity.this, Subjects.firstSemester);
        lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(adapter);
    }

    public void onSecondSemester (View view) {
        setContentView(R.layout.semester);
        SemesterList adapter = new SemesterList(Main_Activity.this, Subjects.secondSemester);
        lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(adapter);
    }

    public void onThirdSemester (View view) {
        setContentView(R.layout.semester);
        SemesterList adapter = new SemesterList(Main_Activity.this, Subjects.thirdSemester);
        lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(adapter);
    }

    public void onFourthSemester (View view) {
        setContentView(R.layout.semester);
        SemesterList adapter = new SemesterList(Main_Activity.this, Subjects.fourthSemester);
        lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(adapter);
    }

    public void onFifthSemester (View view) {
        setContentView(R.layout.semester);
        SemesterList adapter = new SemesterList(Main_Activity.this, Subjects.fifthSemester);
        lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(adapter);
    }

    public void onSixthSemester (View view) {
        setContentView(R.layout.semester);
        SemesterList adapter = new SemesterList(Main_Activity.this, Subjects.sixthSemester);
        lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(adapter);
    }

    public void onSeventhSemester (View view) {
        setContentView(R.layout.semester);
        SemesterList adapter = new SemesterList(Main_Activity.this, Subjects.seventhSemester);
        lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(adapter);
    }

    public void onWirtschaftSemester (View view) {
        setContentView(R.layout.semester);
        SemesterList adapter = new SemesterList(Main_Activity.this, Subjects.wpfgWirtschaft);
        lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(adapter);
    }

    public void onITSemester (View view) {
        setContentView(R.layout.semester);
        SemesterList adapter = new SemesterList(Main_Activity.this, Subjects.wpfgIT);
        lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(adapter);
    }

    public void onBack(View view) {
        setContentView(R.layout.activity_main);
    }
}
