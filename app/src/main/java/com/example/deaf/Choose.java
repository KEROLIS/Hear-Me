package com.example.deaf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class Choose extends AppCompatActivity {
    Button button;
    ImageView egy, kw, uae;
    TextView eg, k, u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        button = findViewById(R.id.next);
        button.setEnabled(false);
        egy = findViewById(R.id.egyPhoto);
        kw = findViewById(R.id.kwaitPhoto);
        uae = findViewById(R.id.uaePhoto);
        eg = findViewById(R.id.egyTxt);
        k = findViewById(R.id.kwaitTxt);
        u = findViewById(R.id.uaeTxt);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(Choose.this, RealWork.class);
                Choose.this.startActivity(mainIntent);
                Choose.this.finish();
            }
        });


    }

    public void OnClick(View v) {
        if (v.getId() == R.id.egyPhoto) {
            eg.setTextColor(getResources().getColor(R.color.black));
            k.setTextColor(getResources().getColor(R.color.white));
            u.setTextColor(getResources().getColor(R.color.white));
            button.setEnabled(true);
            button.setAlpha(1);

        } else if (v.getId() == R.id.kwaitPhoto) {
            k.setTextColor(getResources().getColor(R.color.black));
            eg.setTextColor(getResources().getColor(R.color.white));
            u.setTextColor(getResources().getColor(R.color.white));
            button.setEnabled(true);
            button.setAlpha(1);
        } else {
            u.setTextColor(getResources().getColor(R.color.black));
            eg.setTextColor(getResources().getColor(R.color.white));
            k.setTextColor(getResources().getColor(R.color.white));
            button.setEnabled(true);
            button.setAlpha(1);
        }
    }
}
