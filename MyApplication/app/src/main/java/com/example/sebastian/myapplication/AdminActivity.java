package com.example.sebastian.myapplication;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.sebastian.reco.Common;

public class AdminActivity extends AppCompatActivity {
    Button recognitionbtn;
    ImageButton checkBtn, xBtn, traingleBtn, pigtailBtn;
    Spinner templateSpinner;
    AccelerometerSensor accelerometerSensor;
    ToggleButton newTemplate;
    String currentTemplateName;
    TextView newtemplateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //go to recognition mode
        recognitionbtn = (Button)findViewById(R.id.recognizebtn);
        checkBtn = (ImageButton)findViewById(R.id.checkBtn);
        xBtn= (ImageButton)findViewById(R.id.xBtn);
        traingleBtn = (ImageButton)findViewById(R.id.triangleBtn);
        pigtailBtn = (ImageButton)findViewById(R.id.pigtailBtn);

        //text wiew
        newtemplateText = (TextView) findViewById(R.id.newtemplateText);

        //adding new template
        newTemplate = (ToggleButton) findViewById(R.id.addNewTemplate);

        //templateSpinner = (Spinner)findViewById(R.id.spinner);
        accelerometerSensor = new AccelerometerSensor(
                (SensorManager) getSystemService(Context.SENSOR_SERVICE),
                Common.POINTS);

        //add items to spinner
//        String [] names = new String[]{Common.CHECK, Common.XXX, Common.TRIANGLE, Common.PIGTAIL};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, names);
//        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        templateSpinner.setAdapter(adapter);
//
//
//        //templateSpinner
                recognitionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        //check Button
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTemplateName =Common.CHECK;
                newtemplateText.setText("Check");
            }
        });

        //x Button
        xBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTemplateName =Common.XXX;
                newtemplateText.setText("X");
            }
        });


        //triangle Button
        traingleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTemplateName = Common.TRIANGLE;
                newtemplateText.setText("Triangle");
            }
        });

        //pigtail button
        pigtailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTemplateName = Common.PIGTAIL;
                newtemplateText.setText("Pigtail");
            }
        });


//        templateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parentView,
//                                       View selectedItemView, int position, long id) {
//                currentTemplateName = parentView.getItemAtPosition(position)
//                        .toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//
//            }
//        });
        newTemplate
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        processTemplate(isChecked);
                    }
               });

    }
    protected void processTemplate(boolean isChecked) {
        if (isChecked) {
            accelerometerSensor.sensorOn();
            Common.POINTS.clear();
        } else {
            accelerometerSensor.sensorOff();
            Common.TEMPLATES = GestureTemplateCreator.addTemplate(
                    Common.POINTS, currentTemplateName, this);
        }
    }
}
