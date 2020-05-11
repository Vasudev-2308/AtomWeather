package com.example.atomweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText city;
    String cityName;
    Button forecastButton;
    TextView foreCast;
    public void forecast(View view){
        cityName=city.getText().toString();
        DownloadTask downloadTask=new DownloadTask();
        downloadTask.execute("https://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid=c905647ea071c57f0632a2b923d9178a");
        InputMethodManager methodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        methodManager.hideSoftInputFromWindow(city.getWindowToken(),0);


}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city=findViewById(R.id.yourCityEditText);
        forecastButton=findViewById(R.id.forecastWeatherButton);
        foreCast=findViewById(R.id.forecastTextView);

    }
    public class DownloadTask extends AsyncTask<String,Void,String>{
        @Override
        protected void onPostExecute(String result) {
            String weatherInfo;
            super.onPostExecute(result);
            try {
                JSONObject jsonObject=new JSONObject(result);
                weatherInfo= jsonObject.getString("weather");
                JSONArray arr=new JSONArray(weatherInfo);
                for(int i=0;i<arr.length();i++){
                    JSONObject jsonPart=arr.getJSONObject(i);
                    String mainJson=jsonPart.getString("main");
                    String desc=jsonPart.getString("description");
                    String message="";
                    if(mainJson!=""&&desc!="")
                     message=mainJson+":" +desc;
                    else
                        foreCast.setText("");

                    Log.d("res",message);
                    if(message!="") {
                        foreCast.setTypeface(Typeface.DEFAULT_BOLD);
                        foreCast.setText(message);

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            try {
                URL url=new URL(urls[0]);
                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in =urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1){
                    char current =(char) data;
                    result+=current;
                    data=reader.read();
                }
            } catch (MalformedURLException e) {
                Toast.makeText(MainActivity.this, "City Not Found", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return  result;
        }
    }
}
