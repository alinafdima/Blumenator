package com.flora.blumenator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WaitingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        Intent intent = getIntent();
        String imageStr = intent.getStringExtra("bitmapImage");
        sendServerRequest(imageStr);
    }

    void sendServerRequest(String image_str) {
        Bitmap image = BitmapFactory.decodeFile(image_str);

        // Send server request here

    }


    void proceedWithResult(String flower_name) {
        Intent intent = new Intent(WaitingActivity.this, WaitingActivity.class);
        intent.putExtra("data",flower_name);
        startActivity(intent);
    }
}
