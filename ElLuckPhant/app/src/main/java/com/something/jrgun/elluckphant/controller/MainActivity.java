package com.something.jrgun.elluckphant.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.something.jrgun.elluckphant.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_start);

        // Touch anywhere to continue
        LinearLayout startScreen = (LinearLayout)findViewById(R.id.startScreen);
        startScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity( new Intent(MainActivity.this, MainMenuActivity.class) );
            }
        });



    }
}
