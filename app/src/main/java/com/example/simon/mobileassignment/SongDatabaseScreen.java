package com.example.simon.mobileassignment;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Simon on 06/11/2016.
 * Class that retrieves and displays all songs stored in the database
 */

public class SongDatabaseScreen extends ListActivity
{
    ListView lv;

    ArrayList<String> NameArray;
    ArrayList<String> rowIds;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_screen);

        NameArray  = new ArrayList<String>();
        rowIds  = new ArrayList<String>();

        DBHelper db = new DBHelper(this).open();

        Cursor c  = db.getAllSongs();
        c.moveToFirst();

        try
        {
            do
            {
                String songName = c.getString(1);

                if(songName.isEmpty())
                {
                    songName = "(No Name)";
                }

                NameArray.add("Name: " + songName + "   Length: " + c.getString(3));
                rowIds.add(c.getString(0));

            } while (c.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, NameArray);

            lv = this.getListView();
            lv.setAdapter(adapter);

            final String nextScreen = getIntent().getStringExtra("OpenSongFor");

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id)
                {
                    String rowid = rowIds.get(position);

                    Intent i;

                    if (nextScreen.equals("Playing"))
                    {
                        i = new Intent(v.getContext(), PlaybackScreen.class);
                    }
                    else
                    {
                        i = new Intent(v.getContext(), EditingScreen.class);
                    }

                    i.putExtra("songFileName", rowid);
                    startActivity(i);
                    finish();
                }
            });
        }
        catch(CursorIndexOutOfBoundsException e)
        {
            Toast.makeText(getApplicationContext(),"No Songs Found",Toast.LENGTH_LONG).show();
        }
    }
}
