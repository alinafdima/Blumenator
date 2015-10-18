package com.flora.blumenator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
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
import android.util.Log;

import com.loopj.android.http.*;

import org.json.*;


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
        Format formatter = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String stamp = formatter.format(new Date());
        String jobid = android_id + stamp;

        File myFile = new File(image_str);
        RequestParams params = new RequestParams();
        try {
            params.put("mediafile", myFile);
        } catch(FileNotFoundException e) {
            Log.d("L1", e.toString());}
        params.put("jobid", jobid);

        ServerClass.get("flowerrecognition", params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, PreferenceActivity.Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                System.out.println("Delivery ok");
            }
        });


    }




    void proceedWithResult(String flower_name) {
        Intent intent = new Intent(WaitingActivity.this, WaitingActivity.class);
        intent.putExtra("data",flower_name);
        startActivity(intent);
    }
}
