package com.something.jrgun.elluckphant.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.something.jrgun.elluckphant.R;

/**
 * Created by jrgun on 4/12/2017.
 */

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_mainmenu);


        // to 1 Play
        Button to1PlayFromMain = (Button)findViewById(R.id.to1PlayFromMain);
        to1PlayFromMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity( new Intent(MainMenuActivity.this, OnePlayActivity.class) );
            }
        });


        // to 5 Plays
        Button to5PlaysFromMain = (Button)findViewById(R.id.to5PlaysFromMain);
        to5PlaysFromMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity( new Intent(MainMenuActivity.this, FivePlayActivity.class) );
            }
        });

    }
}
