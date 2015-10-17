package com.flora.blumenator;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static int PICK_IMAGE = 1;
    private static int TAKE_PHOTO = 2;
    File destination;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String name =   dateToString(new Date(),"yyyy-MM-dd-hh-mm-ss");
        Format formatter = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String name = formatter.format(new Date());
        destination = new File(Environment.getExternalStorageDirectory(), name + ".jpg");
        final Button buttonCamera = (Button) findViewById(R.id.btn_main_camera);
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Pressed camera button
                Log.d("Camera button ", "clicked");

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });

        final Button buttonUpload = (Button) findViewById(R.id.btn_main_upload);
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Pressed upload button
                Log.d("Upload button ", "clicked");

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });


    }


    public String getPath(Uri uri)
    {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String path = cursor.getString(columnIndex);
        cursor.close();

        return path;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE) {
                if( resultCode == RESULT_OK && null != data) {

                    // Get the Image from data
                    Uri selectedImage = data.getData();
                    String image = getPath(selectedImage);
                    proceedWithImageConfirmation(image);
                }
                else {
                    Toast.makeText(this, "You haven't picked an image", Toast.LENGTH_LONG).show();
                }
            } else {
                if (requestCode == TAKE_PHOTO) {
                    if(resultCode == RESULT_OK) {
                        imagePath = destination.getAbsolutePath();
                        proceedWithServerRequest(imagePath);
                    }
                    else {
                        Toast.makeText(this, "You haven't taken a photo", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Launches activity PhotoConfirmActivity
     * @param image
     */
    void proceedWithImageConfirmation(String image){
        Intent intent = new Intent(MainActivity.this, PhotoConfirmActivity.class);
        intent.putExtra("bitmapImage",image);
        startActivity(intent);
    }

    /**
     * Launches activity WaitingActivity
     * @param image
     */
    void proceedWithServerRequest(String image) {
        Intent intent = new Intent(MainActivity.this, WaitingActivity.class);
        intent.putExtra("bitmapImage",image);
        startActivity(intent);
    }

}
