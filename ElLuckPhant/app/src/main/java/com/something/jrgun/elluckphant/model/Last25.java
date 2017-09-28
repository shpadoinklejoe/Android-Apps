package com.something.jrgun.elluckphant.model;

import android.os.AsyncTask;
import android.text.TextUtils;

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
import java.util.Date;
import java.text.SimpleDateFormat;


/**
 * Checks for any updates to database in background
 * updates database and BallStats if necessary
 */

public class Last25 {

    private static FirebaseDatabase mydatabase; // so based on same instance
    private static ArrayList<ArrayList<String>> last25drawings = null; // last 25 winning numbers pulled from website
    private static String allpick5 = null;
    private static String allmega = null;

    public Last25()
    {
        new GetLast25().execute();
    }


    // retrieve last 25 winning numbers from lottery's official website
    private class GetLast25 extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... params)
        {
            try {
                Document doc = Jsoup.connect("http://www.megamillions.com/winning-numbers/last-25-drawings").userAgent("mozilla/17.0").get();
                Elements winning_numbers_table = doc.select("tr");

                // save winning lotto picks into 2D list
                last25drawings = new ArrayList<ArrayList<String>>();
                for (Element e : winning_numbers_table)
                {
                    last25drawings.add(new ArrayList<String>(e.getElementsByTag("td").eachText()));
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            print("last 25: ");
            print2D(last25drawings);
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            checkLast25();
        }
    }


    // double check lotto website vs my database
    private void checkLast25()
    {
        mydatabase = FirebaseDatabase.getInstance();
        mydatabase.getReference("lastupdate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy");
                Date lastupdate = null;
                Date lastdraw = null;

                // date needs try-catch to not throw error
                try {
                    lastupdate = formatter.parse(dataSnapshot.getValue(String.class));
                    lastdraw = formatter.parse(last25drawings.get(1).get(0));
                }
                catch(ParseException e) {
                    e.printStackTrace();
                }
                print("last update: " + lastupdate);
                print("last draw: " + lastdraw);

                // if needs updating
                if( lastupdate.before(lastdraw) )
                {
                    ArrayList<String> newpick5 = new ArrayList<>();
                    ArrayList<String> newmega = new ArrayList<>();
                    ArrayList<String> thisdraw;

                    // iterate through winning draws in ascending order
                    for (int i=last25drawings.size()-1; i>0; --i) //intentionally avoid header
                    {
                        Date thisdate = null;
                        thisdraw = last25drawings.get(i);

                        try {
                            thisdate = formatter.parse(thisdraw.get(0));
                        }
                        catch(ParseException e) {
                            e.printStackTrace();
                        }

                        // ignore all draws before lastupdate
                        if( thisdate.after(lastupdate) )
                        {
                            print("needs to be added: " + thisdraw);

                            // first save them into their respective arrays
                            newpick5.addAll( thisdraw.subList(1,6) );
                            newmega.add( thisdraw.get(6) );
                        }
                    }

                    // okay now update database
                    updateDatabase( TextUtils.join(" ", newpick5), TextUtils.join(" ", newmega), formatter.format(lastdraw) );

                }
                // else do nothing

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    // updates lotto numbers in my database
    private void updateDatabase(final String newpick5, final String newmega, String lastdraw)
    {
        print("new pick 5 balls: " + newpick5);
        print("new mega balls: " + newmega);
        print("new update date: " + lastdraw);


        // first retrieve data
        mydatabase.getReference("winning5").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // split out tabs
                String[] split = dataSnapshot.getValue(String.class).split("\\s+") ;

                allpick5 = newpick5 + " " + TextUtils.join(" ", split);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mydatabase.getReference("winningmega").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                allmega = newmega + " " + dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        print("all pick5: " + allpick5);
        print("all mega: " + allmega);


        // the very last step is to refill the global stats
        //CalculateStats cs = new CalculateStats();
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
