package com.flora.blumenator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LinearGradient;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class PhotoConfirmActivity extends AppCompatActivity {

    protected String imageStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Arrived at ", "Photo confirm activity");
        setContentView(R.layout.activity_photo_confirm);

        Intent intent = getIntent();
        imageStr = intent.getStringExtra("bitmapImage");
        Log.d("Checkpoint",imageStr);
        Bitmap bitmap = BitmapFactory.decodeFile(imageStr);
        final ImageView imgView = (ImageView) findViewById(R.id.img_conf_image);
        imgView.setScaleType(ImageView.ScaleType.FIT_XY);
        imgView.setImageBitmap(bitmap);

        final Button buttonConfirm = (Button) findViewById(R.id.btn_conf_confirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Confirmed selected picture
                Log.d("Photo ", "confirmed");

                Intent intent = new Intent(PhotoConfirmActivity.this, WaitingActivity.class);
                intent.putExtra("bitmapImage", imageStr);
                startActivity(intent);
            }
        });
    }
}
