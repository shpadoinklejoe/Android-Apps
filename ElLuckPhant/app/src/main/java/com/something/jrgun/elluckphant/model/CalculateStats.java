package com.something.jrgun.elluckphant.model;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.something.jrgun.elluckphant.HoldStats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

/**
 * This class is so numbers can be pulled from database only once
 *
 * Grabs ball info from database
 * Fills the singleton 'global variable'
 */

public class CalculateStats
{
    private static HashMap<Integer, Integer> pick5stats; // keeps track of how many times
    private static HashMap<Integer, Integer> megaBstats; // each ball has been picked
    private double numOfLottos;

    // default constructor
    public CalculateStats()
    {
        fill5stats();
        fillMegaStats(); // will finish first since less complicated code
    }


    // fill stats for pick 5 from file
    private void fill5stats()
    {

        FirebaseDatabase.getInstance().getReference("winning5").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                pick5stats = new HashMap<>();

                for (int i = 1; i <= 75; ++i) {
                    pick5stats.put(i, 0); // initialize with zeros
                }

                double checkNumOfLottos = 0;
                String[] split = dataSnapshot.getValue(String.class).split("\\s+");

                for(String s : split)
                {
                    int num = Integer.parseInt(s);
                    pick5stats.put(num, pick5stats.get(num) + 1); // increment stat count;

                    ++checkNumOfLottos;
                }

                //System.out.println(pick5stats);
                HoldStats.getInstance().setPick5stats(pick5stats);

                // Quality Assurance
                checkNumOfLottos /= 5;
                System.out.println("Num of Lottos from Pick5: " + checkNumOfLottos);

                if(numOfLottos==0)
                {
                    numOfLottos = checkNumOfLottos;
                    HoldStats.getInstance().setNumOfLottos(numOfLottos);
                }

                // for some reason fill5 is called before fillMega
                if(checkNumOfLottos != numOfLottos)
                {
                    Log.e("WARNING", "NUM OF LOTTOS DO NOT MATCH");
                    System.out.println(numOfLottos + " " + checkNumOfLottos);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    // fill stats for mega ball from file
    private void fillMegaStats()
    {

        FirebaseDatabase.getInstance().getReference("winningmega").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(numOfLottos != 0){ numOfLottos = 0;}

                megaBstats = new HashMap<>();

                for( int i=1; i<=15; ++i )
                {
                    megaBstats.put( i, 0 ); // initialize with zeros
                }

                String[] split = dataSnapshot.getValue(String.class).split("\\s+");

                for(String s : split)
                {
                    int num = Integer.parseInt(s);
                    megaBstats.put(num, megaBstats.get(num) + 1); // increment stat count;

                    ++numOfLottos;
                }

                HoldStats.getInstance().setMegaBstats(megaBstats);
                HoldStats.getInstance().setNumOfLottos(numOfLottos);

                System.out.println("Num of Lottos from Mega: " + numOfLottos);




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    // to reduce the typing involved in print statements
    public static <T> void print( T t)
    {
        System.out.println(t);
    }


} // end class
