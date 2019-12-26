package com.example.marsphotos;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class provides capabilities to search for an image on NASA Mars Rover Photos API given a search date term.
 * The method "FetchPhotos" is the entry to the class.
 * Network operations cannot be done from the UI thread, therefore this class makes use of an AsyncTask inner class
 * that will do the network operations in a separate worker thread.
 * However, any UI updates should be done in the UI thread so avoid any synchronization problems.
 * onPostExecution runs in the UI thread, and it calls the ImageView pictureReady method to do the update
 */

public class MainActivity extends AppCompatActivity {
    /*
    Querying by Earth date:
    Dates should be formatted as 'yyyy-mm-dd'. The earliest date available is the date of landing for each rover.
    https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?earth_date=2015-6-3
    response:
    {"photos":[{"id":102685,"sol":1004,"camera":{"id":20,"name":"FHAZ","rover_id":5,"full_name":"Front Hazard Avoidance Camera"},"photo":"http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01004/opgs/edr/fcam/FLB_486615455EDR_F0481570FHAZ00323M_.JPG","earth_date":"2015-06-03","rover":{"id":5,"name":"Curiosity","landing_date":"2012-08-06","launch_date":"2011-11-26","status":"active","max_sol":2540,"max_date":"2019-09-28","total_photos":366206,"cameras":[{"name":"FHAZ","full_name":"Front Hazard Avoidance Camera"},{"name":"NAVCAM","full_name":"Navigation Camera"},{"name":"MAST","full_name":"Mast Camera"},{"name":"CHEMCAM","full_name":"Chemistry and Camera Complex"},{"name":"MAHLI","full_name":"Mars Hand Lens Imager"},{"name":"MARDI","full_name":"Mars Descent Imager"},{"name":"RHAZ","full_name":"Rear Hazard Avoidance Camera"}]}},{"id":102686,"sol":1004,"camera":{"id":20,"name":"FHAZ","rover_id":5,"full_name":"Front Hazard Avoidance Camera"},"photo":"http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01004/opgs/edr/fcam/FRB_486615455EDR_F0481570FHAZ00323M_.JPG","earth_date":"2015-06-03","rover":{"id":5,"name":"Curiosity","landing_date":"2012-08-06","launch_date":"2011-11-26","status":"active","max_sol":2540,"max_date":"2019-09-28","total_photos":366206,"cameras":[{"name":"FHAZ","full_name":"Front Hazard Avoidance Camera"},{"name":"NAVCAM","full_name":"Navigation Camera"},{"name":"MAST","full_name":"Mast Camera"},{"name":"CHEMCAM","full_name":"Chemistry and Camera Complex"},{"name":"MAHLI","full_name":"Mars Hand Lens Imager"},{"name":"MARDI","full_name":"Mars Descent Imager"},{"name":"RHAZ","full_name":"Rear Hazard Avoidance Camera"}]}},{"id":102842,"sol":1004,"camera":{"id":21,"name":"RHAZ","rover_id":5,"full_name":"Rear Hazard Avoidance Camera"},"photo":"http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01004/opgs/edr/rcam/RLB_486615482EDR_F0481570RHAZ00323M_.JPG","earth_date":"2015-06-03","rover":{"id":5,"name":"Curiosity","landing_date":"2012-08-06","launch_date":"2011-11-26","status":"active","max_sol":2540,"max_date":"2019-09-28","total_photos":366206,"cameras":[{"name":"FHAZ","full_name":"Front Hazard Avoidance Camera"},{"name":"NAVCAM","full_name":"Navigation Camera"},{"name":"MAST","full_name":"Mast Camera"},{"name":"CHEMCAM","full_name":"Chemistry and Camera Complex"},{"name":"MAHLI","full_name":"Mars Hand Lens Imager"},{"name":"MARDI","full_name":"Mars Descent Imager"},{"name":"RHAZ","full_name":"Rear Hazard Avoidance Camera"}]}},{"id":102843,"sol":1004,"camera":{"id":21,"name":"RHAZ","rover_id":5,"full_name":"Rear Hazard Avoidance Camera"},"photo":"http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01004/opgs/edr/rcam/RRB_486615482EDR_F0481570RHAZ00323M_.JPG","earth_date":"2015-06-03","rover":{"id":5,"name":"Curiosity","landing_date":"2012-08-06","launch_date":"2011-11-26","status":"active","max_sol":2540,"max_date":"2019-09-28","total_photos":366206,"cameras":[{"name":"FHAZ","full_name":"Front Hazard Avoidance Camera"},{"name":"NAVCAM","full_name":"Navigation Camera"},{"name":"MAST","full_name":"Mast Camera"},{"name":"CHEMCAM","full_name":"Chemistry and Camera Complex"},{"name":"MAHLI","full_name":"Mars Hand Lens Imager"},{"name":"MARDI","full_name":"Mars Descent Imager"},{"name":"RHAZ","full_name":"Rear Hazard Avoidance Camera"}]}}]}
     */

