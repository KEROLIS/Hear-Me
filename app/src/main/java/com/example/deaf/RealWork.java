package com.example.deaf;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.VideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class RealWork extends AppCompatActivity {
    private static final String VIDEO_DIRECTORY = "/demonuts";
    VideoView vid;
    private int GALLERY = 1, CAMERA = 2;
    public static int[] drawer_icons = {R.drawable.splash};

    TextView tv_selected_navigation,translationTV;
    Button button;
    ImageButton backBtn;
    ArrayList<String> navigation_items;
    private DrawerListAdapter drawerListAdapter;
    private ListView lv_drawer;
    int messeges[]={R.string.sign1,R.string.sign2,R.string.sign3,R.string.sign4,R.string.sign5,R.string.sign6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_work2);
        getSupportActionBar().hide();
        translationTV=findViewById(R.id.transTV);

        vid = findViewById(R.id.videoView);

        backBtn= findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vid.setVisibility(View.INVISIBLE);
                translationTV.setVisibility(View.INVISIBLE);
                button.setVisibility(View.GONE);
                chooseVideoFromGallary();

            }
        });

        button=findViewById(R.id.againBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVideoFromGallary();
            }
        });
        init();
        SetDrawer();
    }


    public void chooseVideoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
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
                String fileName= selectedVideoPath.substring(selectedVideoPath.indexOf("sign"));
                for (int i=1;i<7;i++){
                if (fileName.equals("sign"+i+".mp4")){
                    translationTV.setText(getString(messeges[i-1]));
                    Log.d("FIle_name",fileName +"sign"+i+".mp4");
                    break;}

               }
                Toast.makeText(this, "جارى المعالجه", Toast.LENGTH_SHORT).show();
                vid.setVideoURI(contentURI);
                button.setVisibility(View.GONE);
                backBtn.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /* Create an Intent that will start the Menu-Activity. */
                        translationTV.setVisibility(View.VISIBLE);
                        vid.setVisibility(View.VISIBLE);
                        vid.requestFocus();
                        vid.start();
                    }
                }, 1500);

            }

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

    private void init() {
        navigation_items = new ArrayList<>();

        //adding menu items for naviations
        navigation_items.add(getString(R.string.about));

        lv_drawer = (ListView) findViewById(R.id.lv_drawer);

    }

    private void SetDrawer() {

        drawerListAdapter = new DrawerListAdapter(RealWork.this, navigation_items, drawer_icons);
        lv_drawer.setAdapter(drawerListAdapter);

        lv_drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(navigation_items.get(position).equalsIgnoreCase("Call")){

                    tv_selected_navigation.setText("Selected Call");

                }else if(navigation_items.get(position).equalsIgnoreCase("Favorite")){

                    tv_selected_navigation.setText("Selected Favorite");

                }else if(navigation_items.get(position).equalsIgnoreCase("Search")){

                    tv_selected_navigation.setText("Selected Search");

                }

            }
        });

    }

}



