package com.something.jrgun.elluckphant.model;

import android.os.AsyncTask;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Date;
import java.text.SimpleDateFormat;


/**
 * Checks for any updates to database in background
 * updates database and BallStats if necessary
 */

public class GetLast25 {

    private static ArrayList<ArrayList<String>> last25drawings = null; // last 25 winning numbers pulled from website

    public GetLast25()
    {
        new GrabLast25().execute();
    }


    // retrieve last 25 winning numbers from lottery's official website
    private class GrabLast25 extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... params)
        {

            try {

                Document doc = Jsoup.connect("http://www.megamillions.com/winning-numbers/last-25-drawings").userAgent("mozilla/17.0").get();
                //Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/Cat").userAgent("mozilla/17.0").get();

                Elements winning_numbers_table = doc.select("tr");


                // save winning lotto picks into 2D list
                last25drawings = new ArrayList<ArrayList<String>>();
                for (Element e : winning_numbers_table) {
                    last25drawings.add(new ArrayList<String>(e.getElementsByTag("td").eachText()));
                }

            } catch (IOException e) {

                e.printStackTrace();
            }

            print("last 25: ");
            print2D(last25drawings);
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            updateDatabase();
        }
    }


    // double check lotto website vs my database
    private void updateDatabase()
    {

        FirebaseDatabase.getInstance().getReference("lastupdate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                SimpleDateFormat format = new SimpleDateFormat("M/d/yyyy");
                Date lastupdate = null;
                Date lastdraw = null;

                // date needs try-catch to not throw error
                try {

                    lastupdate = format.parse(dataSnapshot.getValue(String.class));
                    lastdraw = format.parse(last25drawings.get(1).get(0));
                }
                catch(ParseException e)
                {
                    e.printStackTrace();
                }
                print("last update: " + lastupdate);
                print("last draw: " + lastdraw);

                // if needs updating
                if( lastupdate.before(lastdraw) )
                {

                    // iterate through winning numbers backwards
                    for (int i=last25drawings.size()-1; i>0; --i) //intentionally avoid header
                    {
                        Date thisdraw = null;
                        try {

                            thisdraw = format.parse(last25drawings.get(i).get(0));
                        }
                        catch(ParseException e)
                        {
                            e.printStackTrace();
                        }

                        // ignore all draws before lastupdate
                        if( thisdraw.after(lastupdate) )
                        {
                            //TODO
                            // um, now how do i add the new numbers to the database...
                            print("needs to be added: " + last25drawings.get(i));

                        }
                    }

                    // okay now update database
                    // updateDatabase();

                    // the very last step is to refill the global stats
                    //CalculateStats cs = new CalculateStats();

                }
                // else do nothing

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

    // generic 2D printing of arraylist objects
    public static <T> void print2D( ArrayList<ArrayList<T>> al)
    {
        for( ArrayList<T> l : al )
        {
            System.out.println(l);
        }
    }
}
