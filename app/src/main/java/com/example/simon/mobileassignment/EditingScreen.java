package com.example.simon.mobileassignment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Simon on 06/11/2016.
 * Class for the editing screen, which users can load a song into to change its name, key and BPM
 */

public class EditingScreen extends AppCompatActivity
{
    Song s;

    Button doneButton,cancelButton;
    EditText songName,songBPM;
    Spinner songKey;

    String keySelection;
    //KeySelection, which stores the current key selected in the spinner

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_editing_screen);

        //Load song from its id number
        String rowid = getIntent().getStringExtra("songFileName");
        s = FileManager.loadSong(rowid,this);

        //Catches a NullPointerException if file is not successfully loaded and exits the screen
        try
        {
            configureButtons();
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
            this.finish();
        }
    }

    public void configureButtons()
    {
        doneButton = (Button)findViewById(R.id.donebutton);
        cancelButton = (Button)findViewById(R.id.cancelbutton);
        songName = (EditText)findViewById(R.id.songnameedittext);
        songBPM = (EditText)findViewById(R.id.bpmedittext);
        songKey = (Spinner)findViewById(R.id.songkeychooser);

        songName.setText(s.name);
        songBPM.setText(Integer.toString(s.bpm));

        Integer keyNumber = (Integer)MusicPlayer.keyNamesNumbers.get(s.key);

        songKey.setSelection(keyNumber);

        //Updates the keySelection string if the key selected on the spinner is changed
        songKey.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                keySelection = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //Saves the newly edited song
        doneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //updates the song class
                s.name = songName.getText().toString();
                s.bpm = Integer.parseInt(songBPM.getText().toString());
                s.key = keySelection;

                //updates the song in the database
                DBHelper db = new DBHelper(getApplicationContext()).open();
                db.updateSong(Long.parseLong(s.rowId),s.name,s.key,s.data.size());

                //saves the new version of the song
                FileManager.saveSong(s,s.rowId,getSelf());
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    //method to retrieve the activity from within the onClickListener
    Activity getSelf()
    {
        return this;
    }
}
