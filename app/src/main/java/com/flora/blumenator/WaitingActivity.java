package com.flora.blumenator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.provider.Settings.Secure;


public class WaitingActivity extends AppCompatActivity {

    protected String android_id;
    String imageStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        android_id = Secure.getString(getContentResolver(), Secure.ANDROID_ID);

        Intent intent = getIntent();
        imageStr = intent.getStringExtra("bitmapImage");
        sendServerRequest(imageStr);
    }

    void sendServerRequest(String image_str) {
        Bitmap image = BitmapFactory.decodeFile(image_str);

        // Send server request here
//        HttpClient httpclient = new DefaultHttpClient();

    }

    private class ServerRequestTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String ... urls) {
            String response = "";

            for(String urlString : urls) {


                try {
                    URL url = new URL(urlString);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    try {
                        urlConnection.setDoOutput(true);
                        urlConnection.setDoInput(true);
                        urlConnection.setChunkedStreamingMode(0);
                        urlConnection.setRequestMethod("POST");

                        Format formatter = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
                        String stamp = formatter.format(new Date());
                        String deviceId = android_id + stamp;

                        urlConnection.setRequestProperty("id", deviceId);
                        urlConnection.setRequestProperty("jobId", "placeholder");

                        Bitmap image = BitmapFactory.decodeFile(imageStr);
                        urlConnection.setRequestProperty("image", "placeholder");



                        OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
//                        writeStream(out);

                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//                        readStream(in);
                    }
                    finally {
                        urlConnection.disconnect();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }



                return response;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            proceedWithResult(result);
        }
    }


    void proceedWithResult(String flower_name) {
        Intent intent = new Intent(WaitingActivity.this, WaitingActivity.class);
        intent.putExtra("data",flower_name);
        startActivity(intent);
    }
}
