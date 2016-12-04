package com.example.simon.mobileassignment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Simon on 06/11/2016.
 * Class that users record their song in and save it to file
 */

public class RecordingScreen extends AppCompatActivity implements View.OnClickListener
{
    Song s;

    Button saveButton,playButton;

    Boolean recordingStarted = false;
    Boolean currentlyRecording = false;
    Boolean songSaved = false;
    Boolean chordPressed = false;

    int timerCounter = 0;
    int pressed = -1;

    MusicVisualization m;
    PianoKeys p;

    Timer recordTimer;
    TimerTask recordTask;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_recording_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent i = getIntent();
        String songName = i.getStringExtra("songName");
        String songBPM = i.getStringExtra("songBPM");
        String songKey = i.getStringExtra("songKey");

        s = new Song(songName,songKey,Integer.parseInt(songBPM));

        saveButton = (Button)findViewById(R.id.savebutton);
        playButton = (Button)findViewById(R.id.playbutton);

        saveButton.setOnClickListener(this);
        playButton.setOnClickListener(this);

        //initializes and starts the music visualization
        m = (MusicVisualization)findViewById(R.id.canvas);
        m.started = true;

        //passes the song to the piano, allowing it to load the song's key
        p = (PianoKeys)findViewById(R.id.pianoKeys);
        p.setData(this,s);
    }

    //creates recording timer
    void startRecording()
    {
        recordTask = new TimerTask()
        {
            @Override
            public void run()
            {
                if(currentlyRecording)
                {
                    //writes the note pressed and then clears the pressed integer, ready for the next note
                    s.write(pressed,chordPressed);
                    chordPressed = false;
                    pressed = -1;

                    timerCounter++;
                }
            }
        };

        recordTimer = new Timer("RecordTest",true);
        recordTimer.scheduleAtFixedRate(recordTask,0,s.timerInc);

        recordingStarted = true;
        currentlyRecording = true;
    }


    void toggleRecording()
    {
        if(recordingStarted)
        {
            if(currentlyRecording)
            {
                Toast.makeText(this,"Recording Paused",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"Recording Resumed",Toast.LENGTH_SHORT).show();
            }

            currentlyRecording = !currentlyRecording;
        }
        else
        {
            Toast.makeText(this,"Recording Started",Toast.LENGTH_SHORT).show();
            startRecording();
        }
    }

    //saves a new song or updates a previously saved song
    public void saveSong()
    {
        DBHelper db = new DBHelper(this).open();

        if(songSaved)//Re-save Song
        {
            db.updateSong(Long.parseLong(s.rowId),s.name,s.key,s.data.size());
            Toast.makeText(getApplicationContext(),"Song Updated",Toast.LENGTH_LONG).show();
        }
        else//Create new song file
        {
            db.insertSong(s.name,s.key,s.data.size());

            //Get song's row id for file name
            Cursor c = db.getAllSongs();
            c.moveToLast();

            s.rowId = c.getString(0);

            Toast.makeText(getApplicationContext(),"Song Created",Toast.LENGTH_LONG).show();
            songSaved = true;
        }

        FileManager.saveSong(s,s.rowId,this);
    }

    //pressKey is the method used by PianoKeys to send data to the RecordingScreen
    public void pressKey(int i,Boolean c)
    {
        pressed = i;
        chordPressed = c;
        m.setLastPressed(i,c);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.savebutton)
        {
            saveSong();
        }
        else if(v.getId()==R.id.playbutton)
        {
            toggleRecording();
        }
    }

    @Override
    protected void onStop()
    {
        try
        {
            recordTimer.cancel();
        }
        catch (Exception e) { e.printStackTrace(); }

        super.onStop();
    }
}
