package com.ycalm.amandeep.weather;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    TextView place, minTemp, maxTemp, description;
    ArrayList<String> tempArrayList = new ArrayList<>();
    ArrayList<String> minTempArrayList = new ArrayList<>();
    ArrayList<String> maxTempArrayList = new ArrayList<>();
    ArrayList<String> descriptionArrayList = new ArrayList<>();
    ArrayList<String> dateArrayList = new ArrayList<>();
    ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    String placeString;
    String urlString = "";
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    SharedPreferences sharedPreferences;
    //Location
    LocationManager locationManager;
    String provider;
    Location location;
    Double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("com.ycalm.amandeep.weatherhotline", MODE_PRIVATE);
        place = findViewById(R.id.place);
        minTemp = findViewById(R.id.minTemp);
        maxTemp = findViewById(R.id.maxTemp);
        description = findViewById(R.id.description);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdaptor(getApplicationContext(), descriptionArrayList, minTempArrayList, maxTempArrayList, dateArrayList, bitmapArrayList);
        recyclerView.setAdapter(adapter);
        urlString = "http://samples.openweathermap.org/data/2.5/forecast?lat=35&lon=139&appid=b6907d289e10d714a6e88b30761fae22";
        DownloadTask task = new DownloadTask();
        task.execute(urlString);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    public class DownloadTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            try{
                URL url = new URL(urls[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStreamReader reader = new InputStreamReader(httpURLConnection.getInputStream());
                int data = reader.read();
                while(data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                parseDate(result);
                return result;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            place.setText(placeString);
            setTitle(placeString);
            minTemp.setText(minTempArrayList.get(0));
            maxTemp.setText(maxTempArrayList.get(0));
            description.setText(descriptionArrayList.get(0));
            adapter.notifyDataSetChanged();
        }
    }

    public void parseDate(String result){
        if(result != ""){
            try {
                JSONObject jsonObject = new JSONObject(result);
                String jsonString = jsonObject.getString("list");
                JSONArray jsonArray = new JSONArray(jsonString);
                jsonString = jsonObject.getString("city");
                jsonObject = new JSONObject(jsonString);
                placeString = jsonObject.getString("name");
                Bitmap bitmap;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    jsonString = jsonObject.getString("main");
                    jsonObject = new JSONObject(jsonString);
                    jsonString = jsonObject.getString("temp");
                    tempArrayList.add(jsonString);
                    jsonString = jsonObject.getString("temp_min");
                    minTempArrayList.add(jsonString);
                    jsonString = jsonObject.getString("temp_max");
                    maxTempArrayList.add(jsonString);
                    jsonObject = jsonArray.getJSONObject(i);
                    jsonString = jsonObject.getString("weather");
                    JSONArray jsonArray2 = new JSONArray(jsonString);
                    jsonObject = jsonArray2.getJSONObject(0);
                    jsonString = jsonObject.getString("main");
                    descriptionArrayList.add(jsonString);
                    jsonString = jsonObject.getString("icon");
                    //iconArrayList.add(jsonString);
                    URL url = new URL("http://openweathermap.org/img/w/"+jsonString+".png");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.connect();
                    InputStream in = httpURLConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(in);
                    bitmapArrayList.add(bitmap);
                    jsonObject = jsonArray.getJSONObject(i);
                    jsonString = jsonObject.getString("dt_txt");
                    dateArrayList.add(jsonString);
                }
            }catch (Exception e){
                e.printStackTrace();
            }// Log.i("Lengths",Integer.toString(tempArrayList.size())+Integer.toString(minTempArrayList.size())+Integer.toString(maxTempArrayList.size())+Integer.toString(descriptionArrayList.size())+Integer.toString(dateArrayList.size()));
        }
    }


}
