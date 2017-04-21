package com.example.jrgun.elluckphant.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.jrgun.elluckphant.R;

/**
 * Created by jrgun on 4/11/2017.
 */

public class FivePlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_5plays);

        // to main
        Button toMainFrom5Plays = (Button)findViewById(R.id.toMainFrom5Plays);
        toMainFrom5Plays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity( new Intent(FivePlayActivity.this, MainMenuActivity.class) );
            }
        });

        // to 1 Play
        Button to1PlayFrom5Plays = (Button)findViewById(R.id.to1PlayFrom5Plays);
        to1PlayFrom5Plays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity( new Intent(FivePlayActivity.this, OnePlayActivity.class) );
            }
        });


    }
}
