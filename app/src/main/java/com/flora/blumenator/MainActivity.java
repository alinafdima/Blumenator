package com.flora.blumenator;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button buttonCamera = (Button) findViewById(R.id.btn_main_camera);
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Pressed camera button
                Log.d("Camera button ", "clicked");

            }
        });

        final ImageButton buttonUpload = (ImageButton) findViewById(R.id.btn_main_upload);
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
        // Get the cursor
        Cursor cursor = getContentResolver().query(uri,
                filePathColumn, null, null, null);
        // Move to first row
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
            if (requestCode == PICK_IMAGE && resultCode == RESULT_OK  && null != data) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String imgDecodableString = getPath(selectedImage);

                // Set the Image in ImageView after decoding the String
                final ImageButton buttonUpload = (ImageButton) findViewById(R.id.btn_main_upload);
                buttonUpload.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));

            } else {
                Toast.makeText(this, "You haven't picked an image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

}
