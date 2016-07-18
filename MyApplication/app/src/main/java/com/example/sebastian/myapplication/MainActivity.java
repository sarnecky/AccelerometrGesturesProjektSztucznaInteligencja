package com.example.sebastian.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.sebastian.reco.*;
import com.example.sebastian.reco.pojo.GestureResult;
import com.example.sebastian.reco.recognizer.GestureRecognizer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    LinearLayout background;

    //Gestures
    ToggleButton togglebtnRecognize;
    GestureDetectorCompat Detector;
    static Button learnigbtn;

    //Image for
    ImageView displayImage;

    //Accelerometr
    SensorManager mSensorManager;
    Sensor mSensor;
    Sensor accelerometr;
    SensorManager sm;
    TextView acceleration;

    //functionality to call
    static Camera cam = null;
    WifiManager wifi;
    AudioManager mAudioManager;



    //own class accelerometr
    AccelerometerSensor accelerometerSensor;
    //text
    TextView templateName;

    //flags
    boolean flashlight, silientmode, wifimode, screenshotmode;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button to recognize
        togglebtnRecognize = (ToggleButton) findViewById(R.id.toggleButton);
        //name of template
        learnigbtn = (Button)findViewById(R.id.learnigbtn);
        templateName = (TextView) findViewById(R.id.textView);

        displayImage = (ImageView) findViewById(R.id.displayImage);

                //wifi
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        //silient mode
        mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);

        flashlight=false;
        silientmode=false;
        wifimode=false;
        screenshotmode=false;
        //Acceleromter
       // mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
       // mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        //sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
       // accelerometr = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        //sm.registerListener(this, accelerometr, SensorManager.SENSOR_DELAY_NORMAL);

        accelerometerSensor = new AccelerometerSensor(
                (SensorManager) getSystemService(Context.SENSOR_SERVICE),
                Common.POINTS);

        togglebtnRecognize
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isEnable) {
                        recognize(isEnable);
                    }
                });

        learnigbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.sebastian.myapplication.AdminActivity");
                startActivity(intent);
            }
        });

        if (Common.TEMPLATES.isEmpty()) {
            Common.TEMPLATES = GestureTemplateCreator.readTemplates(this);
            int b=0;
        }
    }

    protected void recognize(boolean isEnable) {
        if (isEnable) {
            Common.POINTS.clear();
            accelerometerSensor.sensorOn();
            //templateName.setText("");
        } else {
            accelerometerSensor.sensorOff();
            GestureRecognizer recognizer = new GestureRecognizer(Common.POINTS);
            GestureResult[] result = recognizer.recognize(Common.TEMPLATES);
            //templateName.setText(result[0].toString());
            String r = result[0].toString().substring(12);

            switch(r)
            {
                case Common.CHECK:
                    if(flashlight==false)
                        flashLightOn();
                    else
                        flashLightOff();
                    displayImage.setImageResource(R.drawable.check250x250);
                    break;
                case Common.XXX:
                    if(silientmode==false)
                    {
                        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        silientmode=true;
                    }
                    else
                    {
                        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        silientmode=false;
                    }
                    displayImage.setImageResource(R.drawable.x250x250);
                    break;
                case Common.TRIANGLE:
                    if(wifimode==false)
                    {
                        wifi.setWifiEnabled(true);
                        wifimode = true;
                    }
                    else
                    {
                        wifi.setWifiEnabled(false);
                        wifimode = false;
                    }
                    displayImage.setImageResource(R.drawable.triangle250x250);
                    break;
                case Common.PIGTAIL:
                    takeScreenshot();
                displayImage.setImageResource(R.drawable.pigtail250x250);
                break;
                default:
                    displayImage.setImageResource(R.drawable.nomatch250x250);
                    break;
            }

        }
    }


    public void flashLightOn() {

        try {
            if (getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA_FLASH)) {
                cam = Camera.open();
                Camera.Parameters p = cam.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                cam.setParameters(p);
                cam.startPreview();
                flashlight=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Exception flashLightOn()",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void flashLightOff() {
        try {
            if (getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA_FLASH)) {
                cam.stopPreview();
                cam.release();
                cam = null;
                flashlight=false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Exception flashLightOff",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }
    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }



}
