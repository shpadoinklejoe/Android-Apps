package com.example.jrgun.el_luck_phant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by jrgun on 4/11/2017.
 */

public class OnePlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_1play);

        // to main
        Button toMainFrom1Play = (Button)findViewById(R.id.toMainFrom1Play);
        toMainFrom1Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity( new Intent(OnePlayActivity.this, MainMenuActivity.class) );
            }
        });

        // to 5 Plays
        Button to5PlaysFrom1Play = (Button)findViewById(R.id.to5PlaysFrom1Play);
        to5PlaysFrom1Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity( new Intent(OnePlayActivity.this, FivePlayActivity.class) );
            }
        });

    }

}
