package com.example.simon.mobileassignment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;

/**
 * Created by Simon on 16/11/2016.
 * Class to store the PianoKeys view, the view that displays the keys of a piano to play songs on
 */

public class PianoKeys extends View
{
    int canvasWidth,canvasHeight;
    RecordingScreen parent;

    SoundPool sounds;
    AudioManager am;

    //SoundPool map that maps index numbers of piano keys to their corresponding notes
    HashMap soundPoolMap;

    float streamVolume;
    Rect def,cover;
    Paint black;

    Song s;
    String[] currentNoteNames;

    public PianoKeys(Context context)
    {
        super(context);
        init(context);
    }

    public PianoKeys(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PianoKeys(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
        setWillNotDraw(false);
    }

    private void init(Context context)
    {
        def = new Rect();
        cover = new Rect();

        black = new Paint();
        black.setColor(Color.BLACK);
        black.setStyle(Paint.Style.FILL);
        black.setTextSize(20);
        black.setTextAlign(Paint.Align.CENTER);
    }

    //Sets the song, parent, and audio data of the piano keys
    void setData(RecordingScreen p,Song song)
    {
        parent = p;
        s = song;

        setAudioData();

        sounds = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        soundPoolMap = MusicPlayer.makeSoundPoolMapFromKey(s.key,parent,sounds,s.numKeys);
        currentNoteNames = MusicPlayer.lastMadeNoteNames;

        setListener();
    }

    //OnDraw method, which is only called once to draw the piano
    @Override
    protected void onDraw(Canvas canvas)
    {
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        //Get 1/12 the width of the piano to draw each key
        int incWidth = (int)((float)canvasWidth/12.0);

        super.onDraw(canvas);

        //Draw the horizontal lines of the piano, including the line to split the notes and the chords
        canvas.drawLine(0,0,canvasWidth,0,black);
        canvas.drawLine(0,canvasHeight/2,canvasWidth,canvasHeight/2,black);

        //Draw the vertical lines which split the keys
        for(int i=0;i<12;i++)
        {
            canvas.drawLine(incWidth*i,0,incWidth*i,canvasHeight,black);
            canvas.drawText(currentNoteNames[i],(incWidth*i)+incWidth/2,canvasHeight/1.5f,black);
        }

        //Draw a black rectangle at the edge of the screen to cover any extraneous white space
        cover.set(incWidth*12,0,incWidth*13,canvasHeight);
        canvas.drawRect(cover,black);
    }

    void setAudioData()
    {
        am = (AudioManager)parent.getSystemService(Context.AUDIO_SERVICE);
        streamVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume / am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    //Sets the onTouchListener of the keys, which plays the required sounds
    void setListener()
    {
        View.OnTouchListener v = new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                float x = event.getX();
                float y = event.getY();
                int numPressed = ((int)((x/(float)canvasWidth)*12f));

                double height = Math.floor((y/canvasHeight)*2);

                //Sound only plays when a key is pressed, not released
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                {
                    sounds.play((Integer) soundPoolMap.get(numPressed), streamVolume, streamVolume, 1, 0, 1f);

                    //if the user tapped a chord instead of a note, press the other notes of that chord as well
                    if (height < 1)
                    {
                        sounds.play((Integer) soundPoolMap.get(numPressed + 2), streamVolume, streamVolume, 1, 0, 1f);
                        sounds.play((Integer) soundPoolMap.get(numPressed + 4), streamVolume, streamVolume, 1, 0, 1f);
                        parent.pressKey(numPressed, true);
                    }
                    else
                    {
                        parent.pressKey(numPressed, false);
                    }
                }

                return false;
            }
        };

        this.setOnTouchListener(v);
    }
}
