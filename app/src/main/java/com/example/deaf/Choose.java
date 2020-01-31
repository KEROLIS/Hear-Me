package com.example.deaf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class Choose extends AppCompatActivity {
    Button button;
    TextView eg, k, u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        button = findViewById(R.id.next);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVideoFromGallary();


            }
        });


    }

    public void chooseVideoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("result", "" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            Log.d("what", "cancle");
            return;
        }
        Log.d("what", "gale");
        if (data != null) {
            Uri contentURI = data.getData();

            String selectedVideoPath = getPath(contentURI);
            Log.d("path", selectedVideoPath);
//            saveVideoToInternalStorage(selectedVideoPath);
            Toast.makeText(this, "جارى المعالجه", Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent(Choose.this, RealWork.class);
            UriPass.contentURI=contentURI;
            Choose.this.startActivity(mainIntent);
            Choose.this.finish();
        }
    }

    public String getPath(Uri uri) {
            String[] projection = {MediaStore.Video.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
                // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } else
                return null;
        }
    }

