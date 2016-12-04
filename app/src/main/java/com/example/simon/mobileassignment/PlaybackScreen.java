package com.example.simon.mobileassignment;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Simon on 06/11/2016.
 * Class that users can load a song into to play it
 */

public class PlaybackScreen extends AppCompatActivity
{
    Song s;

    boolean songPlaying = false;
    boolean songStartedPlaying = false;

    Timer playbackTimer;
    TimerTask playbackTask;
    int playbackCounter = 0;

    SoundPool sounds;
    AudioManager am;
    HashMap soundPoolMap;

    float streamVolume;

    MusicVisualization m;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_playback_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        String rowid = getIntent().getStringExtra("songFileName");
        s = FileManager.loadSong(rowid,this);

        am = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        streamVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume / am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        //quits the screen if file was incorrectly loaded
        try
        {
            sounds = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
            soundPoolMap = MusicPlayer.makeSoundPoolMapFromKey(s.key,this,sounds,s.numKeys);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
            this.finish();
        }

        Button playButton = (Button)findViewById(R.id.playbutton);
        playButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                toggleSongPlaying();
            }
        });

        Button mainMenuButton = (Button)findViewById(R.id.menubutton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        m = (MusicVisualization)findViewById(R.id.canvas);

    }

    void toggleSongPlaying()
    {
        if(!songStartedPlaying)
        {
            initializeSongTimer();
            songPlaying = true;
            songStartedPlaying = true;
            m.started = true;
        }
        else
        {
            songPlaying =!songPlaying;
        }
    }

    //runs a timer which plays back the music data in the song file and animates it, as long as it is active
    void initializeSongTimer()
    {
        playbackTask = new TimerTask()
        {
            @Override
            public void run()
            {
                if(songPlaying)
                {
                    //reads the nect block from the chord
                    Song.NoteBlock currentBlock = s.read(playbackCounter);

                    if (currentBlock == null)//Song is over
                    {
                        songStartedPlaying = false;
                        songPlaying = false;
                        playbackCounter = 0;

                        this.cancel();
                        //stops timer when song finishes
                    }
                    else//song continues
                    {
                        int currentNote = currentBlock.note;

                        if (currentNote != -1)//Block isn't a blank
                        {
                            sounds.play((Integer) soundPoolMap.get(currentNote), streamVolume, streamVolume, 1, 0, 1f);

                            if(currentBlock.isChord)//plays three notes if block is a chord
                            {
                                sounds.play((Integer) soundPoolMap.get(currentNote+2), streamVolume, streamVolume, 1, 0, 1f);
                                sounds.play((Integer) soundPoolMap.get(currentNote+4), streamVolume, streamVolume, 1, 0, 1f);
                            }

                            m.setLastPressed(currentNote,currentBlock.isChord,getSelf());
                        }

                        playbackCounter++;
                    }
                }
            }
        };

        playbackTimer = new Timer("RecordTest",true);

        //plays the timer as fast as the song's bpm
        playbackTimer.scheduleAtFixedRate(playbackTask,0,s.timerInc);
    }

    Activity getSelf()
    {
        return this;
    }

    //stops the timer when screen is stopped
    @Override
    protected void onStop()
    {
        try
        {
            playbackTimer.cancel();
        }
        catch (Exception e) { e.printStackTrace(); }

        super.onStop();
    }
}
