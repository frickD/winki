package hochschule.winki;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Hollinger on 20.12.2016.
 */

public class WikipediaItem extends AppCompatActivity {

    private ProgressDialog pd;
    private TextView txtJson;
    private String wikipediaTitle;

    /*
     * Laden des Wikipedia Items
     * Es wird der Titel des gewählten Begriffes dargestellt
     * und ein request an die Wikipedia API mit dem Begriff losgeschickt
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wiki_article);
        TextView title = (TextView) findViewById(R.id.titlearticle);
        txtJson = (TextView) findViewById(R.id.article);
        Bundle b = getIntent().getExtras();
        wikipediaTitle = b.getString(Main_Activity.ArticleTitleBundleKey);
        title.setText(wikipediaTitle);
        new JsonTask().execute("https://de.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles=" + checkWikipediaTitle(wikipediaTitle));
    }

    /*
    * onClick Methode
    * Falls der User mehr lesen will, so öffnet sich der Browser mit dem Wikipedia Artikel zum angeklickten Begriff
    */
    public void onReadMore(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://de.m.wikipedia.org/wiki/" + wikipediaTitle));
        startActivity(browserIntent);
    }

    /* Überprüfungsmethode
    * Manchmal ist der Titel nicht übereinstimmend mit dem Wikipediaartikel
    * dies wird hier überprüft und liefert den richtigen String zurück,
    * um den Request richtig zusenden
    */
    private String checkWikipediaTitle(String title){
        if (title.matches("OLAP")) {
            title = "Online_Analytical_Processing";
        }
        else if(title.matches("OLTP")) {
            title = "Online_Transaction_Processing";
        }
        else if(title.matches("Access Control")) {
            title = "Zugriffskontrolle";
        }
        else if(title.matches("Funktion")) {
            title = "Funktion_(Mathematik)";
        }
        else if(title.matches("Java")){
            title =  "Java_(Programmiersprache)";
        }
        else if(title.matches("Array")){
            title =  "Feld_(Datentyp)";
        }
        else if(title.matches("Grenzwerte")){
           title =  "Grenzwert_(Funktion)";
        }
        else if(title.matches("Exception")){
            title = "Ausnahmebehandlung";
        }
        else if(title.matches("Test-Driven-Development")){
            title = "Testgetriebene_Entwicklung";
        }
        else if(title.matches("Vererbung")){
            title = "Vererbung_(Programmierung)";
        }
        else if(title.matches("Matrix")){
            title = "Matrix_(Mathematik)";
        }
        else if(title.matches("Datenbankmanagementsystem")){
            title = "Datenbank";
        }
        else if(title.matches("Subnetting")){
            title = "Subnetz";
        }
        else if(title.matches("TCP/IP")){
            title = "Transmission_Control_Protocol/Internet_Protocol";
        }
        else if(title.matches("Anforderungen")){
            title = "Anforderung_(Informatik)";
        }
        else if(title.matches("UML")){
            title = "Unified_Modeling_Language";
        }
        else if(title.matches("Einkommenssteuer")){
            title = "Einkommensteuer_(Deutschland)";
        }
        else if(title.matches("Kanban")){
            title = "Kanban_%28Softwareentwicklung%29";
        }
        else if(title.matches("Startups")){
            title = "Start-up-Unternehmen";
        }
        else if(title.matches("BPMN")){
            title = "Business_Process_Model_and_Notation";
        }
        else if(title.matches("TMG")){
            title = "Telemediengesetz";
        }
        else if(title.matches("TKG")){
            title = "Telekommunikationsgesetz_%28Deutschland%29";
        }
        title = title.replace(" ", "_");
        return title;
    }

    /* Überprüfungsmethode
    * Manche Atrikel enthalten komische Zeilenabstände, Umbrüche oder Codes,
    * diese werden hier entfernt
    */
    private String checkWikipediaArticle(String article) {
        article = article.replace("\n", "");
        article = article.replace("     ", "");
        article = article.replace("    ", "");
        article = article.replace("   ", "");
        article = article.replace("  ", "");
        article = article.replace("{\\displaystyle y}", "");
        article = article.replace("{\\displaystyle x}", "");
        article = article.replace("{\\displaystyle f\\colon A\\to B}", " ");
        article = article.replace("{\\displaystyle a\\in A}", " ");
        article = article.replace("{\\displaystyle b\\in B}", " ");
        article = article.replace("{\\displaystyle f(a)}", " ");
        article = article.replace("{\\displaystyle b=f(a)}", " ");
        article = article.replace("{\\displaystyle f}", " ");
        article = article.replace("{\\displaystyle f^{-1}}", " ");
        article = article.replace("{\\displaystyle f^{-1}\\colon B\\to A}", " ");
        return article;
    }


    /* Request an Wikipedia API
    * Bei Android muss eine Anfrage über einen Asynctask erfolgen
    * So wird auch in preExecute eine ProgressBar geladen
    * In doInBackground kommt die eigentliche Abfrage
    * In PostExecute wird der entpackte JSON Content in einem Textview dargestellt und
    * die Progressbar wieder geschlossen
    */
    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(WikipediaItem.this);
            pd.setMessage("Artikel wird geladen...");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.d("Response: ", "> " + line);

                }
                JSONObject jsonObj = new JSONObject(buffer.toString());
                String id = jsonObj.getJSONObject("query").getJSONObject("pages").names().toString();
                id = id.replaceAll("[^0-9]", "");
                return jsonObj.getJSONObject("query").getJSONObject("pages").getJSONObject(id).get("extract").toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            txtJson.setText(checkWikipediaArticle(result));
        }
    }
}
