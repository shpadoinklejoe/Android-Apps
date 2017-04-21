package com.example.jrgun.elluckphant.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jrgun.elluckphant.R;
import com.example.jrgun.elluckphant.model.GenerateNumbers;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by jrgun on 4/11/2017.
 */

public class OnePlayActivity extends AppCompatActivity {

    @BindView(R.id.ball1)
    TextView ball1;
    ArrayList<Integer> lottoNumbers;

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

        // generates 5 numbers
        Button generate1 = (Button) findViewById(R.id.generate1);
        generate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                lottoNumbers = new GenerateNumbers().getNums();
                System.out.println(lottoNumbers);
                System.out.println(Integer.toString(lottoNumbers.get(0)));

                if(ball1 == null)
                {
                    System.out.println("FFFFFFFFFFFFFFFFFFFFFFF");
                    ball1 = (TextView) findViewById(R.id.ball1);
                }
                ball1.setText(Integer.toString(lottoNumbers.get(0)));
            }
        });

    }

}
