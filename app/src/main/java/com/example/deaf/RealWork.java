package com.example.deaf;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class RealWork extends AppCompatActivity {
    private static final String VIDEO_DIRECTORY = "/demonuts";
    Button button;
    VideoView vid;
    private int GALLERY = 1, CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_work2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        button = findViewById(R.id.pro);
        vid = findViewById(R.id.videoView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();

            }
        });
    }

    public void playVideo() {
        MediaController m = new MediaController(this);
        vid.setMediaController(m);

        String path = "android.resource://com.example.deaf/" + R.raw.trail;

        Uri u = Uri.parse(path);

        vid.setVideoURI(u);

        vid.start();

    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select video from gallery"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chooseVideoFromGallary();
                    }
                });
        pictureDialog.show();
    }

    public void chooseVideoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takeVideoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("result", "" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            Log.d("what", "cancle");
            return;
        }
        if (requestCode == GALLERY) {
            Log.d("what", "gale");
            if (data != null) {
                Uri contentURI = data.getData();

                String selectedVideoPath = getPath(contentURI);
                Log.d("path", selectedVideoPath);
                saveVideoToInternalStorage(selectedVideoPath);
                Toast.makeText(this, "جارى المعالجه", Toast.LENGTH_SHORT).show();
                vid.setVideoURI(contentURI);
                vid.requestFocus();
                vid.start();

            }

        } else if (requestCode == CAMERA) {
            Uri contentURI = data.getData();
            String recordedVideoPath = getPath(contentURI);
            Log.d("frrr", recordedVideoPath);
            saveVideoToInternalStorage(recordedVideoPath);
            vid.setVideoURI(contentURI);
            vid.requestFocus();
            vid.start();
        }
    }

    private void saveVideoToInternalStorage(String filePath) {

        File newfile;

        try {

            File currentFile = new File(filePath);
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + VIDEO_DIRECTORY);
            newfile = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".mp4");

            if (!wallpaperDirectory.exists()) {
                wallpaperDirectory.mkdirs();
            }

            if (currentFile.exists()) {

                InputStream in = new FileInputStream(currentFile);
                OutputStream out = new FileOutputStream(newfile);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                Log.v("vii", "Video file saved successfully.");
            } else {
                Log.v("vii", "Video saving failed. Source file missing.");
            }
        } catch (Exception e) {
            e.printStackTrace();
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



