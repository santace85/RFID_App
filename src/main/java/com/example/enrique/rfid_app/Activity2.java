package com.example.enrique.rfid_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.*;

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Button buttonList = (Button) findViewById(R.id.button4);
        buttonList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });

        Button buttonMap = (Button) findViewById(R.id.button5);
        buttonMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToMapsActivity();
            }
        });

    }

    private void goToMainActivity() {

        //main activity is actually activity2
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private void goToMapsActivity() {

        //main activity is actually activity2
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}

