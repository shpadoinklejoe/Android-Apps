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
    private double numOfLottos;
    private static HashMap<Integer, Integer> pick5stats; // keeps track of how many times
    private static HashMap<Integer, Integer> megaBstats; // each ball has been picked



    // default constructor
    public GetNumbers()
    {
        fill5stats();
        fillMegaStats();
    }


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
                checkNumOfLottos /= 5;
                System.out.println("Num of Lottos from Pick5: " + checkNumOfLottos);

                if(numOfLottos==0){numOfLottos = checkNumOfLottos;}

//                // for some reason fill5 is called before fillMega
//                if(checkNumOfLottos != numOfLottos)
//                {
//                    Log.e("WARNING", "NUM OF LOTTOS DO NOT MATCH");
//                    System.out.println(numOfLottos + " " + checkNumOfLottos);
//                }

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

                System.out.println("Num of Lottos from Mega: " + numOfLottos);




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


} // end class
