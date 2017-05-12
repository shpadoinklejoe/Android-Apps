package com.something.jrgun.elluckphant.model;

import com.something.jrgun.elluckphant.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by jrgun on 4/19/2017.
 */

public class GenerateNumbers
{
    private Random rand = new Random();

    // class variables
    private double numOfLottos = 0;
    private ArrayList<Integer> nums; // array for pick 5 and mega ball to be returned
    private HashMap<Integer, Integer> pick5stats; // keeps track of how many times
    private HashMap<Integer, Integer> megaBstats; // each ball has been picked
    private ArrayList<ArrayList<Integer>> play5 = new ArrayList<ArrayList<Integer>>();


    // default constructor
    // used for multiple number pulls that share same statMaps
    public GenerateNumbers()
    {
        fill5stats();
        fillMegaStats();
        this.nums = null;
    }

    // constructor for individual number pulls
    public GenerateNumbers(boolean b)
    {
        fill5stats();
        fillMegaStats();
        this.nums = statisticalPicks();
    }


    // getter for ArrayList of lotto balls
    public ArrayList<Integer> getNums()
    {
        return this.nums;
    }

    // getter for pick 5 stats
    public HashMap<Integer, Integer> getPick5stats()
    {
        return pick5stats;
    }

    // getter for mega ball stats
    public HashMap<Integer, Integer> getMegaBstats()
    {
        return megaBstats;
    }

    // getter for 5 sets of lotto numbers
    public ArrayList<ArrayList<Integer>> getPlay5()
    {
        return play5;
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


    // Generates numbers based on frequency count
    // Key = lotto ball face value
    // Value = number of times ball picked
    private ArrayList<Integer> statisticalPicks()
    {
        ArrayList<Integer> randNums = new ArrayList<>();
        double ratio = 1/15.0; // (5/75) == (1/15)

        // Generate Pick 5
        for(int i=0; i<5; ++i )
        {
            int num = rand.nextInt(100); // no number is favored when modding by multiple of 10
            ++num; // since random number chosen was between 0 -> 74

            // since modding to get random number is statistically flawed
            while( num > 75 || (pick5stats.get(num)/numOfLottos) > ratio || randNums.contains(num) )
            {
                num = rand.nextInt(100);
                ++num;
            }

            randNums.add( num ); // add number
            //pick5stats.put(num, pick5stats.get(num)+1 ); // increment count

        }
        Collections.sort(randNums); // sort for sequential output

        // generate Mega Ball
        int num = rand.nextInt(100);
        ++num;
        while( num > 15 || (megaBstats.get(num)/numOfLottos) > ratio )
        {
            num = rand.nextInt(100);
            ++num;
        }

        randNums.add( num ); // add number
        //megaBstats.put( num, megaBstats.get(num)+1 ); // increment count

        return randNums;
    }


    // Takes all numbers not pulled under its ratio
    // Randomizes them and evenly distributes them over 5 plays
    // ( because it's not good to over-play a single (randomly chosen) number multiple times )
    void statisticalPlay5()
    {
        ArrayList<Integer> play5pick5 = new ArrayList<>();
        ArrayList<Integer> play5mega = new ArrayList<>();
        double ratio = 1/15.0; // (5/75) == (1/15)

        // generate Pick 5 numbers
        for(Map.Entry<Integer, Integer> e : pick5stats.entrySet())
        {
            if( e.getValue()/numOfLottos < ratio )
            {
                play5pick5.add(e.getKey());
            }
        }
        Collections.shuffle(play5pick5); // sort for sequential output
        System.out.println(play5pick5);

        // generate Mega Ball numbers
        for(Map.Entry<Integer, Integer> e : megaBstats.entrySet())
        {
            if( e.getValue()/numOfLottos < ratio )
            {
                play5mega.add(e.getKey());
            }
        }
        Collections.shuffle(play5mega); // sort for sequential output
        System.out.println(play5mega + "\n");

        // fill 2D array with 5 sets of lotto numbers
        for( int i=0; i<25; ++i )
        {
            if( i%5 == 0 )
            {
                play5.add(new ArrayList<Integer>());
            }

            play5.get(i/5).add( play5pick5.get(i) );

        }

        // sort arrays
        int play = 0;
        for( ArrayList<Integer> array : play5 )
        {
            Collections.sort(array);
            play5.get(play).add( play5mega.get(play) );
            ++play;
        }

    }


    // fill stats for pick 5 from file
    private void fill5stats()
    {
        pick5stats = new HashMap<>();
        int numCheck = 0;

        for( int i=1; i<=75; ++i )
        {
            pick5stats.put( i, 0 ); // initialize with zeros
        }

        try{
            Scanner scan = new Scanner(new FileReader("winning5.txt"));

            while (scan.hasNext() ) {
                String text = scan.nextLine();
                String[] split = text.split("\\s+");

                for(String s : split)
                {
                    int num = Integer.parseInt(s);
                    pick5stats.put(num, pick5stats.get(num)+1 ); // increment stat count;
                }
                ++numCheck;
            }
            scan.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(pick5stats);
        System.out.println(numCheck);
    }

    // fill stats for mega ball from file
    private void fillMegaStats()
    {
        megaBstats = new HashMap<>();

        for( int i=1; i<=15; ++i )
        {
            megaBstats.put( i, 0 ); // initialize with zeros
        }

        try{
            Scanner scan = new Scanner(new FileReader("@raw/winningmega.txt"));

            while (scan.hasNext() )
            {
                int num = Integer.parseInt(scan.nextLine());
                megaBstats.put(num, megaBstats.get(num)+1 ); // increment stat count;

                ++numOfLottos;
            }
            scan.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(megaBstats);
        System.out.println(numOfLottos);
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