    private String HerokuUrl = "http://arcane-lowlands-23189.herokuapp.com/Servlet";
    //Local AVD IP address(10.0.2.2)
    // private String HerokuUrl = "http://10.0.2.2:8080/MongoDBServer_war/Servlet";
    private String api_key = "3N6v50PCrmRuCUSjjIv2McaxGx8pklWMLSiZJWgU";
    private TextView textInfo;
    private EditText editDate;
    private Button photoFetch;
    private ImageView imageView;
    private ListView listView;
    private JSONArray array_photos = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageView = this.findViewById(R.id.imageView);
        listView = this.findViewById(R.id.listView);
        textInfo = this.findViewById(R.id.textInfo);
        editDate = this.findViewById(R.id.editDate);
        editDate.setText("2015-6-3");

        photoFetch = this.findViewById(R.id.photoFetch);

        // Add a listener to the send button
        photoFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Done asynchronously in another thread.
                FetchPhotos();
            }
        });

        FetchPhotos();
    }

    /**
     * FetchPhotos is the public FetchPhotosFromServer method.
     * Its arguments are the search term. This provides a callback
     * path such that the doInBackground method is called
     * when the picture is available from the search.
     */
    void FetchPhotos() {
        // Querying by Earth date:
        FetchPhotosFromServer task = new FetchPhotosFromServer(
                this.getApplicationContext(),
                String.format("%s?api_key=%s&earth_date=%s", HerokuUrl, api_key, editDate.getText().toString()));
        task.execute((Void) null);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * AsyncTask provides a simple way to use a thread separate from
     * the UI thread in which to do network operations.
     * doInBackground is run in the helper thread.
     * onPostExecute is run in the UI thread, allowing for safe UI updates.
     */
    public class FetchPhotosFromServer extends AsyncTask<Void, Void, Boolean> {
        Context context;
        String url, err;
        String responseBody;

        FetchPhotosFromServer(Context context, String url) {
            this.context = context;
            this.url = url;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                responseBody = FetchPhotos(url);
                String json = new String(responseBody.getBytes(), "UTF-8");
                org.json.JSONObject jsonObject = new JSONObject(json);
                array_photos = jsonObject.getJSONArray("photos");

                // success to fetch some photos
                return true;
            } catch (Exception e) {
                err = e.getMessage();
                e.printStackTrace();
            }

            // cannot find a photo
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                try {
                    if (array_photos != null && array_photos.length() > 0) {

                        //fetch first photo only
                        org.json.JSONObject photo = array_photos.getJSONObject(0);
                        if (photo != null) {
                            textInfo.setText("Selected Photo:\nId: "
                                    + photo.getString("id")
                                    + " \nearth_date: "
                                    + photo.getString("earth_date")
                            );

                            //show the photo
                            // http://bumptech.github.io/glide/ref/samples.html
                            Glide.with(context)
                                    .load(photo.getString("img_src"))
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(imageView);
                        }
                    } else {
                        textInfo.setText("No Photos found on this day!");
                        imageView.setImageDrawable(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                textInfo.setText(err);
            }
        }

        @Override
        protected void onCancelled() {
        }
    }

    /**
     * Given a URL sending request to API, return response as JSON formatted String
     * https://www.programcreek.com/java-api-examples/?class=java.net.URLConnection&method=connect
     * @param url the request url
     * @return response string
     */
    String FetchPhotos(String url) {
        try {
            URL urlObj = new URL(url);
            HttpURLConnection urlCon = (HttpURLConnection) urlObj.openConnection();
            urlCon.setRequestMethod("GET");
            urlCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0");
            urlCon.setRequestProperty("Accept", "text/html");
            urlCon.setRequestProperty("Accept-Language", "en-US");
            urlCon.setRequestProperty("Connection", "close");

            int responseCode = urlCon.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
                StringBuffer response = new StringBuffer();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //empty JSON string for photos
        return "{\"photos\":[]}";
    }
}
