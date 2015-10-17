package com.flora.blumenator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        String flower = intent.getStringExtra("data");

        final TextView tv = (TextView) findViewById(R.id.txt_result_name);
        tv.setText(flower);
    }
}
