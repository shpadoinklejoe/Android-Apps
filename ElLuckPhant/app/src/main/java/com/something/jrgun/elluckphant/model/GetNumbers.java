package com.something.jrgun.elluckphant.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * This class is so numbers can be pulled from database only once
 */

public class GetNumbers
{
    private static double numOfLottos;
    private HashMap<Integer, Integer> pick5stats; // keeps track of how many times
    private HashMap<Integer, Integer> megaBstats; // each ball has been picked


    // getters //
    public double getNumOfLottos() {
        return numOfLottos;
    }

    public HashMap<Integer, Integer> getPick5stats() {
        return pick5stats;
    }

    public HashMap<Integer, Integer> getMegaBstats() {
        return megaBstats;
    }


    // default constructor
    public GetNumbers()
    {
        fill5stats();
        fillMegaStats();
    }


    // fill stats for pick 5 from file
    private void fill5stats()
    {
        pick5stats = new HashMap<>();

        for (int i = 1; i <= 75; ++i) {
            pick5stats.put(i, 0); // initialize with zeros
        }

        FirebaseDatabase.getInstance().getReference("winning5").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                //numOfLottos = 0;
                String[] split = dataSnapshot.getValue(String.class).split("\\s+");

                for(String s : split)
                {
                    int num = Integer.parseInt(s);
                    pick5stats.put(num, pick5stats.get(num) + 1); // increment stat count;

                    //++numOfLottos;
                    numOfLottos = numOfLottos +1;
                }
                numOfLottos /= 5;
                System.out.println("Num of Lottos from Pick5: " + numOfLottos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    // fill stats for mega ball from file
    private void fillMegaStats()
    {
        megaBstats = new HashMap<>();

        for( int i=1; i<=15; ++i )
        {
            megaBstats.put( i, 0 ); // initialize with zeros
        }

        FirebaseDatabase.getInstance().getReference("winningmega").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                double checkNumOfLottos = 0;
                String[] split = dataSnapshot.getValue(String.class).split("\\s+");

                for(String s : split)
                {
                    int num = Integer.parseInt(s);
                    megaBstats.put(num, megaBstats.get(num) + 1); // increment stat count;

                    ++checkNumOfLottos;
                }
                System.out.println("Num of Lottos from Mega: " + checkNumOfLottos);

                if(checkNumOfLottos != numOfLottos)
                {
                    Log.e("WARNING", "NUM OF LOTTOS DO NOT MATCH");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


} // end class
