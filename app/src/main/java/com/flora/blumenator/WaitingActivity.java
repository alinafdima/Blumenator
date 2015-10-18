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
import java.io.FileInputStream;
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

import cz.msebera.android.httpclient.Header;


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
        final String jobid = android_id + stamp;

        RequestParams params = new RequestParams();
        try {
            InputStream myInputStream = new FileInputStream(image_str);
            params.put("mediafile", myInputStream);
            params.put("jobid", jobid);


            ServerClass.post("flowerrecognition", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("flowerrecognition", "Failure");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Log.d("flowerrecognition", "Success");
                    checkResponse(jobid);
                }
            });

        } catch (FileNotFoundException e) {
            Log.d("imageName", "File not found");
            e.printStackTrace();
        }

    }


    void checkResponse(String jobid) {
        final String jobidAgain = jobid;
        RequestParams params = new RequestParams();
        params.put("jobid", jobid);
        Log.d("checkfinished", "Before");
        ServerClass.post("checkfinished", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("checkfinished", "Error");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("checkfinished", "success = " + responseString);
                Log.d("Response", responseString);
                if (!responseString.equals("0")) {
                    Log.d("checkfinished", "nonzero");
                    proceedWithResult(responseString);
                } else {
                    Log.d("checkfinished", "response=0");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("checkfinished", "recall");
                    checkResponse(jobidAgain);
                }

            }
        });
    }



    void proceedWithResult(String flower_name) {
        Intent intent = new Intent(WaitingActivity.this, ResultActivity.class);
        intent.putExtra("data", flower_name);
        startActivity(intent);
    }
}
