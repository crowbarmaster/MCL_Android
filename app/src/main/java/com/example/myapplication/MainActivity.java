package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    public static File CorePath;
    public Context CoreContext;
    private static final int Image_Capture_Code = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CorePath = new File(getExternalFilesDir(null).getAbsolutePath());
        CoreContext = getApplicationContext();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        DataManager dataManager = new DataManager();
        dataManager.LoadAllData();
        new Timer().scheduleAtFixedRate(new Update(), 1000, 1000);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imgCapture = (ImageView) findViewById(R.id.capturedImage);
        if (requestCode == Image_Capture_Code) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                imgCapture.setImageBitmap(bp);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class Update extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String txt = "You are not logged in. Please log in to continue.";
                    TextView info = findViewById(R.id.info_box);
                    if (DataManager.user.FirstName != null) {
                        txt = "Welcome, " + DataManager.user.FirstName + " " + DataManager.user.LastName + "! You are logged in.";
                    }
                    if (info != null) {
                        info.setText(txt);
                    }
                }
            });
        }
    }
}