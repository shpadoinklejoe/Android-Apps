package com.something.jrgun.elluckphant.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

/**
 * Created by jrgun on 4/19/2017.
 */

public class GenerateNumbers {
    private Random rand = new Random();


    // class variables
    ArrayList<Integer> nums;


    // default constructor
    public GenerateNumbers()
    {
        this.nums = generateRandom();
    }


    // getter for ArrayList of lotto balls
    public ArrayList<Integer> getNums()
    {
        return this.nums;
    }

    // generate lotto numbers randomly
    ArrayList<Integer> generateRandom()
    {
        ArrayList<Integer> randNums = new ArrayList<>();

        for(int i=0; i<5; ++i )
        {
            int n =  rand.nextInt(75)+1;
            while( randNums.contains(n))
            {
                n = rand.nextInt(75)+1;
            }
            randNums.add( n );
        }
        Collections.sort(randNums);

        randNums.add(rand.nextInt(15)+1 );

        return randNums;
    }


    // to print without typing out "system.out.println"
    public void print()
    {
        System.out.println(this);
    }

    // nicely formatted lotto numbers
    public String toString()
    {
        String s = "[ ";
        for( int i=0; i<6; ++i )
        {
            int current = this.nums.get(i);

            if( i >= 0 && i < this.nums.size()-2 )
            {
                if( current < 10)
                {
                    s += " ";
                }
                s += current + " - ";
            }
            else if( i == this.nums.size()-2 )
            {
                if( current < 10)
                {
                    s = " " + s;
                }

                s += current;
            }
            else{
                s += " ] [ ";
                if( current < 10)
                {
                    s += " ";
                }
                s += current + " ]";
            }
        }
        return s;
    }


} // end class



